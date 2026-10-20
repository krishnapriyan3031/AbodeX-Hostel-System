package com.abodex.exception;

public class StudentAlreadyBookedException extends RuntimeException {
    public StudentAlreadyBookedException(String message) {
        super(message);
    }
}
