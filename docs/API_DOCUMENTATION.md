# AbodeX — REST API Specification & Documentation
*Standardized API documentation adhering to Criterion 2 & Documentation Breakdown Item 2 (0.5 Marks).*

**Base URL**: `http://localhost:8080`  
**Swagger UI**: `http://localhost:8080/swagger-ui/index.html`  
**OpenAPI Specification**: `http://localhost:8080/api-docs`

---

## 1. REST Endpoint Summary Table

| Operation | HTTP Method | Endpoint URL | Request Body | Response Code | Description |
| :--- | :---: | :--- | :--- | :---: | :--- |
| **READ** | `GET` | `/api/blocks` | None | `200 OK` | Retrieves all 21 Anna University hostel blocks (13 Boys, 8 Girls). |
| **READ** | `GET` | `/api/blocks/{blockName}/floors/{floor}` | None | `200 OK` | Retrieves 40 rooms for a specific floor with available cot counts. |
| **READ** | `GET` | `/api/blocks/{blockName}/rooms/{roomNumber}` | None | `200 OK` | Retrieves detailed cot states (Bunk 1/2 Upper/Lower) and occupants for a room. |
| **CREATE** | `POST` | `/api/bookings` | `BookingRequestDto` (JSON) | `201 Created` | Allocates an available cot to a registered student. |
| **READ** | `GET` | `/api/bookings/{registerNumber}` | None | `200 OK` | Retrieves the active cot allotment for a student. |
| **UPDATE** | `PUT` | `/api/bookings/{registerNumber}` | `BookingRequestDto` (JSON) | `200 OK` | Switches the student to another room or cot position. |
| **DELETE** | `DELETE` | `/api/bookings/{registerNumber}` | None | `204 No Content` | Vacates the allotted cot and cancels the booking. |

---

## 2. Detailed Endpoint Specifications

### 2.1 GET All Hostel Blocks
Retrieves all configured hostel blocks in the university.

- **URL**: `/api/blocks`
- **Method**: `GET`
- **Headers**: `Accept: application/json`
- **cURL Request**:
  ```bash
  curl -X GET "http://localhost:8080/api/blocks"
  ```
- **Response (`200 OK`)**:
  ```json
  [
    {
      "id": 1,
      "blockName": "Kurinji Block (Boys)",
      "gender": "Boys"
    },
    {
      "id": 14,
      "blockName": "Ganga Block (Girls)",
      "gender": "Girls"
    }
  ]
  ```

---

### 2.2 GET Floor Rooms
Retrieves all 40 rooms for the specified floor index (0: Ground, 1: First, 2: Second, 3: Third).

- **URL**: `/api/blocks/{blockName}/floors/{floor}`
- **Method**: `GET`
- **Path Parameters**:
  - `blockName` (String, required): e.g., `Kurinji Block (Boys)`
  - `floor` (Integer, required): `0`, `1`, `2`, or `3`
- **cURL Request**:
  ```bash
  curl -X GET "http://localhost:8080/api/blocks/Kurinji%20Block%20(Boys)/floors/0"
  ```
- **Response (`200 OK`)**:
  ```json
  [
    {
      "roomId": 1,
      "roomNumber": "001",
      "floor": 0,
      "availableCots": 4,
      "totalCots": 4,
      "statusText": "4/4 free",
      "occupancyState": "AVAILABLE"
    },
    {
      "roomId": 2,
      "roomNumber": "002",
      "floor": 0,
      "availableCots": 2,
      "totalCots": 4,
      "statusText": "2/4 free",
      "occupancyState": "PARTIALLY_OCCUPIED"
    },
    {
      "roomId": 3,
      "roomNumber": "003",
      "floor": 0,
      "availableCots": 0,
      "totalCots": 4,
      "statusText": "0/4 free",
      "occupancyState": "FULLY_OCCUPIED"
    }
  ]
  ```

---

### 2.3 GET Room Cot Details
Retrieves cot-level occupancy details for a specific room.

- **URL**: `/api/blocks/{blockName}/rooms/{roomNumber}`
- **Method**: `GET`
- **cURL Request**:
  ```bash
  curl -X GET "http://localhost:8080/api/blocks/Kurinji%20Block%20(Boys)/rooms/014"
  ```
- **Response (`200 OK`)**:
  ```json
  {
    "roomId": 14,
    "roomNumber": "014",
    "floor": 0,
    "availableCots": 3,
    "totalCots": 4,
    "statusText": "3/4 free",
    "occupancyState": "PARTIALLY_OCCUPIED",
    "cots": [
      {
        "cotId": 53,
        "cotCode": "B1_U",
        "bunkNumber": 1,
        "upper": true,
        "label": "Bunk 1 · Upper",
        "occupied": false,
        "occupantName": null
      },
      {
        "cotId": 54,
        "cotCode": "B1_L",
        "bunkNumber": 1,
        "upper": false,
        "label": "Bunk 1 · Lower",
        "occupied": true,
        "occupantName": "Student A",
        "occupantRegNo": "202310122",
        "occupantDepartment": "Computer Science"
      },
      {
        "cotId": 55,
        "cotCode": "B2_U",
        "bunkNumber": 2,
        "upper": true,
        "label": "Bunk 2 · Upper",
        "occupied": false,
        "occupantName": null
      },
      {
        "cotId": 56,
        "cotCode": "B2_L",
        "bunkNumber": 2,
        "upper": false,
        "label": "Bunk 2 · Lower",
        "occupied": false,
        "occupantName": null
      }
    ]
  }
  ```

---

### 2.4 CREATE Cot Booking
Reserves an available cot for a student. Enforces the university's single-cot rule and checks cot availability.

- **URL**: `/api/bookings`
- **Method**: `POST`
- **Headers**: `Content-Type: application/json`
- **Request Body**:
  ```json
  {
    "registerNumber": "2023103001",
    "name": "Arun Kumar",
    "department": "Computer Science and Engineering",
    "gender": "Boys",
    "blockName": "Kurinji Block (Boys)",
    "roomNumber": "014",
    "cotCode": "B1_U"
  }
  ```
- **cURL Request**:
  ```bash
  curl -X POST "http://localhost:8080/api/bookings" \
       -H "Content-Type: application/json" \
       -d '{"registerNumber":"2023103001","name":"Arun Kumar","department":"Computer Science and Engineering","gender":"Boys","blockName":"Kurinji Block (Boys)","roomNumber":"014","cotCode":"B1_U"}'
  ```
- **Response (`201 Created`)**:
  ```json
  {
    "bookingId": 1,
    "registerNumber": "2023103001",
    "studentName": "Arun Kumar",
    "department": "Computer Science and Engineering",
    "blockName": "Kurinji Block (Boys)",
    "roomNumber": "014",
    "floor": 0,
    "cotCode": "B1_U",
    "cotLabel": "Bunk 1 · Upper",
    "bookingTime": "2026-10-21T03:15:00",
    "status": "CONFIRMED"
  }
  ```

---

### 2.5 READ Student Booking
Fetches a student's active booking allotment.

- **URL**: `/api/bookings/{registerNumber}`
- **Method**: `GET`
- **cURL Request**:
  ```bash
  curl -X GET "http://localhost:8080/api/bookings/2023103001"
  ```
- **Response (`200 OK`)**:
  ```json
  {
    "bookingId": 1,
    "registerNumber": "2023103001",
    "studentName": "Arun Kumar",
    "department": "Computer Science and Engineering",
    "blockName": "Kurinji Block (Boys)",
    "roomNumber": "014",
    "floor": 0,
    "cotCode": "B1_U",
    "cotLabel": "Bunk 1 · Upper",
    "bookingTime": "2026-10-21T03:15:00",
    "status": "CONFIRMED"
  }
  ```

---

### 2.6 UPDATE: Switch Cot or Room
Updates an existing allotment by switching the student to another room or cot position.

- **URL**: `/api/bookings/{registerNumber}`
- **Method**: `PUT`
- **Headers**: `Content-Type: application/json`
- **Request Body**:
  ```json
  {
    "registerNumber": "2023103001",
    "name": "Arun Kumar",
    "department": "Computer Science and Engineering",
    "gender": "Boys",
    "blockName": "Kurinji Block (Boys)",
    "roomNumber": "015",
    "cotCode": "B2_L"
  }
  ```
- **cURL Request**:
  ```bash
  curl -X PUT "http://localhost:8080/api/bookings/2023103001" \
       -H "Content-Type: application/json" \
       -d '{"registerNumber":"2023103001","name":"Arun Kumar","department":"Computer Science and Engineering","gender":"Boys","blockName":"Kurinji Block (Boys)","roomNumber":"015","cotCode":"B2_L"}'
  ```
- **Response (`200 OK`)**:
  ```json
  {
    "bookingId": 1,
    "registerNumber": "2023103001",
    "studentName": "Arun Kumar",
    "department": "Computer Science and Engineering",
    "blockName": "Kurinji Block (Boys)",
    "roomNumber": "015",
    "floor": 0,
    "cotCode": "B2_L",
    "cotLabel": "Bunk 2 · Lower",
    "bookingTime": "2026-10-21T03:15:00",
    "status": "CONFIRMED"
  }
  ```

---

### 2.7 DELETE: Vacate Allotted Cot
Vacates the cot and cancels the student's booking.

- **URL**: `/api/bookings/{registerNumber}`
- **Method**: `DELETE`
- **cURL Request**:
  ```bash
  curl -X DELETE "http://localhost:8080/api/bookings/2023103001"
  ```
- **Response**: `204 No Content` (Empty body)

---

## 3. Standardized Error Responses (`@ControllerAdvice`)

All application exceptions are routed through `GlobalExceptionHandler` to produce uniform JSON structures:

| HTTP Status | Error Type | Scenario |
| :---: | :--- | :--- |
| **`400 Bad Request`** | `Validation Failed` | Missing name, register number not 7-12 digits, missing block/room/cot |
| **`404 Not Found`** | `Not Found` | Invalid block, room number, cot code, or student register number |
| **`409 Conflict`** | `Booking Conflict` | Student already holds an active cot (single-cot policy violation) |
| **`409 Conflict`** | `Conflict` | The selected cot is already occupied by another student |

### Sample Error Payloads

#### Duplicate Booking Violation (`409 Conflict`):
```json
{
  "timestamp": "2026-10-21T03:20:10.512",
  "status": 409,
  "error": "Booking Conflict",
  "message": "Student with Register No 2023103001 already has an active cot booking. Anna University allows only 1 cot per student.",
  "path": "/api/bookings"
}
```

#### Bean Validation Error (`400 Bad Request`):
```json
{
  "timestamp": "2026-10-21T03:22:45.109",
  "status": 400,
  "error": "Validation Failed",
  "message": "Input payload contains validation errors",
  "path": "/api/bookings",
  "validationErrors": {
    "registerNumber": "Register number must be between 7 and 12 digits",
    "roomNumber": "Room number is required"
  }
}
```
