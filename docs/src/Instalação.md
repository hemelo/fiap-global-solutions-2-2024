# INSTALAÇÃO

## Pré-requisitos

Para instalar o projeto, é necessário ter instalado em sua máquina:

### Opcionais 
- [Git](https://git-scm.com/) - Necessário para clonar o repositório e compilá-lo, somente se não quisar baixar o arquivo zipado pré-compilado.
- [Maven](https://maven.apache.org/download.cgi) - Necessário para compilar o projeto.

### Obrigatórios

- [Java 22](https://www.oracle.com/java/technologies/javase-jdk22-downloads.html) - Necessário para executar o projeto.
- *[Database Oracle](https://www.oracle.com/database/technologies/) - Necessário para criar o banco de dados.

O banco de dados pode ser criado localmente ou em um servidor remoto. Caso deseje criar o banco de dados localmente, siga este tutorial [aqui](https://docs.oracle.com/en/database/oracle/oracle-database/21/ntdbi/index.html).

## Partindo para a Instalação Local

Para realizar a instalação do projeto, você pode realizar o download do arquivo zipado do projeto ou clonar o repositório.

### Realizando download

Você pode realizar o download de duas maneiras:

1. Clicando no botão verde "Code" e selecione a opção "Download ZIP". Logo após, descompacte o arquivo baixado.
2. Clonando o repositório com o comando:

```bash
git clone 
```

### Prosseguindo com a instalação

1. Acesse a pasta do projeto:

```bash
cd global-solutions-2-2024
```

#### Configurando o projeto


2. Com os dados de conexão do banco de dados Oracle, configure o arquivo `database.properties` localizado em `src/main/resources`:

```properties
database.username=
database.password=
database.url=
```

3. Com os dados de conexão do banco de dados Oracle, configure o arquivo `flyway.conf` localizado em `src/main/resources`:

```properties
flyway.user=
flyway.password=
flyway.url=
```

#### Instalando dependências

4. Execute o comando para baixar as dependências do projeto:

```bash
mvn dependency:resolve
```

#### Executando migrações e populando o banco de dados

5. Execute o comando para realizar as migrações do projeto:

```bash
mvn flyway:migrate
```

> Verifique se as migrações foram realizadas com sucesso

6. (OPCIONAL) Popule o banco de dados com o script `init.sql` localizado em `src/main/resources` para ter dados iniciais no banco de dados e 
facilitar o uso do sistema. 


#### Compilando o projeto

7. Execute o comando para compilar o projeto:

```bash
mvn clean install
```

> Verifique se o projeto foi compilado com sucesso


#### Executando o projeto

8. Execute o comando para executar o projeto:

```bash
mvn exec:java
```
