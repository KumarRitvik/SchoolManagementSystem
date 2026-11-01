package com.ritvik.school.student_service.exceptions;

public class StudentNotFoundException extends RuntimeException {
    public  StudentNotFoundException(String message){
        super(message);
    }
}
