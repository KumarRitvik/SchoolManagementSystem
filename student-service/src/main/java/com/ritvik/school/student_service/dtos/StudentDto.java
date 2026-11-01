package com.ritvik.school.student_service.dtos;

import com.ritvik.school.student_service.enums.*;
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
    @NotBlank
    private String guardianPhoneNumber;

    private String photo;

    @Positive
    @NotNull
    private double semFees;

    @Positive
    @NotNull
    private double hostelerFees;

    @Positive
    @NotNull
    private double totalCalculatedFees;


    @NotNull
    private StudentType studentType;


}