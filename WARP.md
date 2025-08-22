# WARP.md

This file provides guidance to WARP (warp.dev) when working with code in this repository.

Project: Spring Boot 3 (Java 21) REST API with PostgreSQL, Flyway, Spring Security (JWT), SpringDoc OpenAPI.

- App base path: /api
- Primary module: Maven (pom.xml)
- DB: PostgreSQL, migrations via Flyway (src/main/resources/db/migration)

Common commands (PowerShell-compatible)
- Build (with tests): mvn clean package
- Build (skip tests): mvn -DskipTests clean package
- Run (dev, hot reload via devtools): mvn spring-boot:run
- Run packaged JAR: java -jar target/foro-hub-0.0.1-SNAPSHOT.jar
- Run tests (all): mvn test
- Run a single test class: mvn -Dtest=FullyQualifiedClassName test
- Run a single test method: mvn -Dtest=FullyQualifiedClassName#methodName test

Run with environment variables
- JWT secret (required for auth):
  - PowerShell (current session): $env:JWT_SECRET = "your-32-char-secret"
  - Then run: mvn spring-boot:run
- Optional: override DB via Spring Boot envs if not using application.properties
  - $env:SPRING_DATASOURCE_URL = "jdbc:postgresql://localhost:5432/alura_foro_hub"
  - $env:SPRING_DATASOURCE_USERNAME = "postgres"
  - $env:SPRING_DATASOURCE_PASSWORD = "<password>"

API documentation
- Swagger UI: http://localhost:8080/api/swagger-ui.html
- OpenAPI JSON: http://localhost:8080/api/v3/api-docs

High-level architecture
- Web/API layer (controllers)
  - REST controllers under com.jhampier.forohub.controllers expose endpoints like /auth/login, /topicos, /usuarios. Base path /api from server.servlet.context-path.
- Domain layer (entities + DTOs)
  - Packages under com.jhampier.forohub.domain: curso, perfil, respuesta, topico, usuario represent core forum concepts and data contracts.
- Persistence layer (JPA)
  - Spring Data JPA repositories for domain aggregates; PostgreSQL dialect configured. Flyway manages schema and seed data.
- Security (JWT)
  - Spring Security with stateless JWT auth using com.auth0:java-jwt. Secret is read from env (api.security.token.secret via JWT_SECRET). Expect an authentication controller for issuing tokens and a filter to validate them on protected endpoints. Roles (ADMIN, USER) via perfiles and usuarios_perfiles relationship.
- Infrastructure
  - infra.exceptions: centralized exception handling.
  - infra.springdoc: OpenAPI/Swagger configuration.
  - configuration: CORS and other app-level configs.
- Application entrypoint
  - Application.java boots the Spring context.

Database and migrations
- Migrations auto-run on startup (Flyway):
  - V1__create-db.sql: creates perfiles, usuarios, usuarios_perfiles (N:M), cursos, topicos, respuestas.
  - V2__insert-default-data.sql: seeds roles ADMIN/USER, admin user (admin@forohub.com, BCrypt password), and assigns ADMIN.
- Default JPA settings:
  - spring.jpa.hibernate.ddl-auto=update (DDL updates in dev); Flyway is source of truth for schema.

Local development defaults (application.properties)
- Context path: /api
- DB: jdbc:postgresql://localhost:5432/alura_foro_hub (username=postgres; set your password)
- JWT secret: api.security.token.secret uses env JWT_SECRET with a default fallback; in non-prod, you can rely on the default but prefer setting JWT_SECRET.

Notes on Docker/CI
- Dockerfile present is a Jenkins LTS image with Docker and Maven installed for CI environments; it is not a runtime container for this Spring Boot app.

Smoke checks
- Verify DB connection before running:
  - psql -h localhost -U postgres -d alura_foro_hub -c "\dt" (or use your DB tool)
- On startup, ensure Flyway logs show successful migration of V1/V2.
- After boot, hit /api/swagger-ui.html to confirm API is up and discoverable.

