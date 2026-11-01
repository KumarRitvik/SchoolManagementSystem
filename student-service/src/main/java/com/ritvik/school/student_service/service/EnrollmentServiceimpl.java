package com.ritvik.school.student_service.service;

import com.ritvik.school.student_service.dtos.EnrollmentDto;
import com.ritvik.school.student_service.dtos.SubjectDto;
import org.springframework.data.domain.Page;

public interface EnrollmentServiceimpl {
    EnrollmentDto createEnrollment(EnrollmentDto enrollmentDto);

    Page<EnrollmentDto> viewAllEnrollments(int page, int size, String sortBy, String direction);

    EnrollmentDto updateEnrollment(EnrollmentDto enrollmentDto);

    void deleteEnrollment(long enrollmentId);

    EnrollmentDto viewEnrollmentById(long enrollmentId);

    SubjectDto viewSubjectById(long subjectId);
}
