package com.ritvik.school.academic_service.exceptions;

public class ClassRoomDoesNotExistsException extends RuntimeException {
    public ClassRoomDoesNotExistsException(String message) {
        super(message);
    }
}
