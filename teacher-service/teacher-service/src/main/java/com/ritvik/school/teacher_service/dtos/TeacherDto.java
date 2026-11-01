package com.ritvik.school.teacher_service.dtos;


import com.ritvik.school.teacher_service.enums.TeacherType;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class TeacherDto {

    private Long teacherId;


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


    @NotNull
    private TeacherType teacherType;


    @Min(50000)
    @Max(150000)
    private double salary;

    @NotBlank
    @NotNull
    private String photo;


}