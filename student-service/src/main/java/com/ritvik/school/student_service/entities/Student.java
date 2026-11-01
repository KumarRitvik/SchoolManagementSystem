package com.ritvik.school.student_service.entities;

import com.ritvik.school.student_service.enums.Gender;
import com.ritvik.school.student_service.enums.StudentType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_id")
    private Long studentId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "phone_number", nullable = false, unique = true, length = 10)
    private String phoneNumber;

    @Column
    private LocalDate dateOfBirth;

    @Column
    private String guardianName;

    @Column
    private String guardianPhoneNumber;

    @Enumerated(EnumType.STRING)
    @Column
    private Gender gender;


    @Enumerated(EnumType.STRING)
    @Column
    private StudentType studentType;

    @Column
    private String address;

    @Column
    private String photo;

    @Column
    private Double semFees;

    @Column
    private Double hostelerFees;

    @Column
    private Double totalCalculatedFees;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "student")
     List<Enrollment> enrollments= new ArrayList<>();

}

