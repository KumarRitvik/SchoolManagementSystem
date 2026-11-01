
package com.ritvik.school.student_service.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "enrollment")
@Data
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "enrollment_id")
    private Long enrollmentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @Column
    private Integer sessionYear;

    @Column
    private Long scoredTheoryMarks;

    @Column
    private Long scoredPracticeMarks;

    @Column
    private Boolean active;

    // Enrollment table owns relationship between clasrroom
    // & subject of academic microservice. therefore enrollment
    // entity(table) will have classId & subjectId here(inter-servicereference)
    private Long subjectId;
    private Long classId;

    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;
}
