package com.ritvik.school.teacher_service.exceptions;

public class AssignmentAlreadyExistsException extends RuntimeException {
    public AssignmentAlreadyExistsException(String message) {
        super(message);
    }
}
