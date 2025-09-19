# 4. Instalação e Inicialização

Este guia descreve os passos necessários para configurar o ambiente de desenvolvimento e executar a aplicação **Biblioteca A7** localmente.

## 4.1. Requisitos de Sistema

Antes de começar, certifique-se de que você tem os seguintes softwares instalados e configurados em seu sistema:

- **Java Development Kit (JDK):** Versão 17 ou superior.
- **Apache Maven:** Versão 3.8 ou superior, para gerenciamento de dependências e build do projeto.
- **PostgreSQL:** Um servidor de banco de dados PostgreSQL ativo.
- **WildFly:** Servidor de Aplicação para fazer o deploy do servidor. A versão utilizada durante o desenvolvimento foi a compatível com Jakarta EE 10.
- **Git:** Para clonar o repositório.

## 4.2. Passo a Passo para Instalação

1.  **Clonar o Repositório:**
    ```bash
    git clone <URL_DO_REPOSITORIO>
    cd biblioteca-a7
    ```

2.  **Configurar o Banco de Dados:**
    - Crie um banco de dados no PostgreSQL para a aplicação (ex: `biblioteca_db`).
    - Anote o nome do banco, usuário e senha.

3.  **Configurar o Datasource no WildFly:**
    - A aplicação precisa de um datasource configurado no WildFly para se conectar ao PostgreSQL.
    - No diretório `src/main/webapp/WEB-INF`, há um arquivo `biblioteca-ds.xml` de exemplo. Você precisará configurá-lo no seu servidor WildFly, ajustando a URL de conexão, usuário e senha para o seu ambiente.
    - O JNDI name esperado pela aplicação é `java:jboss/datasources/BibliotecaDS`, conforme definido no `persistence.xml`.

4.  **Compilar o Projeto:**
    - Execute o Maven para baixar as dependências e compilar o código-fonte.
    ```bash
    mvn clean install
    ```
    - Isso irá gerar o arquivo `biblioteca-a7.war` no diretório `target/`.

## 4.3. Passo a Passo para Inicialização

### 4.3.1. Servidor (API REST)

1.  **Iniciar o WildFly:** Inicie seu servidor WildFly.

2.  **Fazer o Deploy:** Você pode fazer o deploy da aplicação de duas formas:
    - **Manualmente:** Copie o arquivo `target/biblioteca-a7.war` para o diretório `standalone/deployments` do seu WildFly.
    - **Via Plugin Maven:** Utilize o plugin do WildFly configurado no `pom.xml`.
      ```bash
      mvn wildfly:deploy
      ```

3.  **Verificar a API:** Após o deploy, a API estará disponível no endereço base do seu servidor. Por padrão, será algo como `http://localhost:8080/biblioteca-a7/api`.

### 4.3.2. Cliente (Aplicação Desktop Swing)

1.  **Configurar a URL do Servidor:** A aplicação cliente precisa saber onde o servidor está rodando. Essa configuração está na classe `com.biblioteca.presentation.util.ApiClient`. Verifique se a URL base (`http://localhost:8080/biblioteca-a7`) está correta para o seu ambiente.

2.  **Executar a Aplicação:**
    - A classe principal da aplicação desktop é `com.biblioteca.presentation.TelaPrincipal`.
    - Você pode executá-la diretamente da sua IDE (Eclipse, IntelliJ, etc.) clicando com o botão direito sobre o arquivo e selecionando "Run As Java Application".

Após esses passos, a aplicação cliente será iniciada e você poderá interagir com o sistema, que fará as chamadas para o servidor rodando no WildFly.