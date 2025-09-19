# 10. Deployment e Ambiente

Esta seção fornece diretrizes sobre como configurar os ambientes e realizar o deploy da aplicação **Biblioteca A7**.

## 10.1. Configuração de Ambientes

É uma boa prática manter ambientes separados para desenvolvimento, homologação (staging) e produção. A principal diferença entre eles reside na configuração do banco de dados e, potencialmente, em outras configurações externas.

- **Desenvolvimento (Development):**
  - **Banco de Dados:** Geralmente um banco de dados local na máquina do desenvolvedor (PostgreSQL) ou até mesmo um banco em memória como o H2 para testes rápidos.
  - **Servidor de Aplicação:** WildFly rodando localmente.
  - **Configuração:** As configurações de datasource (`biblioteca-ds.xml`) e persistência (`persistence.xml`) apontam para o banco de dados local.

- **Homologação (Staging/QA):**
  - **Banco de Dados:** Um servidor PostgreSQL dedicado, compartilhado pela equipe de desenvolvimento e QA. O banco de dados deve ser uma réplica fiel (em termos de estrutura) do ambiente de produção.
  - **Servidor de Aplicação:** Uma instância do WildFly em um servidor dedicado para testes.
  - **Objetivo:** Validar as novas funcionalidades de forma integrada antes de enviá-las para produção.

- **Produção (Production):**
  - **Banco de Dados:** Um servidor PostgreSQL robusto, com políticas de backup e alta disponibilidade.
  - **Servidor de Aplicação:** Uma ou mais instâncias do WildFly, possivelmente em um cluster para garantir escalabilidade e tolerância a falhas.
  - **Configuração:** O datasource deve ser configurado com credenciais seguras (evitar senhas hard-coded) e apontar para o banco de dados de produção. O nível de log deve ser ajustado para `INFO` ou `WARN` para evitar excesso de informações.

## 10.2. Como Fazer Deploy

O processo de deploy consiste em gerar o pacote da aplicação (`.war`) e publicá-lo no servidor de aplicação WildFly.

### Passo 1: Gerar o Pacote de Produção

Para gerar o build da aplicação, utilize o Maven. É recomendado usar um perfil de build específico para produção, caso exista, para aplicar configurações otimizadas.

```bash
# Limpa o projeto e gera o pacote .war
mvn clean package
```

Este comando irá gerar o arquivo `biblioteca-a7.war` no diretório `target/`.

### Passo 2: Deploy no WildFly

Existem várias maneiras de fazer o deploy no WildFly:

1.  **Deploy via Console de Administração (Web):**
    - Acesse a console de administração do WildFly (geralmente em `http://<seu-servidor>:9990`).
    - Navegue até a seção "Deployments".
    - Clique em "Add" e faça o upload do arquivo `biblioteca-a7.war` gerado no passo anterior.

2.  **Deploy via Linha de Comando (CLI):**
    - Utilize a ferramenta `jboss-cli.sh` (ou `jboss-cli.bat`) que acompanha o WildFly.
    - Conecte-se ao servidor e use o comando `deploy`:
      ```bash
      # Inicie o CLI
      ./jboss-cli.sh --connect

      # Execute o deploy
      deploy /caminho/para/seu/projeto/target/biblioteca-a7.war
      ```

3.  **Deploy via Filesystem (Hot Deploy):**
    - Simplesmente copie o arquivo `biblioteca-a7.war` para o diretório `standalone/deployments/` da sua instalação do WildFly.
    - O WildFly irá detectar o novo arquivo e fará o deploy automaticamente. Este método é mais comum em ambientes de desenvolvimento.

Após o deploy, a aplicação estará disponível no contexto `/biblioteca-a7` do seu servidor.