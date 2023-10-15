# Java Api Template ☕
This is a template for a Java API, using Spring Boot, Spring Data JPA, Spring Security, and JWT.

The API is a simple CRUD for a User entity. :bust_in_silhouette:

## Next Steps 🚀
- [ ] Add unit tests in the service layer :white_check_mark:
- [ ] Integrate Spring Security and JWT authentication :lock:
- [ ] Implement user roles :busts_in_silhouette:
- [ ] Implement ACL :key:
- [ ] Add integration tests :gear:
- [ ] Add Dockerfile :whale:
- [ ] Add Swagger documentation :page_with_curl:

## Requirements 📋
- Java 17 :coffee:
- Maven :wrench:
- Docker :whale:
- Postgres :elephant:

## Setup 🔨
```make install``` or
```mvn clean install```

### Database Setup 💾
#### Create Database Container 🐋
This API uses a postgres database. To set up the database, run the following command:

```make create_db container=<container> user=<user> password=<pwd> database=<db_name>```

    Where:
    - container:    the name of the docker container. If not provided, the default value is 'postgres'
    - user:         the user to create. If not provided, the default value is 'user'
    - password:     the password for the user. If not provided, the default value is '123123123'
    - database:     the name of the database to create. If not provided, the default value is 'java_api_template'
#### Run Database Container 🏹
If you want to run or re-run the database in a different container, you can use the following command:

```make run_db container=<container>```

    Where:
    - container:    the name of the docker container. If not provided, the default value is 'postgres'
#### Stop Database Container 🛑
If you want to stop the database container, you can use the following command:

```make stop_db container=<container>```

    Where:
    - container:    the name of the docker container. If not provided, the default value is 'postgres'

Tests will run using an in-memory HSQL database, so there is no need to create a database for tests.

## Run 🏃
```make run``` or
```mvn spring-boot:run -pl webapp```

⚠️ `make run` will run the database container if it is not running.

## Test ✅
```make test``` or
```mvn test```

If you consider it, make some changes :pencil:



