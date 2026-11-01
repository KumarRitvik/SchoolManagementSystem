package com.ritvik.school.academic_service.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ClassRoomAlreadyExistsException.class)
    public ResponseEntity<String> handleClassRoomAlreadyExistsException(ClassRoomAlreadyExistsException caee){
        log.warn("entered ClassRoomAlreadyExistsException handler");
        return new ResponseEntity<>("ClassRoom already exists:"+caee.getMessage(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(SubjectAlreadyExistsException.class)
    public ResponseEntity<String> handleSubjectAlreadyExistsException(SubjectAlreadyExistsException sae){
        log.warn("entered SubjectAlreadyExistsException handler");
        return new ResponseEntity<>("Subject already exists:"+sae.getMessage(),HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(SubjectDoesNotExistsException.class)
    public ResponseEntity<String> handleSubjectDoesNotExistsException(SubjectDoesNotExistsException sdne){
        log.warn("entered SubjectDoesNotExistsException handler");
        return new ResponseEntity<>("Subject does not exist:"+sdne.getMessage(),HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(ClassRoomDoesNotExistsException.class)
    public ResponseEntity<String> handleClassRoomNotExistsException(ClassRoomDoesNotExistsException crdnee){
        log.warn("entered ClassRoomDoesNotExistsException handler");
        return new ResponseEntity<>("ClassRoom does not exist:"+crdnee.getMessage(),HttpStatus.BAD_REQUEST);
    }
}
