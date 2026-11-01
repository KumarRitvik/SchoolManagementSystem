package com.ritvik.school.academic_service.entities;

import com.ritvik.school.academic_service.enums.SubjectType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Table(name ="subject")
@Entity
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subject_id")
    private Long subjectId;

    @NotNull
    @Enumerated(EnumType.STRING)
    private SubjectType subjectName;

    @NotNull
    private String subjectCode;

    private Integer theoryMarks;

    private Integer practicalMarks;


}
