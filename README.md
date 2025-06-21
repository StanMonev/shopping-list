# Shopping‑List

A lightweight Spring Boot CRUD app for managing your personal shopping list.

---

## Prerequisites

* **Java 21**
* **Gradle 8+** (wrapper included)
* **Docker Desktop** (optional, for running everything in containers)

---

## How to run

### 1 · Prepare configuration

1. Copy **`.env.example` → `.env`** and adjust the variables to your liking.
2. Copy **`application-example.properties` → `application-local.properties`** if you plan to run the app directly from Gradle.

### 2 · Run with Gradle (local profile)

```bash
./gradlew bootRun --args='--spring.profiles.active=local'
```

### 3 · Run with Docker Compose

```bash
docker compose up --build
```

That’s it—open your browser and start adding items to the list.

---

## Tests

```bash
./gradlew test
```
