# Payment Service

Part of the GreenCharge EV Charging Station Network microservices platform.

## Tech Stack
Java 17, Spring Boot 3.2.5, Spring Data JPA, H2 (in-memory), Bean Validation,
Lombok, RestTemplate (inter-service communication)

## Layered Architecture
```
controller/  -> REST endpoints (PaymentController)
service/     -> Business logic interface + impl (PaymentServiceImpl)
repository/  -> Spring Data JPA repository (PaymentRepository)
entity/      -> JPA entities (Payment, PaymentStatus, PaymentMethod)
dto/         -> Request/response DTOs + SessionDTO (mirrors Session Service response)
client/      -> SessionServiceClient - all RestTemplate calls live here
config/      -> RestTemplate bean
exception/   -> Custom exceptions + GlobalExceptionHandler (@ControllerAdvice)
```

## IMPORTANT: Run the Session Service first
This service calls the Charging Session Service over HTTP (via RestTemplate)
to verify a session exists and is COMPLETED before charging for it, and
to read `energyConsumedKwh` for the billing calculation.

Start in this order:
1. Charging Station Service - port **8081**
2. Booking Service - port **8082**
3. Charging Session Service - port **8083**
4. Payment Service (this one) - port **8084**

Config in `application.properties`:
```
session.service.base-url=http://localhost:8083
billing.rate-per-kwh=8.50
```

## Setup

```bash
mvn clean install
mvn spring-boot:run
```
Service runs on **http://localhost:8084**

H2 console: **http://localhost:8084/h2-console**
- JDBC URL: `jdbc:h2:mem:paymentdb`
- User: `sa`, Password: *(blank)*

## Endpoints

| Method | Path | Description |
|---|---|---|
| POST | /payments | Charge for a COMPLETED session (amount = energyConsumedKwh x rate) |
| PUT | /payments/{id}/refund | Refund a successful payment |
| GET | /payments | List all payments (optionally `?userId=`) |
| GET | /payments/{id} | Get a payment by id |

## Sample Flow

1. Stop a session on the Session Service (records energy consumed):
   ```
   PUT http://localhost:8083/sessions/1/stop
   { "energyConsumedKwh": 12.5 }
   ```

2. Pay for it:
   ```
   POST http://localhost:8084/payments
   {
     "sessionId": 1,
     "paymentMethod": "UPI"
   }
   ```
   With the default rate (8.50/kWh), this returns `amount: 106.25`.

## Business Rules Enforced
- Session must exist and be `COMPLETED` -> `SessionNotCompletedException` (400)
- Only one payment per session -> `PaymentAlreadyExistsException` (409)
- Only a `SUCCESS` payment can be refunded -> `InvalidRefundRequestException` (400)
- If the Session Service can't be reached -> `SessionServiceUnavailableException` (503)

## Next Step
API Gateway - routes external traffic to all 4 services (Station, Booking,
Session, Payment) as the single entry point, per the project notes
("Implement API Gateway to communicate with external world").
