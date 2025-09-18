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

public class ApiClient {

    private static final String BASE_URL = "http://localhost:8080/biblioteca-a7/api";
    private final HttpClient client;
    private final ObjectMapper objectMapper;


    public ApiClient() {
        this.client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();

        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    public <T> T get(String path, Type responseType) throws IOException, InterruptedException {
        HttpRequest request = buildGetRequest(path);
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        checkResponse(response);
        JavaType javaType = objectMapper.getTypeFactory().constructType(responseType);
        return objectMapper.readValue(response.body(), javaType);
    }

    public <T> T post(String path, Object body, Type responseType) throws IOException, InterruptedException {
        String requestBody = objectMapper.writeValueAsString(body);
        HttpRequest request = buildPostRequest(path, requestBody);
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        checkResponse(response);
        JavaType javaType = objectMapper.getTypeFactory().constructType(responseType);
        return objectMapper.readValue(response.body(), javaType);
    }

    public <T> T put(String path, Object body, Type responseType) throws IOException, InterruptedException {
        String requestBody = objectMapper.writeValueAsString(body);
        HttpRequest request = buildPutRequest(path, requestBody);
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        checkResponse(response);
        JavaType javaType = objectMapper.getTypeFactory().constructType(responseType);
        return objectMapper.readValue(response.body(), javaType);
    }

    public void delete(String path) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + path))
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        checkResponse(response);
    }

    public void postMultipart(String path, String fieldName, File file) throws IOException, InterruptedException {
        String boundary = "---SuaBibliotecaBoundary" + UUID.randomUUID().toString();
        HttpRequest request = buildMultipartRequest(path, fieldName, file, boundary);
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        checkResponse(response);
    }

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

    private HttpRequest buildGetRequest(String path) {
        return HttpRequest.newBuilder().uri(URI.create(BASE_URL + path)).GET().build();
    }

    private HttpRequest buildPostRequest(String path, String body) {
        return HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + path))
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .header("Content-Type", "application/json")
                .build();
    }

    private HttpRequest buildPutRequest(String path, String body) {
        return HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + path))
                .PUT(HttpRequest.BodyPublishers.ofString(body))
                .header("Content-Type", "application/json")
                .build();
    }

    private void checkResponse(HttpResponse<String> response) throws IOException {
        if (response.statusCode() < 200 || response.statusCode() >= 300) {
            throw new IOException("Erro na requisição: " + response.statusCode() + " - " + response.body());
        }
    }

    public static Type listOf(Class<?> type) {
        return new ParameterizedTypeImpl(List.class, new Type[] { type });
    }

    public static Type mapOf(Class<?> keyType, Class<?> valueType) {
        return new ParameterizedTypeImpl(Map.class, new Type[] { keyType, valueType });
    }

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
