package com.ritvik.school.academic_service.entities;

import com.ritvik.school.academic_service.enums.Section;
import com.ritvik.school.academic_service.enums.Standard;
import jakarta.persistence.*;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
public class ClassRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "class_id")
    private Long classRoomId;

    @Enumerated(EnumType.STRING)
    private Standard standard;

    @Enumerated(EnumType.STRING)
    private Section section;

    @NotNull
    private String academicYear;

    @NotNull
    private Integer capacity;


}
