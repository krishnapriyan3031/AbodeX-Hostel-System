package com.abodex.dto;

import java.time.LocalDateTime;

/**
 * Clean response DTO for booking details.
 */
public class BookingResponseDto {
    private Long bookingId;
    private String registerNumber;
    private String studentName;
    private String department;
    private String blockName;
    private String roomNumber;
    private int floor;
    private String cotCode;
    private String cotLabel;
    private LocalDateTime bookingTime;
    private String status;

    public BookingResponseDto() {}

    public Long getBookingId() { return bookingId; }
    public void setBookingId(Long bookingId) { this.bookingId = bookingId; }

    public String getRegisterNumber() { return registerNumber; }
    public void setRegisterNumber(String registerNumber) { this.registerNumber = registerNumber; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getBlockName() { return blockName; }
    public void setBlockName(String blockName) { this.blockName = blockName; }

    public String getRoomNumber() { return roomNumber; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }

    public int getFloor() { return floor; }
    public void setFloor(int floor) { this.floor = floor; }

    public String getCotCode() { return cotCode; }
    public void setCotCode(String cotCode) { this.cotCode = cotCode; }

    public String getCotLabel() { return cotLabel; }
    public void setCotLabel(String cotLabel) { this.cotLabel = cotLabel; }

    public LocalDateTime getBookingTime() { return bookingTime; }
    public void setBookingTime(LocalDateTime bookingTime) { this.bookingTime = bookingTime; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
