# Catalog Service

Catalog Service is a reactive microservice responsible for managing marketplace listings.  
The service is built using **hexagonal architecture**, separating domain logic, application use cases, and infrastructure adapters.

## Tech Stack

- Java 21
- Spring Boot WebFlux (Reactive)
- Project Reactor (Reactive programming)
- PostgreSQL database
- Liquibase
- MapStruct
- Docker-ready environment

## Architecture

The service follows **Hexagonal Architecture (Ports & Adapters)**:

- **Domain layer** – business models and rules
- **Application layer** – use cases (commands and queries)
- **Adapters layer** – REST controllers, persistence adapters, mappers
- Reactive persistence using R2DBC

This structure allows clear separation of concerns, easier testing, and infrastructure independence.

## API Endpoints

### Create listing
- POST /listings

### Get listings
- GET /listings
- Optional filters: category, tags

### Get listing detail
- GET /listings/{id}

### Update listing
- PATCH /listings/{id}

### Search listings
- GET /listings/search?query=...

## Running locally
```bash
mvn clean package -DSkipTets
```

```bash
docker compose up
```



