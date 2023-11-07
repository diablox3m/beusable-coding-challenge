# Beusable Coding Challenge
Demo project for Coding challenge

#### Prerequisites to run
- Java JRE (17 or latest compatible)

#### Prerequisites to compile
- Java JDK 17

#### Build & Run

1. Run Gradle build and tests
    ```bash
   ./gradlew build
   ```
2. Run service
    ```bash
    ./gradlew bootRun
    ```
   You can now access Swagger UI at http://localhost:8080/swagger-ui/index.html

#### Invoke Room Occupancy API

1. One way to invoke API is to use Swagger UI on the link provided above.
2. Other way is to use cURL command
    ```bash
    curl --location 'http://localhost:8080/api/rooms/occupancy?premium=3&economy=3'
    ```
   and response should look like:
    ```json
    {
      "premium": {
        "roomsOccupied": 3,
        "price": 738.0
      },
      "economy": {
        "roomsOccupied": 3,
        "price": 167.99
      }
    }
    ```