package com.ritvik.school.academic_service.exceptions;

public class SubjectDoesNotExistsException extends RuntimeException{
    public SubjectDoesNotExistsException(String message) {
        super(message);
    }
}
