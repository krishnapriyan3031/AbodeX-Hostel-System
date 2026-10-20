# AbodeX — Smart Hostel Room Allocation System
*A visual, cot-level hostel room allocation web application engineered with Spring Boot 3, Spring Data JPA, RESTful APIs, and OpenAPI/Swagger.*

[![Java](https://img.shields.io/badge/Java-17%2F21%2F25-orange.svg)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.4-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![JPA / Hibernate](https://img.shields.io/badge/ORM-Spring%20Data%20JPA-blue.svg)](https://spring.io/projects/spring-data-jpa)
[![OpenAPI / Swagger](https://img.shields.io/badge/Swagger-OpenAPI%203-green.svg)](http://localhost:8080/swagger-ui/index.html)
[![Database](https://img.shields.io/badge/Database-H2%20In--Memory-yellow.svg)](http://localhost:8080/h2-console)
[![Build Status](https://img.shields.io/badge/Tests-9%20Passed%20%2F%200%20Failed-success.svg)](test.bat)

---

## 📑 Project Documentation Index (Grading Rubric Alignment)

All documentation components specified in the evaluation rubric are organized within this repository:

| Rubric Component | Marks | File Location in GitHub | Description |
| :--- | :---: | :--- | :--- |
| **1. README** | **0.5** | [`README.md`](README.md) *(this file)* | Overview, architecture, build & run guide, database configuration, sample requests. |
| **2. API Documentation** | **0.5** | [`docs/API_DOCUMENTATION.md`](docs/API_DOCUMENTATION.md) | Full endpoint table, request/response bodies, cURL commands, Swagger & Postman guide. |
| **3. Design Documentation** | **0.5** | [`docs/DESIGN_DOCUMENTATION.md`](docs/DESIGN_DOCUMENTATION.md) | Short report, layered architecture diagram (Mermaid), and Entity-Relationship (ER) diagram. |
| **4. Code-Level Docs** | **0.5** | Located across `src/main/java/**` | Detailed Javadocs & comments on controllers, services, repositories, and non-obvious logic. |
| **Postman Collection** | — | [`abodex_postman_collection.json`](abodex_postman_collection.json) | Exported Postman collection ready to import and run test requests immediately. |
| **Viva Voce Cheat Sheet** | — | [`docs/VIVA_PREPARATION_GUIDE.md`](docs/VIVA_PREPARATION_GUIDE.md) | Request flow, DI explanation, annotation breakdown, and on-the-spot code modification scenarios. |

---

## 1. Project Overview & Problem Statement
In universities such as Anna University, hostel allocation is traditionally conducted at a coarse room-level or through paper/manual queues. This creates lack of visibility over cot ergonomics (students have distinct preferences for Upper vs. Lower bunks) and roommate transparency.

**AbodeX** resolves this by providing a **cot-level visual room allocation system**:
- **Campus Scale**: 13 Hostels for Boys and 8 Hostels for Girls.
- **Block Layout**: 4 Floors per block (Ground, First, Second, Third).
- **Floor Density**: 40 Rooms per floor (Rooms `001`–`040`, `101`–`140`, `201`–`240`, `301`–`340`) = **160 total rooms**.
- **Room Capacity**: 4 occupants per room distributed across 2 bunk beds:
  - `Bunk 1 · Upper` & `Bunk 1 · Lower`
  - `Bunk 2 · Upper` & `Bunk 2 · Lower`
  - Total **640 distinct cot positions** per hostel block.
- **Enforced Rules**:
  - **Single-Cot Policy**: A student (identified by their unique University Register Number) can hold only **one** cot across the university at any given time.
  - **Concurrency & Conflict Prevention**: Double booking of the same cot by two simultaneous requests is prevented with database constraints and transactional validation.

---

## 2. Technology Stack
- **Backend Framework**: Spring Boot 3.3.4
- **Language**: Java 17+ (Compatible with Java 21 & 25)
- **Data Persistence**: Spring Data JPA & Hibernate ORM
- **Database**: H2 Relational Database (Web Console enabled at `/h2-console`)
- **Validation**: Jakarta Bean Validation (`@Valid`, `@NotBlank`, `@Pattern`)
- **API Documentation**: Springdoc OpenAPI 3.0 / Swagger UI
- **Testing**: JUnit 5, Mockito, Spring Boot Test, MockMvc
- **Frontend (Bonus +1)**: Responsive, dark-mode single-page client serving real-time status and interactive cot selection matching design specifications.

---

## 3. Architecture & Design

### Layered Architecture Flow
```mermaid
graph TD
    Client["Client (Browser / Postman / Swagger)"] -->|"HTTP Requests (GET, POST, PUT, DELETE)"| Controller["Controller Layer (@RestController)<br/>HostelController, BookingController"]
    Controller -->|"DTOs & Validations (@Valid)"| Service["Service Layer (@Service)<br/>HostelService"]
    Service -->|"Entities & Business Rules"| Repository["Data Access Layer (@Repository)<br/>StudentRepo, RoomRepo, CotRepo, BookingRepo"]
    Repository -->|"Spring Data JPA / Hibernate"| DB[("Relational Database (H2 / MySQL)")]
```

### Entity Relationship (ER) Diagram
```mermaid
erDiagram
    HOSTEL_BLOCK ||--|{ ROOM : contains
    ROOM ||--|{ COT : contains
    STUDENT ||--o| BOOKING : holds
    COT ||--o| BOOKING : allocated_to

    HOSTEL_BLOCK {
        Long id PK
        String blockName
        String gender
    }

    ROOM {
        Long id PK
        String roomNumber
        int floor
        Long block_id FK
    }

    COT {
        Long id PK
        String cotCode
        int bunkNumber
        boolean upper
        boolean occupied
        Long room_id FK
    }

    STUDENT {
        String registerNumber PK
        String name
        String department
        String gender
    }

    BOOKING {
        Long id PK
        String student_reg_no FK
        Long cot_id FK
        LocalDateTime bookingTime
        String status
    }
```

*For complete design documentation, see [`docs/DESIGN_DOCUMENTATION.md`](docs/DESIGN_DOCUMENTATION.md).*

---

## 4. REST API Endpoint Summary (CRUD Operations)

| Operation | HTTP Method | Endpoint URL | Request Body | Response Code & Output | Description |
| :--- | :---: | :--- | :--- | :---: | :--- |
| **READ** | `GET` | `/api/blocks` | None | `200 OK` (Array of blocks) | Returns all 21 Anna University blocks |
| **READ** | `GET` | `/api/blocks/{blockName}/floors/{floor}` | None | `200 OK` (40 RoomCardDtos) | Returns 40 rooms with occupancy count |
| **READ** | `GET` | `/api/blocks/{blockName}/rooms/{roomNumber}`| None | `200 OK` (RoomCardDto + 4 Cots) | Returns cot occupancies & occupants |
| **CREATE** | `POST` | `/api/bookings` | `BookingRequestDto` (JSON) | `201 Created` (`BookingResponseDto`) | Allots an available cot to a student |
| **READ** | `GET` | `/api/bookings/{regNo}` | None | `200 OK` (`BookingResponseDto`) | Fetches active allotment for a student |
| **UPDATE** | `PUT` | `/api/bookings/{regNo}` | `BookingRequestDto` (JSON) | `200 OK` (`BookingResponseDto`) | Switches room/cot for student |
| **DELETE** | `DELETE` | `/api/bookings/{regNo}` | None | `204 No Content` | Vacates cot & cancels booking |

*For complete endpoint specifications, JSON schemas, and error responses, see [`docs/API_DOCUMENTATION.md`](docs/API_DOCUMENTATION.md).*

---

## 5. Sample Requests and Responses

### 1. Create Cot Booking (`POST /api/bookings`)
**Request Body**:
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

**Response (`201 Created`)**:
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

### 2. Error Response on Duplicate Booking Attempt (`409 Conflict`)
```json
{
  "timestamp": "2026-10-21T03:16:22",
  "status": 409,
  "error": "Booking Conflict",
  "message": "Student with Register No 2023103001 already has an active cot booking. Anna University allows only 1 cot per student.",
  "path": "/api/bookings"
}
```

---

## 6. How to Build and Run the Project

### Prerequisites
- **Java**: JDK 17, 21, or 25 installed.
- **Maven**: Maven 3.8+ (A convenience `run.bat` is included that uses the bundled environment automatically).

### Option A: Quick Launch (Windows)
Double-click [`run.bat`](run.bat) in the root directory.

### Option B: Command Line (Any OS)
```bash
# 1. Clone the repository
git clone https://github.com/krishnapriyan3031/AbodeX-Hostel-System.git
cd AbodeX-Hostel-System

# 2. Compile and run tests
mvn clean test

# 3. Start the application
mvn spring-boot:run
```

Once started, the following services are live:
- 🌐 **Web Frontend (Interactive Booking Dashboard)**: [`http://localhost:8080`](http://localhost:8080)
- 📄 **Interactive Swagger / OpenAPI UI**: [`http://localhost:8080/swagger-ui/index.html`](http://localhost:8080/swagger-ui/index.html)
- 🗄️ **H2 Database Console**: [`http://localhost:8080/h2-console`](http://localhost:8080/h2-console)
  - JDBC URL: `jdbc:h2:mem:abodexdb`
  - Username: `sa`
  - Password: `password`

---

## 7. Automated Testing Evidence (Criteria 6)

The project includes unit and integration tests executing under JUnit 5 and Spring MockMvc:
```bash
mvn test
```
Result:
```text
[INFO] Tests run: 9, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

- [`BookingServiceTest`](src/test/java/com/abodex/BookingServiceTest.java): Verifies business rules (single-cot rule, duplicate rejection, occupied cot conflict, booking cancellation).
- [`BookingControllerTest`](src/test/java/com/abodex/BookingControllerTest.java): Verifies HTTP status codes (`200`, `201`, `204`, `400`), validation filters, and MockMvc endpoints.
