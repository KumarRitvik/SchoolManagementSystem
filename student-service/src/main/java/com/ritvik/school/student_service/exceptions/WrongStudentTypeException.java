package com.ritvik.school.student_service.exceptions;

public class WrongStudentTypeException extends  RuntimeException{
    public WrongStudentTypeException(String message){
        super(message);
    }

}
