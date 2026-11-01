package com.ritvik.school.teacher_service.services;

import com.ritvik.school.teacher_service.dtos.AssignmentDto;
import org.springframework.data.domain.Page;

public interface AssignmentServiceImpl {
    AssignmentDto createAssignment(AssignmentDto assignmentDto);

    Page<AssignmentDto> viewAllAssignment(int page, int size, String sortBy,
                                          String direction);

    AssignmentDto viewAssignmentById(long assignmentId);

    AssignmentDto updateAssignment(AssignmentDto assignmentDto);

    void deleteAssignment(long assignmentId);
}
