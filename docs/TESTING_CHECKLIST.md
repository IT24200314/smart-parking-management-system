# SmartParkPro Testing Checklist

Use Swagger UI at `http://localhost:8080/swagger-ui.html`.

## Toolchain Proof

Run:

```bash
java -version
mvn -version
docker --version
```

Expected:

- Java 21
- Maven 3.9+
- Docker Desktop available

## Automated Backend Verification

```bash
cd backend
mvn clean test
mvn verify
```

Coverage report:

- `backend/target/site/jacoco/index.html`

Portfolio screenshot placeholders:

- `docs/screenshots/swagger-crud.png`
- `docs/screenshots/jacoco-coverage.png`
- `docs/screenshots/github-actions.png`
- `docs/screenshots/qr-ticket.png`

## CRUD Swagger Checklist

| Module | Create | Read | Update | Delete |
|---|---|---|---|---|
| Users | ✅ | ✅ | ✅ | ✅ |
| Parking Lots | ✅ | ✅ | ✅ | ✅ |
| Parking Slots | ✅ | ✅ | ✅ | ✅ |
| Vehicles | ✅ | ✅ | ✅ | ✅ |
| Bookings | ✅ | ✅ | ✅ | ✅ |
| Payments | ✅ | ✅ | ✅ | ✅ |

## Portfolio Feature Checklist

| Feature | Endpoint | Evidence |
|---|---|---|
| QR ticket generation | `POST /api/bookings` | Response contains `ticketCode` and `qrCodeBase64` |
| Entry scan | `POST /api/tickets/scan-entry` | Booking status becomes `CHECKED_IN`, slot becomes `OCCUPIED` |
| Exit scan | `POST /api/tickets/scan-exit` | Booking status becomes `COMPLETED`, slot becomes `AVAILABLE` |
| Booking confirmation email | `POST /api/bookings` | Enable `MAIL_NOTIFICATIONS_ENABLED=true` |
| Payment receipt email | `POST /api/payments` | Payment with `PAID` status sends receipt |
| AI demand prediction | `GET /api/ai/predict-demand` | Occupancy, peak window, revenue forecast |
