# AI Budget Back-end Project

## Skills
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
  - Monitoring: Spring Actuator + Prometheus + Grafana, Sentry
  - API Docs: Spring REST Docs
- DBMS: PostgreSQL(with pgvector)
- Server Hosting: Render(https://render.com/)

## Features
- gateway-server
  - API Gateway & Load Balancing
  - API Docs integration
- eureka-server
  - Service Registry & Discovery
- auth-server:
  - Authentication & Authorization
- budget-server:
  - Serving CRUD for budget transactions
  - Open banking integration for synchronizing card payment history
- ai-server:
  - LLM OpenAPI integration for AI based investment recommendation

## System Architecture
<img width="1000" height="600" alt="image" src="https://github.com/user-attachments/assets/fb2cc60e-8b4d-46da-9f06-185415f16fa4" />

## Getting Started to dev
- How to run
  - Edit configurations
    ```
    Active Profiles:
    - gateway-server: local
    - eureka-server: local
    - budget-server: local
    Environment Variables:
    - gateway-server: GATEWAY_SECRET={gatewaySecret}
    - eureka-server: GATEWAY_SECRET={gatewaySecret}
    - budget-server: GATEWAY_SECRET={gatewaySecret};DB_USERNAME={dbUsername};DB_PASSWORD={dbPassword};
    ```
  - Run
    ```
    Tip) View → Tool Windows → Services → Run (every servers start)
    ```
- Logs
  ```
  ./logs/{microserviceProjectName}/{date}/*
  ```
- Montitor
  ```
  cd docker
  docker-compose up -d
  http://localhost:3001/dashboards
  ```
- API Docs
  ```
  http://localhost:8000/apidocs/index.thml
  ``` 
