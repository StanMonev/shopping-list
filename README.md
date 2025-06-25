# Shopping‑List

A lightweight Spring Boot CRUD app for managing your personal shopping list.

---

## Prerequisites

* **Java 21**
* **Gradle 8+** (wrapper included)
* **PostgreSQK 16+**
* **Docker Desktop** (optional, for running everything in containers)

---

## How to run

### 1 · Prepare configuration

1. Copy **`.env.example` → `.env`** and adjust the variables to your liking.
2. Copy **`application-example.properties` → `application-local.properties`** and change the variable values to your specific enviroment.

### 2 · Run with Gradle (local profile)

```bash
./gradlew bootRun --args='--spring.profiles.active=local'
```

### 3 · Run with Docker Compose

```bash
docker compose up --build
```

That’s it—open your browser on http://localhost:8080 . Make sure to log in with your credentials that you added in *application-local.properties*:
`spring.security.user.name` and `spring.security.user.password`. After logging in you can start adding new products to the shopping list!

---

## Tests

```bash
./gradlew test
```

Tests are available for the following classes:
* **ShoppingListServiceImpl** - Testing the service functionality
* **ProductController** - Testing the HTTP status / JSON / basic-auth
* **ViewController** - Testing Thymeleaf pages, form flow, security

---
## Build & Dependencies
Libraries used in this app (controlled by Spring Boot 3.5.3):
* **lombok** – Easier model class representation
* **spring-boot-starter-web** – REST endpoints
* **spring-boot-starter-validation** – Form validation
* **spring-boot-starter-data-jpa** – Persistence
* **spring-boot-starter-security** – Form & basic auth
* **spring-boot-starter-thymeleaf** – HTML views
* **spring-boot-devtools** – Hot-reload (dev only)
* **org.postgresql:postgresql** – JDBC driver
