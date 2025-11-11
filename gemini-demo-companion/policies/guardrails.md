# Enterprise Guardrails for Gemini Code Assist Usage

This document outlines the mandatory guardrails and policies that must be adhered to when using Gemini Code Assist (GCA) within the enterprise. These policies ensure security, compliance, code quality, and consistency across all development efforts.

## 1. Naming Conventions:
*   **Repositories:** All new repositories must follow the pattern `[project-prefix]-[service-name]-[type]`, e.g., `delivery-tracking-api-service`.
*   **Branches:** Feature branches must be named `feature/[JIRA-ID]-[short-description]`.
*   **Commits:** Commit messages must follow Conventional Commits specification (e.g., `feat: add new delivery tracking endpoint`).
*   **Code Elements:**
    *   Java classes: PascalCase.
    *   Java methods: camelCase.
    *   Variables: camelCase.
    *   Constants: SCREAMING_SNAKE_CASE.

## 2. Secrets Management:
*   **No Hardcoded Secrets:** Absolutely no API keys, database credentials, or other sensitive information should be hardcoded in source code.
*   **GCP Secret Manager:** All secrets for GCP deployments must be stored and accessed via GCP Secret Manager.
*   **Local Development:** For local development, use environment variables or secure configuration files (e.g., `.env`, `application-local.yaml` with `.gitignore`).

## 3. Code Quality & Standards:
*   **Static Analysis:** All code must pass checks from SonarQube (or equivalent static analysis tool) with a quality gate of 0 new bugs, 0 new vulnerabilities, and 0 new code smells.
*   **Unit Test Coverage:** New code must maintain or increase the overall unit test coverage, with a minimum of 80% coverage for new or modified lines.
*   **Clean Architecture:** Adherence to the established clean architecture principles for new microservices.
*   **API Design:** RESTful API design principles must be followed, including proper HTTP methods, status codes, and clear resource naming.

## 4. Security:
*   **SAST/DAST:** All applications must undergo Static Application Security Testing (SAST) and Dynamic Application Security Testing (DAST) as part of the CI/CD pipeline.
*   **Dependency Scanning:** Automated dependency vulnerability scanning must be enabled and critical vulnerabilities addressed promptly.
*   **Least Privilege:** Service accounts and IAM roles must be configured with the principle of least privilege.

## 5. Integration Standards:
*   **API Gateway:** All external-facing APIs must be exposed through an API Gateway.
*   **Logging & Monitoring:** Standardized logging formats and integration with centralized logging (e.g., Cloud Logging) and monitoring (e.g., Cloud Monitoring, Prometheus/Grafana) solutions.
*   **Message Queues:** Use Google Cloud Pub/Sub for asynchronous communication between microservices.

## 6. Documentation:
*   **README.md:** Every repository must have a comprehensive `README.md` including setup instructions, API documentation, and architecture overview.
*   **OpenAPI/Swagger:** All REST APIs must be documented using OpenAPI/Swagger specifications.
