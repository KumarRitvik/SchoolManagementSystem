package com.ritvik.school.teacher_service.dtos;

import com.ritvik.school.teacher_service.enums.Gender;
import com.ritvik.school.teacher_service.enums.StudentType;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
    public class StudentDto {

        private Long studentId;

        @NotBlank
        @Pattern(regexp = "^[A-Za-z ]+$", message = "Name must contain only letters and spaces")
        @Size(min=2, max=50)
        private String name;

        @NotBlank
        @Email
        private String email;

        @NotBlank
        @Pattern(
                regexp = "^[6-9]\\d{9}$",
                message = "Invalid phone number")
        private String phoneNumber;

        @NotBlank
        private Gender gender;

        @NotNull
        @Past
        private LocalDate dateOfBirth;

        @NotBlank
        private String address;

        @NotNull
        @Pattern(regexp = "^[A-Za-z ]+$", message = "Name must contain only letters and spaces")
        @Size(min=2, max=50)
        private String guardianName;

        @NotBlank
        @Pattern(
                regexp = "^[6-9]\\d{9}$",
                message = "Invalid phone number")
        private String guardianPhoneNumber;


        @NotBlank
        private String photo;

        @Positive
        @NotNull
        private Double semFees;

        @Positive
        @NotNull
        private Double hostelerFees;

        @Positive
        @NotNull
        private double totalCalculatedFees;


        @NotNull
        private StudentType studentType;



    }


