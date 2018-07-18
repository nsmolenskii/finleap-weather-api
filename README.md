# FinLeap java case study

## Requirements
You need to create an API service using Java which will retrieve the average forecast weather
metrics of a specific city. The API should expose a “/data” endpoint to retrieve the averages.
The /data endpoint should return a JSON object with the average of the following metrics:

  * Average of daily (6h-18h) temperature, in Celsius, for the following 3 days
  * Average of nightly (18h-6h) temperature, in Celsius, for the following 3 days
  * Average of pressure for the following 3 days

The /data endpoint needs a CITY parameter containing the city name as the input for the correct
response.
Use Open Weather Map to get the data (https://openweathermap.org free account for forecast
data)
Make sure you use a full REST API convention and that you return the correct error codes when
necessary.

At the root of the project there must be a README file describing the process to run and test
the service and any challenges/decisions made during the process of developing this case
study.

Bonus points:
  * Validate input - watch out for any injection tries that an API user might insert
  * API usage docs (Swagger, or whatever is preferred)
  * Unit tests
  * Integration tests
  * Caching
  
## Prerequisites
  * [Oracle JDK](http://www.oracle.com/technetwork/java/javase/overview/index.html)
  * [Apache Maven](https://maven.apache.org)
  * IDE with configured Lombok plugin: [IDEA](https://projectlombok.org/setup/intellij) or [Eclipse](https://projectlombok.org/setup/eclipse)  
  * IDE with configured Annotation Processing: [IDEA and Eclipse](https://immutables.github.io/apt.html)
    
## Running 

Maven wrapper is configured in project. So you can use local maven installation as well as wrapped version .
All command provided in form of local installation
* unit tests
    ```bash
    mvn clean test
    ```
* integration tests
    ```bash
    mvn clean verify -DskipTests
    ```
* unit and integration tests
    ```bash
    mvn clean verify
    ```
* run
    ```bash
    mvn spring-boot:run
    ```
    
## Documentation 
Auto-generated documentation in  [Swagger-UI](http://localhost:8080/swagger-ui.html) available on running server. 