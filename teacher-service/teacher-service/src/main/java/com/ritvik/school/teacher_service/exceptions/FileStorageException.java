package com.ritvik.school.teacher_service.exceptions;

public class FileStorageException extends RuntimeException{
    public FileStorageException(String message, Exception exception) {
        super(message);
    }
}
