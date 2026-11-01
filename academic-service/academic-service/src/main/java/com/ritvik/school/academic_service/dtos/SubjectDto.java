package com.ritvik.school.academic_service.dtos;

import com.ritvik.school.academic_service.entities.Subject;

import com.ritvik.school.academic_service.enums.SubjectType;
import jakarta.persistence.*;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data

public class SubjectDto {

    private Long subjectId;

    @Enumerated(EnumType.STRING)
    private SubjectType subjectName;

    @Pattern(regexp = "^[A-Z]{3}[0-9]{3}$", message = "Subject code must be in the format ABC123")
    private String subjectCode;

    @Min(0)
    @Max(80)
    private Integer theoryMarks;

    @Min(0)
    @Max(20)
    private Integer practicalMarks;
}
