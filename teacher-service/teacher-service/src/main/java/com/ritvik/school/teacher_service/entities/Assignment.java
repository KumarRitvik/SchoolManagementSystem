package com.ritvik.school.teacher_service.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Assignment {

            @Id
            @GeneratedValue(strategy = GenerationType.IDENTITY)
            private Long assignmentId;

            private Integer academicYear;

            private Boolean active;

            @ManyToOne
            @JoinColumn(name = "teacher_id")
            private Teacher teacher;


    // Assignment table owns relationship between clasrroom
    // & subject of academic microservice. therefore Assignment
    // entity(table) will have classId & subjectId here(inter-servicereference)
            private Long subjecId;
            private Long classId;

        }


