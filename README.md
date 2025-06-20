# Shopping List

A lightweight web application that lets you **view, add, edit and delete** items on a personal shopping list.

## Prerequisites

* **Java 21**
* **Gradle 8+** (wrapper included)
* **Docker** *(optional – for containerised run)*


## Getting Started

### 1. Run locally with Gradle

```bash
# Clone the repo
$ git clone https://github.com/StanMonev/shopping-list.git
$ cd shopping‑list

# Launch the application
$ ./gradlew bootRun
```

The server starts on **[http://localhost:8080](http://localhost:8080)**. Open your browser to manage your list.

### 2. Run in Docker

```bash
# Build the image
$ docker build -t shopping-list .

# Start a container (maps port 8080)
$ docker run --rm -p 8080:8080 shopping-list
```

Stop the container with **Ctrl‑C**.


## Running Tests

```bash
./gradlew test
```

Automated tests cover the main features to make sure everything works as expected.
