package com.ritvik.school.academic_service.services;

import com.ritvik.school.academic_service.dtos.SubjectDto;
import org.springframework.data.domain.Page;

public interface SubjectServiceImpl {
    SubjectDto createSubject(SubjectDto subjectDto);

    Page<SubjectDto> viewAllSubjects(int page, int size, String sortBy,
                                     String direction);

    SubjectDto viewSubjectById(long subjectId);

    SubjectDto updateSubject(SubjectDto subjectDto);

    void deleteSubject(long subjectId);
}
