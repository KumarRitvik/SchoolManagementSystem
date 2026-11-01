package com.ritvik.school.student_service.dtos;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class EnrollmentDto {

        private Long enrollmentId;

        private Long studentId;

        private Long subjectId;

        private Long classId;

        @Max(value = 2026, message = "Session year must be less than or equal to 2023")
        @Min(value = 2004, message = "Session year must be greater than or equal to 2004")
        private Integer sessionYear;

        private Boolean active;

        private Long scoredTheoryMarks;

        private Long scoredPracticeMarks;

    }


