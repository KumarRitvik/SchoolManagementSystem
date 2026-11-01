package com.ritvik.school.teacher_service.exceptions;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(TeacherNotFoundException.class)
    public ResponseEntity<String>  handleTeacherNotFoundException(TeacherNotFoundException tnfe){
        return new ResponseEntity<String>("Teacher not found in database:"+tnfe.getMessage(),HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(TeacherAlreadyExistsException.class)
    public ResponseEntity<String>  handleTeacherAlreadyExistsException(TeacherAlreadyExistsException taee){
        return new ResponseEntity<>("Teacher already exist in database:"+taee.getMessage(),HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(WrongTeacherTypeException.class)
    public ResponseEntity<String> handleWrongTeacherTypeException(WrongTeacherTypeException wtte){
        log.warn("entered wrongTeacherTypeException handler");
        return new ResponseEntity<>("Teacher type error:"+wtte.getMessage(),HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRunTimeException(RuntimeException rt){
        log.warn("RuntimeException handler");
        return new ResponseEntity<>("Internal server error:"+rt.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleWResourceNotFoundException(ResourceNotFoundException rnfe){
        log.warn("entered ResourceNotFoundException handler");
        return new ResponseEntity<>("Resource not present:"+rnfe.getMessage(),HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(FileStorageException.class)
    public ResponseEntity<String> handleFileStorageException(FileStorageException fse){
        log.warn("entered FileStorageException handler");
        return new ResponseEntity<>("FileStorageError:"+fse.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AssignmentAlreadyExistsException.class)
    public ResponseEntity<String> handleAssignmentAlreadyExistsException(AssignmentAlreadyExistsException aeee){
        log.warn("entered AssignmentAlreadyExistsException handler");
        return new ResponseEntity<>("Assignment already exists:"+aeee.getMessage(),HttpStatus.BAD_REQUEST);
    }
}