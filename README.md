# AI-Java-LangChain4j

A Java microservice that implements an AI-powered support assistant using LangChain4j and Google's Gemini AI model, deployed on Google Cloud Platform (GCP).

## Overview

This project demonstrates how to integrate AI capabilities into a Java microservice using LangChain4j framework with Google's Gemini AI model. It provides a REST API endpoint for an AI-powered support assistant that maintains conversation history per user using Redis caching.

## What It Does

- Provides an AI-powered support assistant that responds to user queries
- Uses Google Gemini AI via LangChain4j framework
- Maintains conversation history per user using Redis caching
- Exposes a simple REST API endpoint for asking questions
- Designed for cloud-native deployment on Google Cloud Platform (GCP)

## Tech Stack

- **Language**: Java (100%)
- **Framework**: Spring Boot
- **AI Framework**: LangChain4j
- **AI Model**: Google Gemini
- **Cache**: Redis
- **Build Tool**: Gradle
- **Cloud Platform**: Google Cloud Platform (GCP)

## Architecture

- **Controller**: `AIController` - Handles REST requests
- **Service**: `SupportAssistant` - Manages AI interactions with Gemini
- **Cache**: Redis - Stores conversation context per user
- **Config**: Spring Boot with YAML configuration

## REST Endpoint

### Endpoint Details

- **Base URL**: `http://localhost:8080/api/ai`
- **Path**: `/ask`
- **Method**: `GET`
- **Parameters**:
  - `userId` (required): Unique identifier for the user
  - `message` (required): The question/message to send to the AI

### CURL Examples

**Basic example:**
```bash
curl -X GET "http://localhost:8080/api/ai/ask?userId=user123&message=What%20is%20Java%3F"
```

**More readable with URL encoding:**
```bash
curl -X GET 'http://localhost:8080/api/ai/ask' \
  -G \
  --data-urlencode 'userId=user123' \
  --data-urlencode 'message=What is Java?'
```

**With a complex question:**
```bash
curl -X GET 'http://localhost:8080/api/ai/ask' \
  -G \
  --data-urlencode 'userId=customer_001' \
  --data-urlencode 'message=How do I implement authentication in a Spring Boot microservice?'
```

**Using jq to format the response:**
```bash
curl -X GET 'http://localhost:8080/api/ai/ask' \
  -G \
  --data-urlencode 'userId=support_user' \
  --data-urlencode 'message=Tell me about microservices' | jq .
```

## Setup & Prerequisites

Before running the application:

### 1. Set the Gemini API Key

```bash
export GEMINI_API_KEY=your_actual_gemini_api_key
```

### 2. Start Redis

Redis is required for session management and conversation history:

```bash
# Using Docker
docker run -d -p 6379:6379 redis:latest

# Or if Redis is already installed locally
redis-server
```

### 3. Build and Run the Application

```bash
# Build the project
./gradlew clean build

# Run the Spring Boot application
./gradlew bootRun
```

### 4. Test the Endpoint

```bash
curl -X GET 'http://localhost:8080/api/ai/ask' \
  -G \
  --data-urlencode 'userId=test_user' \
  --data-urlencode 'message=Hello, how are you?'
```

## Key Features

✅ User-specific conversation context (stored in Redis)
✅ Powered by Google Gemini AI
✅ Simple GET-based REST API
✅ Spring Boot microservice architecture
✅ GCP-ready deployment
✅ Gradle build system

## Project Structure

```
AI-Java-LangChain4j/
├── src/
│   ├── main/
│   │   ├── java/com/chandra/ai/AI_Java_LangChain4j/
│   │   │   ├── controller/AIController.java
│   │   │   ├── service/SupportAssistant.java
│   │   │   └── AiJavaLangChain4jApplication.java
│   │   └── resources/application.yaml
│   └── test/
├── build.gradle
├── settings.gradle
├── gradlew
└── README.md
```

## Configuration

The application uses Spring Boot with YAML configuration. Key settings in `application.yaml`:

```yaml
spring:
  application:
    name: AI-Java-LangChain4j
  data:
    redis:
      host: localhost
      port: 6379

langchain4j:
  google-ai:
    gemini:
      api-key: ${GEMINI_API_KEY}
```

## Use Cases

- **Customer Support**: Automate customer service queries with AI-powered responses
- **Knowledge Assistant**: Build intelligent Q&A systems
- **Internal Help Desk**: Deploy as an internal support assistant
- **Educational Tool**: Learn how to integrate LLMs in Java microservices

## Getting Started with Google Gemini API

1. Visit [Google AI Studio](https://aistudio.google.com/app/apikey)
2. Create a new API key
3. Set it as an environment variable: `export GEMINI_API_KEY=your_key`

## Contributing

Feel free to fork this repository and submit pull requests for any improvements.

## License

This project is open source and available under the MIT License.