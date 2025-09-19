package com.biblioteca.presentation.util;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Cliente para comunicação com a API REST da aplicação.
 * <p>
 * Esta classe fornece métodos para realizar requisições HTTP para a API,
 * incluindo operações GET, POST, PUT, DELETE e envio de arquivos multipart.
 * Utiliza o HttpClient do Java para comunicação e Jackson para serialização/desserialização JSON.
 * </p>
 *
 * @author Biblioteca A7
 * @version 1.0
 */
public class ApiClient {

    /**
     * URL base da API REST.
     */
    private static final String BASE_URL = "http://localhost:8080/biblioteca-a7/api";
    
    /**
     * Cliente HTTP para realizar as requisições.
     */
    private final HttpClient client;
    
    /**
     * Objeto para serialização/desserialização JSON.
     */
    private final ObjectMapper objectMapper;


    /**
     * Construtor padrão.
     * Inicializa o cliente HTTP com timeout de conexão e configura o ObjectMapper
     * com suporte para classes do Java 8 Date/Time API.
     */
    public ApiClient() {
        this.client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();

        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    /**
     * Realiza uma requisição GET para a API.
     *
     * @param <T> o tipo de retorno esperado
     * @param path o caminho relativo da API
     * @param responseType o tipo de retorno esperado
     * @return o objeto desserializado da resposta
     * @throws IOException se ocorrer um erro de I/O
     * @throws InterruptedException se a operação for interrompida
     */
    public <T> T get(String path, Type responseType) throws IOException, InterruptedException {
        HttpRequest request = buildGetRequest(path);
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        checkResponse(response);
        JavaType javaType = objectMapper.getTypeFactory().constructType(responseType);
        return objectMapper.readValue(response.body(), javaType);
    }

    /**
     * Realiza uma requisição POST para a API.
     *
     * @param <T> o tipo de retorno esperado
     * @param path o caminho relativo da API
     * @param body o objeto a ser enviado no corpo da requisição
     * @param responseType o tipo de retorno esperado
     * @return o objeto desserializado da resposta
     * @throws IOException se ocorrer um erro de I/O
     * @throws InterruptedException se a operação for interrompida
     */
    public <T> T post(String path, Object body, Type responseType) throws IOException, InterruptedException {
        String requestBody = objectMapper.writeValueAsString(body);
        HttpRequest request = buildPostRequest(path, requestBody);
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        checkResponse(response);
        JavaType javaType = objectMapper.getTypeFactory().constructType(responseType);
        return objectMapper.readValue(response.body(), javaType);
    }

    /**
     * Realiza uma requisição PUT para a API.
     *
     * @param <T> o tipo de retorno esperado
     * @param path o caminho relativo da API
     * @param body o objeto a ser enviado no corpo da requisição
     * @param responseType o tipo de retorno esperado
     * @return o objeto desserializado da resposta
     * @throws IOException se ocorrer um erro de I/O
     * @throws InterruptedException se a operação for interrompida
     */
    public <T> T put(String path, Object body, Type responseType) throws IOException, InterruptedException {
        String requestBody = objectMapper.writeValueAsString(body);
        HttpRequest request = buildPutRequest(path, requestBody);
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        checkResponse(response);
        JavaType javaType = objectMapper.getTypeFactory().constructType(responseType);
        return objectMapper.readValue(response.body(), javaType);
    }

    /**
     * Realiza uma requisição DELETE para a API.
     *
     * @param path o caminho relativo da API
     * @throws IOException se ocorrer um erro de I/O
     * @throws InterruptedException se a operação for interrompida
     */
    public void delete(String path) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + path))
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        checkResponse(response);
    }

    /**
     * Realiza uma requisição POST multipart para a API.
     * Útil para envio de arquivos.
     *
     * @param path o caminho relativo da API
     * @param fieldName o nome do campo do formulário
     * @param file o arquivo a ser enviado
     * @throws IOException se ocorrer um erro de I/O
     * @throws InterruptedException se a operação for interrompida
     */
    public void postMultipart(String path, String fieldName, File file) throws IOException, InterruptedException {
        String boundary = "---SuaBibliotecaBoundary" + UUID.randomUUID().toString();
        HttpRequest request = buildMultipartRequest(path, fieldName, file, boundary);
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        checkResponse(response);
    }

    /**
     * Constrói uma requisição HTTP multipart.
     *
     * @param path o caminho relativo da API
     * @param fieldName o nome do campo do formulário
     * @param file o arquivo a ser enviado
     * @param boundary o delimitador de partes do multipart
     * @return a requisição HTTP configurada
     * @throws IOException se ocorrer um erro de I/O
     */
    private HttpRequest buildMultipartRequest(String path, String fieldName, File file, String boundary) throws IOException {
        Path filePath = file.toPath();
        String fileName = filePath.getFileName().toString();
        String mimeType = Files.probeContentType(filePath);
        if (mimeType == null) {
            mimeType = "application/octet-stream"; // Default MIME type
        }

        byte[] fileBytes = Files.readAllBytes(filePath);

        List<byte[]> byteArrays = new ArrayList<>();
        byteArrays.add(("--" + boundary + "\r\n").getBytes(StandardCharsets.UTF_8));
        byteArrays.add(("Content-Disposition: form-data; name=\"" + fieldName + "\"; filename=\"" + fileName + "\"\r\n").getBytes(StandardCharsets.UTF_8));
        byteArrays.add(("Content-Type: " + mimeType + "\r\n\r\n").getBytes(StandardCharsets.UTF_8));
        byteArrays.add(fileBytes);
        byteArrays.add(("\r\n--" + boundary + "--\r\n").getBytes(StandardCharsets.UTF_8));

        return HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + path))
                .header("Content-Type", "multipart/form-data; boundary=" + boundary)
                .POST(HttpRequest.BodyPublishers.ofByteArrays(byteArrays))
                .build();
    }

    /**
     * Constrói uma requisição HTTP GET.
     *
     * @param path o caminho relativo da API
     * @return a requisição HTTP configurada
     */
    private HttpRequest buildGetRequest(String path) {
        return HttpRequest.newBuilder().uri(URI.create(BASE_URL + path)).GET().build();
    }

    /**
     * Constrói uma requisição HTTP POST.
     *
     * @param path o caminho relativo da API
     * @param body o corpo da requisição em formato JSON
     * @return a requisição HTTP configurada
     */
    private HttpRequest buildPostRequest(String path, String body) {
        return HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + path))
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .header("Content-Type", "application/json")
                .build();
    }

    /**
     * Constrói uma requisição HTTP PUT.
     *
     * @param path o caminho relativo da API
     * @param body o corpo da requisição em formato JSON
     * @return a requisição HTTP configurada
     */
    private HttpRequest buildPutRequest(String path, String body) {
        return HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + path))
                .PUT(HttpRequest.BodyPublishers.ofString(body))
                .header("Content-Type", "application/json")
                .build();
    }

    /**
     * Verifica se a resposta HTTP é bem-sucedida (código 2xx).
     * Lança uma exceção se a resposta indicar erro.
     *
     * @param response a resposta HTTP a ser verificada
     * @throws IOException se a resposta indicar erro
     */
    private void checkResponse(HttpResponse<String> response) throws IOException {
        if (response.statusCode() < 200 || response.statusCode() >= 300) {
            throw new IOException("Erro na requisição: " + response.statusCode() + " - " + response.body());
        }
    }

    /**
     * Cria um tipo parametrizado para uma lista de objetos.
     * Útil para desserialização de listas genéricas.
     *
     * @param type o tipo dos elementos da lista
     * @return o tipo parametrizado List<type>
     */
    public static Type listOf(Class<?> type) {
        return new ParameterizedTypeImpl(List.class, new Type[] { type });
    }

    /**
     * Cria um tipo parametrizado para um mapa de objetos.
     * Útil para desserialização de mapas genéricos.
     *
     * @param keyType o tipo das chaves do mapa
     * @param valueType o tipo dos valores do mapa
     * @return o tipo parametrizado Map<keyType, valueType>
     */
    public static Type mapOf(Class<?> keyType, Class<?> valueType) {
        return new ParameterizedTypeImpl(Map.class, new Type[] { keyType, valueType });
    }

    /**
     * Implementação de ParameterizedType para uso com o ObjectMapper.
     * Permite criar tipos genéricos para desserialização.
     */
    private static class ParameterizedTypeImpl implements ParameterizedType {
        private final Type rawType;
        private final Type[] actualTypeArguments;

        public ParameterizedTypeImpl(Type rawType, Type[] actualTypeArguments) {
            this.rawType = rawType;
            this.actualTypeArguments = actualTypeArguments;
        }

        @Override
        public Type[] getActualTypeArguments() {
            return actualTypeArguments;
        }

        @Override
        public Type getRawType() {
            return rawType;
        }

        @Override
        public Type getOwnerType() {
            return null;
        }
    }
}
