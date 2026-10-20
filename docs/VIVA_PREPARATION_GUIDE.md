# AbodeX — Viva Voce Preparation & Technical Defense Guide
*Targeted guide to secure full marks (2.0 / 2.0) in Criterion 8 (Viva / Individual Understanding).*

---

## 1. Core Spring Concepts & Annotations Explained

### `@SpringBootApplication`
- **What it does**: Meta-annotation combining three critical annotations:
  1. `@Configuration`: Marks the class as a source of bean definitions.
  2. `@EnableAutoConfiguration`: Tells Spring Boot to automatically configure beans based on classpath dependencies (e.g. configuring Tomcat when `spring-boot-starter-web` is detected).
  3. `@ComponentScan`: Scans the current package (`com.abodex`) and subpackages to discover annotated components (`@Service`, `@Repository`, `@RestController`).

### `@RestController` vs. `@Controller`
- `@Controller` is traditionally used in Spring MVC to return HTML view templates (e.g. Thymeleaf/JSP).
- `@RestController` combines `@Controller` and `@ResponseBody`. It indicates that the return value of every method is automatically serialized into JSON or XML and written directly to the HTTP response body.

### `@Service` and `@Repository`
- Both are specializations of `@Component`.
- `@Service`: Documents the class as a business logic layer component.
- `@Repository`: Indicates that the class provides data storage/retrieval operations and automatically enables Spring's data-access exception translation.

### Dependency Injection (DI) & Inversion of Control (IoC)
- **Concept**: Instead of a class manually instantiating its dependencies using `new HostelRepository()`, the Spring IoC Container creates and injects them.
- **Why Constructor Injection?**:
  - Dependencies can be declared `final`, guaranteeing immutability.
  - Prevents `NullPointerException` during standalone unit testing (Mockito can inject mock objects without launching the Spring context).
  - Clearly exposes all required dependencies upfront.

---

## 2. Request Lifecycle & Architecture Flow

```text
HTTP Request (Browser / Postman)
    │
    ▼
DispatcherServlet (Spring Front Controller)
    │
    ▼
HandlerMapping (Finds matching @GetMapping / @PostMapping)
    │
    ▼
Validation Filter (@Valid / MethodArgumentNotValidException)
    │
    ▼
Controller Layer (@RestController)
    │
    ▼
Service Layer (@Service + @Transactional)
    │
    ▼
Repository Layer (Spring Data JpaRepository)
    │
    ▼
Database (H2 / Hibernate ORM)
```

1. **DispatcherServlet**: Central front-controller in Spring MVC that intercepts incoming HTTP requests.
2. **HandlerMapping**: Inspects `@RequestMapping` patterns to determine which controller method handles the request.
3. **Data Binding & Validation**: JSON request payload is deserialized into Java DTOs (`BookingRequestDto`), and Jakarta Bean Validation rules are applied.
4. **Service Execution**: Transaction management ensures that if an error occurs while writing to the database, the operation rolls back cleanly.
5. **Exception Handling**: If any runtime exception is thrown, it is intercepted by `@ControllerAdvice` rather than bubbling up as an ugly stack trace.

---

## 3. Likely "On-The-Spot" Live Code Changes & How to Answer

During the viva, professors often ask candidates to make a minor modification on the spot to test individual understanding:

### Scenario A: "How would you add a student's Phone Number or Email?"
1. **Entity**: Open `Student.java`, add:
   ```java
   @Column(name = "phone_number", length = 15)
   private String phoneNumber;
   // generate getter and setter
   ```
2. **DTO**: Open `BookingRequestDto.java`, add the field with validation:
   ```java
   @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be 10 digits")
   private String phoneNumber;
   ```
3. **Service**: In `HostelService.java`, update:
   ```java
   student.setPhoneNumber(request.getPhoneNumber());
   ```

### Scenario B: "How do you increase rooms per floor from 40 to 50?"
- Open `DataInitializer.java`, locate `initializeBlockRooms`:
  ```java
  for (int r = 1; r <= 50; r++) { ... } // Change 40 to 50
  ```
- Open `HostelBlock.java`, update the constant:
  ```java
  public static final int ROOMS_PER_FLOOR = 50;
  ```

### Scenario C: "How do you add an endpoint to view all students who booked cots on a given floor?"
- Open `BookingController.java`, add:
  ```java
  @GetMapping("/floor/{floor}")
  public ResponseEntity<List<BookingResponseDto>> getBookingsByFloor(@PathVariable int floor) {
      return ResponseEntity.ok(hostelService.getBookingsForFloor(floor));
  }
  ```
- Implement the query in `BookingRepository.java`:
  ```java
  List<Booking> findByCot_Room_Floor(int floor);
  ```

---

## 4. Key Defense Points for High Marks
- **Single-Cot Policy**: Explain that institutional rules require 1 student $\rightarrow$ 1 cot. Highlight the `@OneToOne @JoinColumn(unique = true)` database constraint and the service-level check `existsByStudent_RegisterNumber()`.
- **Concurrency & Double-Booking Protection**: Explain how transactional isolation and unique database keys guarantee that two simultaneous clicks on the same cot cannot both succeed.
- **REST Status Code Correctness**: Point out that you don't just return `200 OK` for everything—you return `201 Created` for new bookings, `204 No Content` for successful cancellation, `400 Bad Request` for validation errors, and `409 Conflict` for duplicate attempts.
