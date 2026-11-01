package com.ritvik.school.teacher_service.exceptions;

public class TeacherAlreadyExistsException extends RuntimeException {
    public TeacherAlreadyExistsException(String message) {

        super(message);
    }
}
