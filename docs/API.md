# SmartParkPro API Documentation

Interactive Swagger UI is available at:

- Local: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

All protected endpoints require:

```http
Authorization: Bearer <jwt>
```

## Authentication

| Method | Path | Description |
|---|---|---|
| POST | `/api/auth/register` | Create account and return JWT |
| POST | `/api/auth/login` | Authenticate and return JWT |

## Core CRUD APIs

| Module | Base Path |
|---|---|
| User Management | `/api/users` |
| Parking Management | `/api/parking-lots` |
| Parking Slot Management | `/api/parking-slots` |
| Vehicle Management | `/api/vehicles` |
| Booking Management | `/api/bookings` |
| Payment Management | `/api/payments` |

Each CRUD module supports:

| Method | Path | Description |
|---|---|---|
| GET | `{base}` | List records |
| GET | `{base}/{id}` | Get one record |
| POST | `{base}` | Create record |
| PUT | `{base}/{id}` | Update record |
| DELETE | `{base}/{id}` | Delete record |

## Operations

| Method | Path | Description |
|---|---|---|
| GET | `/api/dashboard` | Total vehicles, available slots, occupied slots, revenue |
| GET | `/api/analytics?vehicleType=CAR` | Demand prediction and slot recommendations |
| GET | `/api/ai/predict-demand` | Occupancy, peak hour, and revenue prediction |
| POST | `/api/waiting-queue` | Enqueue vehicle when parking is full |
| GET | `/api/waiting-queue/{parkingLotId}` | Read FIFO waiting queue |
| DELETE | `/api/waiting-queue/{parkingLotId}/next` | Dequeue next vehicle |
| GET | `/api/recent-activities` | Read recent activity stack |
| POST | `/api/tickets/scan-entry` | Scan QR ticket at entry |
| POST | `/api/tickets/scan-exit` | Scan QR ticket at exit |

## QR Ticket Payload

`POST /api/bookings` returns:

- `ticketCode`
- `qrCodeBase64`
- `checkedInAt`
- `checkedOutAt`

Scan request:

```json
{
  "ticketCode": "SPP-1234ABCD"
}
```
