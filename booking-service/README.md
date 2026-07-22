# Booking Service

Part of the GreenCharge EV Charging Station Network microservices platform.

## Tech Stack
Java 17, Spring Boot 3.2.5, Spring Data JPA, H2 (in-memory), Bean Validation,
Lombok, RestTemplate (inter-service communication)

## Layered Architecture
```
controller/  -> REST endpoints (BookingController)
service/     -> Business logic interface + impl (BookingServiceImpl)
repository/  -> Spring Data JPA repository (BookingRepository)
entity/      -> JPA entities (Booking, BookingStatus)
dto/         -> Request/response DTOs + StationDTO (mirrors Station Service response)
client/      -> StationServiceClient - all RestTemplate calls live here
config/      -> RestTemplate bean
exception/   -> Custom exceptions + GlobalExceptionHandler (@ControllerAdvice)
```

## IMPORTANT: Run the Charging Station Service first
This service calls the Charging Station Service over HTTP (via RestTemplate)
to verify a connector exists and is available before confirming a booking.
Start that service first on **port 8081**, then start this one.

The URL it calls is configured in `application.properties`:
```
station.service.base-url=http://localhost:8081
```

## Setup

1. No external database needed - H2 runs in memory.
2. Build and run:
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```
3. Service runs on **http://localhost:8082**
4. H2 console: **http://localhost:8082/h2-console**
   - JDBC URL: `jdbc:h2:mem:bookingdb`
   - User: `sa`, Password: *(blank)*

## Endpoints

| Method | Path | Description |
|---|---|---|
| POST | /bookings | Create a booking (checks connector availability + slot conflicts) |
| GET | /bookings | List all bookings (optionally `?userId=`) |
| GET | /bookings/{id} | Get a booking by id |
| PUT | /bookings/{id}/confirm | Move status PENDING -> CONFIRMED |
| PUT | /bookings/{id}/cancel | Move status to CANCELLED |
| DELETE | /bookings/{id} | Delete a booking |

## Sample Request (POST /bookings)
```json
{
  "stationId": 1,
  "connectorId": 1,
  "userId": "user-123",
  "startTime": "2026-08-01T10:00:00",
  "endTime": "2026-08-01T11:00:00"
}
```
Note: `stationId` and `connectorId` must correspond to a real station/connector
already created in the Charging Station Service (see its README for the
sample POST /stations request).

## Business Rules Enforced
- `endTime` must be after `startTime` -> `InvalidBookingTimeException` (400)
- Connector must exist and be available on the Station Service ->
  `ConnectorUnavailableException` (409)
- No two active (PENDING/CONFIRMED) bookings can overlap on the same
  connector -> `SlotAlreadyBookedException` (409)
- If the Station Service can't be reached -> `StationServiceUnavailableException` (503)

## Next Services
Charging Session Service, Payment Service, then the API Gateway - following
this same layered pattern.
