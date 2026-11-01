package com.ritvik.school.sms_service.dtos;

import lombok.Data;

@Data
public class StudentDto {

    private Long studentId;
    private String name;
    private String email;
    private String phoneNumber;

}