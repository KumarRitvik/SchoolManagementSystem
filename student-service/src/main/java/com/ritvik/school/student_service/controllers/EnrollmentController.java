package com.ritvik.school.student_service.controllers;

import com.ritvik.school.student_service.dtos.EnrollmentDto;
import com.ritvik.school.student_service.dtos.SubjectDto;
import com.ritvik.school.student_service.service.EnrollmentService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/enrollment")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }


    @PostMapping("/createEnrollment")
    public ResponseEntity<EnrollmentDto> createEnrollment(@RequestBody EnrollmentDto enrollmentDto) {
        return ResponseEntity.ok(enrollmentService.createEnrollment(enrollmentDto));
    }

    @GetMapping("/viewAllEnrollments")
    public ResponseEntity<Page<EnrollmentDto>> viewAllEnrollments(@RequestParam int page,

                                                                  @RequestParam int size,

                                                                  @RequestParam(defaultValue = "enrollmentId") String sortBy,

                                                                  @RequestParam(defaultValue = "asc") String direction) {
        return ResponseEntity.ok(enrollmentService.viewAllEnrollments(page, size, sortBy, direction));
    }


    @GetMapping("/viewEnrollmentById")
    public ResponseEntity<EnrollmentDto> viewEnrollmentById(@RequestParam long enrollmentId) {
        return ResponseEntity.ok(enrollmentService.viewEnrollmentById(enrollmentId));
    }


    @PutMapping("/updateEnrollment")
    public ResponseEntity<EnrollmentDto> updateEnrollment(@RequestBody EnrollmentDto enrollmentDto) {
        return ResponseEntity.ok(enrollmentService.updateEnrollment(enrollmentDto));
    }

    @DeleteMapping("/deleteEnrollment")
    public ResponseEntity<String> deleteEnrollment(@RequestParam long enrollmentId) {
        enrollmentService.deleteEnrollment(enrollmentId);
        return ResponseEntity.ok("Enrollment deleted successfully");
    }

    @GetMapping("/viewSubjectById")
    public ResponseEntity<SubjectDto> viewSubjectById(@RequestParam long subjectId) {

        return ResponseEntity.ok(enrollmentService.viewSubjectById(subjectId));
    }

}
