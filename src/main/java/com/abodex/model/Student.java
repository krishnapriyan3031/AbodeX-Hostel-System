package com.abodex.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * Entity representing an Anna University student profile.
 */
@Entity
@Table(name = "students")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Student {

    @Id
    @NotBlank(message = "Register Number is required")
    @Pattern(regexp = "^[0-9]{7,12}$", message = "Register Number must be 7-12 digits")
    @Column(name = "register_number", nullable = false, unique = true, length = 20)
    private String registerNumber;

    @NotBlank(message = "Student name is required")
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @NotBlank(message = "Department is required")
    @Column(name = "department", nullable = false, length = 100)
    private String department;

    @Column(name = "gender", length = 20)
    private String gender; // "Boys" or "Girls"

    public Student() {}

    public Student(String registerNumber, String name, String department, String gender) {
        this.registerNumber = registerNumber;
        this.name = name;
        this.department = department;
        this.gender = gender;
    }

    public String getRegisterNumber() {
        return registerNumber;
    }

    public void setRegisterNumber(String registerNumber) {
        this.registerNumber = registerNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
