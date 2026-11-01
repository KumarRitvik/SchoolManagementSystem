package com.ritvik.school.student_service.exceptions;

public class StudentAlreadyExistsException extends RuntimeException{
    public StudentAlreadyExistsException(String  message){
        super(message);
    }
}
