# openai-spring-demo

A minimal Spring Boot demo project showcasing integrations with OpenAI capabilities.

Features
- Simple REST controllers and web pages demonstrating:
	- Text generation and prompts
	- Embeddings and similarity search
	- Image generation and analysis
	- Speech-to-text and text-to-speech
	- Content moderation
	- Retrieval-Augmented Generation (RAG) example

Getting started

Prerequisites
- JDK 17 or later
- Maven 3.6+

Run locally

```bash
mvn spring-boot:run
```

Build

```bash
mvn clean package
```

Configuration
- Edit `src/main/resources/application.properties` to set API keys and other settings.

Notes
- This project is intended as a learning/demo app. Do not use in production without reviewing security and configuration.

License
- MIT-style or adapt to your preferred license.
