package com.ritvik.school.student_service.dtos;

import com.ritvik.school.student_service.enums.TeacherType;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class TeacherDto {

    private Long teacherId;


    private String name;


    private String email;


    private String phoneNumber;

    private String photo;
}