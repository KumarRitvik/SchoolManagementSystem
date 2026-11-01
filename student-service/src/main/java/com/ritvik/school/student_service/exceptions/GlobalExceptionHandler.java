package com.ritvik.school.student_service.exceptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

    @Slf4j
    @ControllerAdvice
    public class GlobalExceptionHandler {

        @ExceptionHandler(StudentNotFoundException.class)
        public ResponseEntity<String> handleStudentNotFoundException(StudentNotFoundException snfe){
            return new ResponseEntity<String>("Student not found in database:"+snfe.getMessage(), HttpStatus.BAD_REQUEST);
        }
        @ExceptionHandler(StudentAlreadyExistsException.class)
        public ResponseEntity<String>  handleStudentAlreadyExistsException(StudentAlreadyExistsException saee){
            return new ResponseEntity<>("Student already exist in database:"+saee.getMessage(),HttpStatus.BAD_REQUEST);
        }
        @ExceptionHandler(WrongStudentTypeException.class)
        public ResponseEntity<String> handleWrongStudentTypeException(WrongStudentTypeException wste){
            log.warn("entered wrongStudentTypeException handler");
            return new ResponseEntity<>("Student type error:"+wste.getMessage(),HttpStatus.BAD_REQUEST);
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
        @ExceptionHandler(EnrollmentNotFoundException.class)
        public ResponseEntity<String> handleEnrollmentNotFoundException(EnrollmentNotFoundException snfe){
            log.warn("entered EnrollmentNotFoundException handler");
            return new ResponseEntity<>("Enrollment not found:"+snfe.getMessage(),HttpStatus.BAD_REQUEST);
        }


    }



