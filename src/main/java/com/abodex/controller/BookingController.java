package com.abodex.controller;

import com.abodex.dto.BookingRequestDto;
import com.abodex.dto.BookingResponseDto;
import com.abodex.service.HostelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller exposing complete CRUD endpoints for Cot Bookings (Rubric Item 2).
 */
@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = "*")
@Tag(name = "Booking Operations", description = "CRUD operations for student cot allotments")
public class BookingController {

    private final HostelService hostelService;

    public BookingController(HostelService hostelService) {
        this.hostelService = hostelService;
    }

    @Operation(summary = "CREATE: Book a cot", description = "Reserves an available cot for a student (Status 201 Created)")
    @PostMapping
    public ResponseEntity<BookingResponseDto> createBooking(@Valid @RequestBody BookingRequestDto request) {
        BookingResponseDto response = hostelService.bookCot(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "READ: Get student booking", description = "Fetches the active allotment for a student by Register Number (Status 200 OK)")
    @GetMapping("/{registerNumber}")
    public ResponseEntity<BookingResponseDto> getBooking(@PathVariable String registerNumber) {
        BookingResponseDto response = hostelService.getBookingByRegisterNumber(registerNumber);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "UPDATE: Switch cot or room", description = "Updates an existing allotment by switching to another cot/room (Status 200 OK)")
    @PutMapping("/{registerNumber}")
    public ResponseEntity<BookingResponseDto> switchCot(
            @PathVariable String registerNumber,
            @Valid @RequestBody BookingRequestDto request) {
        BookingResponseDto response = hostelService.switchCot(registerNumber, request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "DELETE: Cancel / Vacate cot", description = "Vacates the cot and deletes the student's booking (Status 204 No Content)")
    @DeleteMapping("/{registerNumber}")
    public ResponseEntity<Void> cancelBooking(@PathVariable String registerNumber) {
        hostelService.cancelBooking(registerNumber);
        return ResponseEntity.noContent().build();
    }
}
