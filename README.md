# AI Budget Back-end Project

## Developed with ..
- Language: Kotlin
- Design Pattern: Ports and Adapters(Hexagonal)
- Framework: Spring Boot
  - API Gateway&Load Balancer: Spring Cloud Gateway
  - Service Registry&Discovery: Spring Cloud Eureka
  - Microservices: Spring Web/Webflux
    - Security: Spring Security
    - AI: Spring AI
    - Data interface: Spring Data JPA/Redis
    - Service Interface: Spring Cloud OpenFeign
  - Logging: Logback
  - Monitoring: Spring Actuator + Prometheus + Grafana
  - API Docs: Spring REST Docs
- DBMS: PostgreSQL(with pgvector)

## Features
- budget-server:
  - Serving CRUD for budget transactions
  - Bank OpenAPI integration for synchronizing remote finance accounts budget transactions
- ai-server:
  - LLM OpenAPI integration for AI based investment recommendation
- auth-server:
  - Authentication/Authorization
- erueka-server
  - Service Registry & Discovery
- gateway-server
  - API Gateway & Load Balancing

## Architecture
<img width="1000" height="600" alt="image" src="https://github.com/user-attachments/assets/fb2cc60e-8b4d-46da-9f06-185415f16fa4" />

## Getting Started
- 작성중
  ```
  작성중
  ```
