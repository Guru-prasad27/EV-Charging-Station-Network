# Charging Session Service

Part of the GreenCharge EV Charging Station Network microservices platform.

## Tech Stack
Java 17, Spring Boot 3.2.5, Spring Data JPA, H2 (in-memory), Bean Validation,
Lombok, RestTemplate (inter-service communication)

## Layered Architecture
```
controller/  -> REST endpoints (SessionController)
service/     -> Business logic interface + impl (SessionServiceImpl)
repository/  -> Spring Data JPA repository (SessionRepository)
entity/      -> JPA entities (ChargingSession, SessionStatus)
dto/         -> Request/response DTOs + BookingDTO (mirrors Booking Service response)
client/      -> BookingServiceClient - all RestTemplate calls live here
config/      -> RestTemplate bean
exception/   -> Custom exceptions + GlobalExceptionHandler (@ControllerAdvice)
```

## IMPORTANT: Run the Booking Service first
This service calls the Booking Service over HTTP (via RestTemplate) to
verify a booking exists and is CONFIRMED before starting a session.

Start in this order:
1. Charging Station Service - port **8081**
2. Booking Service - port **8082**
3. Charging Session Service (this one) - port **8083**

Config in `application.properties`:
```
booking.service.base-url=http://localhost:8082
```

## Setup

```bash
mvn clean install
mvn spring-boot:run
```
Service runs on **http://localhost:8083**

H2 console: **http://localhost:8083/h2-console**
- JDBC URL: `jdbc:h2:mem:sessiondb`
- User: `sa`, Password: *(blank)*

## Endpoints

| Method | Path | Description |
|---|---|---|
| POST | /sessions/start | Start a session for a CONFIRMED booking |
| PUT | /sessions/{id}/stop | Stop a session, recording energy consumed (kWh) |
| PUT | /sessions/{id}/terminate | Force-end a session without energy data (e.g. fault) |
| GET | /sessions | List all sessions (optionally `?userId=`) |
| GET | /sessions/{id} | Get a session by id |

## Sample Flow

1. Confirm a booking on the Booking Service:
   `PUT http://localhost:8082/bookings/1/confirm`

2. Start the session:
   ```
   POST http://localhost:8083/sessions/start
   { "bookingId": 1 }
   ```

3. Stop the session once charging finishes:
   ```
   PUT http://localhost:8083/sessions/1/stop
   { "energyConsumedKwh": 12.5 }
   ```

## Business Rules Enforced
- Booking must exist and be `CONFIRMED` -> `BookingNotConfirmedException` (400)
- Only one active (`IN_PROGRESS`) session per booking -> `SessionAlreadyActiveException` (409)
- Can't stop/terminate a session that's already ended -> `SessionAlreadyEndedException` (409)
- If the Booking Service can't be reached -> `BookingServiceUnavailableException` (503)

## Next Services
Payment Service, then the API Gateway - following this same layered pattern.
