package com.ritvik.school.teacher_service.exceptions;

public class WrongTeacherTypeException extends RuntimeException{
    public WrongTeacherTypeException(String message){
        super(message);
    }
}
