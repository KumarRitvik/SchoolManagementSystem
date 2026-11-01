package com.ritvik.school.academic_service.exceptions;

public class ClassRoomAlreadyExistsException extends RuntimeException {
    public ClassRoomAlreadyExistsException(String  message) {
        super(message);
    }
}
