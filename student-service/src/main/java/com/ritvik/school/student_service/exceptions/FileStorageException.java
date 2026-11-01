package com.ritvik.school.student_service.exceptions;

public class FileStorageException extends RuntimeException{
    public FileStorageException(String message, Exception exception) {
        super(message);
    }
}
