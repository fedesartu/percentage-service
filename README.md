# Percentage Service
## Technologies
- Java 21
- Spring Boot 3.2.4
- Spring Boot Webflux
- Postgres Database
- Redis Cache
- REST API

## Run Locally using Docker Image
1. Download docker-compose.yml file from repo
2. Run -> dokcer compose up

## Run Locally from Scratch
1. Clone repository
2. Inside project folder, run -> mvn clean install
3. Inside percentage-service-api folder, run -> docker build . -t fedesartu/percentage-service
4. Inside project folder, run -> dokcer compose up

## Useful Urls
- Swagger: http://localhost:8085/webjars/swagger-ui/index.html#/
- Database: http://localhost:8050/?pgsql=db&username=postgres&db=percentage&ns=logs
- External client mock: https://app.beeceptor.com/console/fedesartu5
- Docker Image: https://hub.docker.com/r/fedesartu/percentage-service

Just in case, docker-compose file is in the repository.
