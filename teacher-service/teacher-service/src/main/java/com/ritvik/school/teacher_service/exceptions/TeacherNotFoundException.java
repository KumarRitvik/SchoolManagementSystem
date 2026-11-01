package com.ritvik.school.teacher_service.exceptions;

public class TeacherNotFoundException extends RuntimeException{
    public TeacherNotFoundException(String message){
        super(message);
    }
}
