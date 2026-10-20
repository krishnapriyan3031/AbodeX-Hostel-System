package com.abodex.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entity representing an active cot allotment for a student.
 */
@Entity
@Table(name = "bookings")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "student_reg_no", nullable = false, unique = true)
    private Student student;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cot_id", nullable = false, unique = true)
    @JsonIgnore
    private Cot cot;

    @Column(name = "booking_time", nullable = false)
    private LocalDateTime bookingTime;

    @Column(name = "status", nullable = false, length = 30)
    private String status; // "CONFIRMED", "CANCELLED"

    public Booking() {}

    public Booking(Student student, Cot cot) {
        this.student = student;
        this.cot = cot;
        this.bookingTime = LocalDateTime.now();
        this.status = "CONFIRMED";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Cot getCot() {
        return cot;
    }

    public void setCot(Cot cot) {
        this.cot = cot;
    }

    public LocalDateTime getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(LocalDateTime bookingTime) {
        this.bookingTime = bookingTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
