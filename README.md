<div align="center">

# ⚡ GreenCharge — EV Charging Station Network

### A Microservices-based Capstone Project

*Book a slot. Plug in. Charge up. Pay seamlessly.*

![Java](https://img.shields.io/badge/Java-17-orange?style=for-the-badge&logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.5-brightgreen?style=for-the-badge&logo=springboot)
![H2](https://img.shields.io/badge/Database-H2-blue?style=for-the-badge&logo=h2)
![Microservices](https://img.shields.io/badge/Architecture-Microservices-purple?style=for-the-badge)
![License](https://img.shields.io/badge/License-MIT-lightgrey?style=for-the-badge)

</div>

---

## 🧠 Project Mindmap

```mermaid
mindmap
  root((GreenCharge<br/>EV Network))
    Charging Station Service
      CRUD Stations
      Connector Types
        CCS
        CHAdeMO
        Type-2
        GB/T
      Availability Check
    Booking Service
      Create Booking
      Confirm / Cancel
      Overlap Prevention
      RestTemplate → Station
    Charging Session Service
      Start Session
      Stop Session
      Track Energy kWh
      RestTemplate → Booking
    Payment Service
      Auto Billing
      Multiple Payment Modes
      Refunds
      RestTemplate → Session
    Cross-Cutting Concerns
      Global Exception Handling
      Bean Validation
      Slf4j Logging
      Layered Architecture
```

---

## 🏗️ System Architecture

```mermaid
flowchart LR
    Client([📱 Client / Postman]) --> GW[🚪 API Gateway]

    GW --> SS[⚡ Charging Station Service<br/>:8081]
    GW --> BS[📅 Booking Service<br/>:8082]
    GW --> CS[🔌 Charging Session Service<br/>:8083]
    GW --> PS[💳 Payment Service<br/>:8084]

    BS -.RestTemplate.-> SS
    CS -.RestTemplate.-> BS
    PS -.RestTemplate.-> CS

    SS --> DB1[(H2 · stationdb)]
    BS --> DB2[(H2 · bookingdb)]
    CS --> DB3[(H2 · sessiondb)]
    PS --> DB4[(H2 · paymentdb)]

    style GW fill:#6c5ce7,color:#fff
    style SS fill:#00b894,color:#fff
    style BS fill:#0984e3,color:#fff
    style CS fill:#e17055,color:#fff
    style PS fill:#fdcb6e,color:#000
```

---

## 🔄 End-to-End Flow

```mermaid
sequenceDiagram
    actor U as User
    participant Station as Station Service
    participant Booking as Booking Service
    participant Session as Session Service
    participant Payment as Payment Service

    U->>Station: POST /stations (register a station)
    U->>Booking: POST /bookings (reserve a connector)
    Booking->>Station: GET connector availability
    Station-->>Booking: ✅ available
    U->>Booking: PUT /confirm
    U->>Session: POST /sessions/start
    Session->>Booking: GET booking status
    Booking-->>Session: ✅ CONFIRMED
    U->>Session: PUT /stop (energyConsumedKwh)
    U->>Payment: POST /payments
    Payment->>Session: GET session status + energy
    Session-->>Payment: ✅ COMPLETED, 12.5 kWh
    Payment-->>U: 💰 Amount charged
```

---

## 📦 Microservices Overview

| # | Service | Port | Responsibility |
|---|---|:---:|---|
| 1 | ⚡ **Charging Station Service** | `8081` | Manage stations & connectors, CRUD, availability checks |
| 2 | 📅 **Booking Service** | `8082` | Reserve connectors, prevent double-booking, confirm/cancel |
| 3 | 🔌 **Charging Session Service** | `8083` | Start/stop a charging session, track energy consumed |
| 4 | 💳 **Payment Service** | `8084` | Bill the user based on energy consumed, process refunds |

Each service is fully independent — its own database, its own Maven build, its own port — and talks to the *previous* service in the chain via **RestTemplate**.

---

## 🛠️ Tech Stack

<div align="center">

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 3.2.5 |
| Persistence | Spring Data JPA + H2 (in-memory) |
| Validation | Jakarta Bean Validation |
| Logging | Slf4j + Lombok `@Slf4j` |
| Inter-service Communication | RestTemplate |
| Build Tool | Maven |
| API Testing | Postman |

</div>

---

## 🧩 Layered Architecture (per service)

```
📁 <service-name>
 ┣ 📁 controller     → REST endpoints, request/response only
 ┣ 📁 service         → Business logic (interface + impl)
 ┣ 📁 repository      → Spring Data JPA repositories
 ┣ 📁 entity          → JPA entities & enums
 ┣ 📁 dto             → Request/response DTOs + validation
 ┣ 📁 client          → RestTemplate wrappers to other services
 ┣ 📁 config          → Bean configuration (RestTemplate, etc.)
 ┗ 📁 exception       → Custom exceptions + @ControllerAdvice
```

Every method across every service starts with:
```java
log.info("I am inside the method: methodName");
```
so the console tells a clear story of the request as it flows through the layers.

---

## ✅ Features Implemented

- 🏗️ **Well-designed Repository pattern** across all 4 services
- 🔧 **Full CRUD operations** for stations, bookings, sessions, payments
- 🛡️ **Request validation** using Bean Validation (`@NotNull`, `@NotBlank`, `@Positive`, `@Future`, …)
- 🚨 **Centralized exception handling** via `@ControllerAdvice` — every error returns a consistent JSON shape
- 📝 **Structured logging** with `@Slf4j` on every class and method
- 🔗 **Inter-service communication** using RestTemplate (Booking → Station, Session → Booking, Payment → Session)
- ⛔ **Business rule enforcement**: no double-booking, no duplicate active sessions, no duplicate payments
- 💾 **H2 in-memory database** with web console for quick inspection

---

## 🚀 Getting Started

### Prerequisites
- Java 17+
- Maven 3.8+
- Postman (for testing)

### Run all services (in this order!)

```bash
# Terminal 1
cd ev-charging-station-service && mvn spring-boot:run   # :8081

# Terminal 2
cd booking-service && mvn spring-boot:run                 # :8082

# Terminal 3
cd session-service && mvn spring-boot:run                  # :8083

# Terminal 4
cd payment-service && mvn spring-boot:run                   # :8084
```

### H2 Console access

| Service | Console URL | JDBC URL |
|---|---|---|
| Station | `localhost:8081/h2-console` | `jdbc:h2:mem:stationdb` |
| Booking | `localhost:8082/h2-console` | `jdbc:h2:mem:bookingdb` |
| Session | `localhost:8083/h2-console` | `jdbc:h2:mem:sessiondb` |
| Payment | `localhost:8084/h2-console` | `jdbc:h2:mem:paymentdb` |

User: `sa` · Password: *(blank)*

---

## 📮 API Endpoints Summary

<details>
<summary><b>⚡ Charging Station Service (8081)</b></summary>

| Method | Endpoint | Description |
|---|---|---|
| POST | `/stations` | Create a station |
| GET | `/stations` | List all (optional `?city=`) |
| GET | `/stations/{id}` | Get by id |
| PUT | `/stations/{id}` | Update |
| DELETE | `/stations/{id}` | Delete |
| GET | `/stations/{id}/connectors/{cid}/available` | Check connector availability |

</details>

<details>
<summary><b>📅 Booking Service (8082)</b></summary>

| Method | Endpoint | Description |
|---|---|---|
| POST | `/bookings` | Create a booking |
| GET | `/bookings` | List all (optional `?userId=`) |
| GET | `/bookings/{id}` | Get by id |
| PUT | `/bookings/{id}/confirm` | Confirm booking |
| PUT | `/bookings/{id}/cancel` | Cancel booking |
| DELETE | `/bookings/{id}` | Delete |

</details>

<details>
<summary><b>🔌 Charging Session Service (8083)</b></summary>

| Method | Endpoint | Description |
|---|---|---|
| POST | `/sessions/start` | Start a session for a confirmed booking |
| PUT | `/sessions/{id}/stop` | Stop a session, record energy consumed |
| PUT | `/sessions/{id}/terminate` | Force-end a session |
| GET | `/sessions` | List all (optional `?userId=`) |
| GET | `/sessions/{id}` | Get by id |

</details>

<details>
<summary><b>💳 Payment Service (8084)</b></summary>

| Method | Endpoint | Description |
|---|---|---|
| POST | `/payments` | Charge for a completed session |
| PUT | `/payments/{id}/refund` | Refund a payment |
| GET | `/payments` | List all (optional `?userId=`) |
| GET | `/payments/{id}` | Get by id |

</details>

---

## 🧪 Testing with Postman

A ready-to-import **Postman Collection** is included, with multiple sample payloads for every endpoint (different stations, users, payment methods, etc.), plus a step-by-step guide covering the full flow from station creation to payment.

```
📄 ev-charging-network.postman_collection.json
📄 postman-testing-guide.md
```
## Output
### PostMan:
### Charging Station Service
<img width="1901" height="1017" alt="Screenshot 2026-07-22 092051" src="https://github.com/user-attachments/assets/f8a14608-4c46-4948-850e-f6623f7be5d1" />
<img width="1087" height="936" alt="image" src="https://github.com/user-attachments/assets/33e611c4-8127-45f0-8484-0946a9c46ce3" />

### Booking Service
<img width="1681" height="1020" alt="Screenshot 2026-07-22 092249" src="https://github.com/user-attachments/assets/fa53ec93-d9e7-4099-9aab-9650f9bd864b" />

<img width="1681" height="1020" alt="Screenshot 2026-07-22 092249" src="https://github.com/user-attachments/assets/f032a6f9-bdd2-4659-8d74-3c59006b3a7e" />

### Charging Session Service
<img width="1776" height="1008" alt="Screenshot 2026-07-22 092335" src="https://github.com/user-attachments/assets/394fe6ec-b504-4a55-a597-5cf140417686" />

### Payment Service
<img width="1806" height="1006" alt="Screenshot 2026-07-22 092347" src="https://github.com/user-attachments/assets/0305d8f2-89fe-4fa1-a768-469c9f5cc61a" />

### H2 Console
### Charging Station Service
<img width="1917" height="900" alt="Screenshot 2026-07-22 092438" src="https://github.com/user-attachments/assets/51048dc6-684a-48e1-b04b-733dfbe435fa" />

<img width="1916" height="950" alt="Screenshot 2026-07-22 092454" src="https://github.com/user-attachments/assets/2c29fd6f-4f46-46e0-bb12-4950431ae9e1" />

### Booking Service
<img width="1917" height="895" alt="Screenshot 2026-07-22 092523" src="https://github.com/user-attachments/assets/4c80dab5-e962-4bfd-9d97-8866e2ec0fd6" />

### Charging Session Service
<img width="1917" height="855" alt="Screenshot 2026-07-22 092543" src="https://github.com/user-attachments/assets/a7aeb1ab-0df4-4f9e-8625-ae109f03631f" />

### Payment Service
<img width="1917" height="796" alt="Screenshot 2026-07-22 092606" src="https://github.com/user-attachments/assets/c9d78a69-4b02-4d70-92f9-81593962fab4" />


---

<div align="center">

### 🌱 Built as part of the EV Charging Station Network Capstone Project

*Well-designed Repository • CRUD • Validation • Exception Handling • Logging • REST-based Microservices*

</div>
