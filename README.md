# Java Api Template

## Setup
```mvn clean install spring-boot:repackage```

### Database Setup
This API uses a postgres database. To setup the database, run the following command:

```docker run --name postgres -e POSTGRES_PASSWORD=123123123 -e POSTGRES_USER=user -p 5432:5432 -d postgres```

Inside the database (`psql -U user`), create a database called ```java_api_template```, using the following command:

```CREATE DATABASE "java_api_template" WITH ENCODING "UTF-8";```

## Run
```mvn spring-boot:run -pl webapp```



