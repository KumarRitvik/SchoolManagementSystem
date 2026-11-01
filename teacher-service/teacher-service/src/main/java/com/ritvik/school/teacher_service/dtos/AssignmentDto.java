package com.ritvik.school.teacher_service.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class AssignmentDto {

    private Long assignmentId;

    @PastOrPresent
    @Min(value = 2020, message = "Academic year must be greater than or equal to 2000")
    private Integer academicYear;

    @Pattern(regexp = "^(true|false)$", message = "Active must be true or false")
    private Boolean active;

    private Long teacherId;

    private Long subjectId;

    private Long classId;



}
