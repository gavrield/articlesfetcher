# Articles Fetcher Microservice

## Overview

The Articles Fetcher is a microservice designed to fetch and store articles from NewsAPI.org, which aggregates news from various sources. It is built using Java and utilizes Gradle as its build system. The service is containerized using Docker, making it easy to deploy and scale. Additionally, it uses the Gemini service for article summarization and MongoDB Atlas for data persistence.

## Directory Structure

- **app/**: Contains the main application code.
- **build/**: Output directory for build artifacts.
- **gradle/**: Gradle wrapper files.
- **src/**: Source code directory containing all Java classes and resources.
- **Dockerfile**: Defines the Docker image for the microservice.
- **docker-compose.yaml**: Configuration for Docker Compose to run the service and its dependencies.
- **build.gradle**: Gradle build script for managing dependencies and tasks.
- **settings.gradle**: Gradle settings file.

## Prerequisites

- Java Development Kit (JDK) 21 or later
- Docker
- Gradle

## Building and Running

1. **Build the project**:

   ```bash
   ./gradlew build
   ```

2. **Run the service using Docker Compose**:

   ```bash
   docker-compose up
   ```

## Environment Variables

The service uses a `.env` file to manage environment variables. Ensure this file is correctly configured before running the service.

## Endpoints

- **GET /fetch**: Fetches news articles by query and stores them in a database. Requires a `query` parameter.

## Contributing

Contributions are welcome! Please fork the repository and submit a pull request for any enhancements or bug fixes.

## License

This project is licensed under the MIT License.
