package com.ritvik.school.user_service.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRunTimeException(RuntimeException rt){
        log.warn("RuntimeException handler");
        return new ResponseEntity<>("Internal server error:"+rt.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException rnfe){
        log.warn("ResourceNotFoundException handler");
        return new ResponseEntity<>("Resource not found:"+rnfe.getMessage(),HttpStatus.NOT_FOUND);
    }
}