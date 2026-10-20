package com.abodex.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * Request payload for creating or switching a cot booking.
 */
public class BookingRequestDto {

    @NotBlank(message = "Student register number is required")
    @Pattern(regexp = "^[0-9]{7,12}$", message = "Register number must be between 7 and 12 digits")
    private String registerNumber;

    @NotBlank(message = "Student name is required")
    private String name;

    @NotBlank(message = "Department is required")
    private String department;

    private String gender;

    @NotBlank(message = "Hostel block name is required")
    private String blockName;

    @NotBlank(message = "Room number is required")
    private String roomNumber;

    @NotBlank(message = "Cot code is required (e.g. B1_U, B1_L, B2_U, B2_L)")
    private String cotCode;

    public BookingRequestDto() {}

    public String getRegisterNumber() { return registerNumber; }
    public void setRegisterNumber(String registerNumber) { this.registerNumber = registerNumber; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getBlockName() { return blockName; }
    public void setBlockName(String blockName) { this.blockName = blockName; }

    public String getRoomNumber() { return roomNumber; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }

    public String getCotCode() { return cotCode; }
    public void setCotCode(String cotCode) { this.cotCode = cotCode; }
}
