package com.ritvik.school.student_service.dtos;

import com.ritvik.school.student_service.enums.SubjectType;
import lombok.Data;

import javax.security.auth.Subject;

@Data
    public class SubjectDto {

        private Long subjectId;
        private SubjectType subjectName;
        private String subjectCode;
        private Integer theoryMarks;
        private Integer practicalMarks;
    }
