
---

## 🚀 Features Overview

| Feature                     | Description                                                       |
|-----------------------------|-------------------------------------------------------------------|
| 🧠 Clean Architecture       |                                                                   |
| 🔐 HTTPS with NGINX         | Secured via reverse proxy on port `8443`                          |
| 💾 PostgreSQL               |                                                                   |
| 💣 Optimistic Lock Handling | Making app more flexible and responsive, provide versioning       |
| 🔁 Retry Scheduler          | Scheduled task retries failed transactions every 10 seconds       |
| 📡 Actuator                 | Exposes `/actuator/health`, `/actuator/info` with DB, disk checks |
| 🔍 AOP Request Logging      | Logs all incoming requests with sensitive data masking            |
| 🧱 Logback Logging          | Logs to console + `logs/app.log`, masks `amount`, `balance`, etc. |
| 📏 DTO Validation           | Uses `@Valid` and `jakarta.validation` with global error handling |
| 💥 Exception Handling       | Central `@ControllerAdvice` logs and returns structured error responses |
| 🔒 Thread-Safe Transactions | optimistic lock implemented, locked transaction scheduled for retry |
| 🧪 Unit Tests               | Covers `CustomerService` and `TransactionService` using JUnit + Mockito |
| 📡 Distributed Tracking     | Micrometer added, logback configured to store thread id, trace id and span id |
| 🔁 Idempotency Support      | For transaction operations simple idempotency check implemented   |
---

## 🧪 Example APIs

| Operation         | Endpoint                             | Description                      |
|------------------|--------------------------------------|----------------------------------|
| Create Customer  | `POST /api/v1/customers`             | Creates customer (balance = 100) |
| Top-up           | `POST /api/v1/transactions/topup`    | Adds balance                     |
| Purchase         | `POST /api/v1/transactions/purchase` | Deducts from balance             |
| Refund           | `POST /api/v1/transactions/refund`   | Refunds given transaction        |

---

## ⚙️ Tech Stack

- Java 17
- Spring Boot 3.x
- Spring Data JPA (PostgreSQL)
- Hibernate Optimistic Locking
- Jakarta Validation
- Lombok
- Logback
- Micrometer
- Docker & Docker Compose
- NGINX (HTTPS)
- Postman (API testing)
- JUnit 5 + Mockito (unit testing)

---

## 🐳 Run Locally (Docker)

```bash
docker-compose up --build
 ```

**Postman collection added to solution**