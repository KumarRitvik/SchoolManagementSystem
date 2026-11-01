package com.ritvik.school.sms_service.clients;

import com.ritvik.school.sms_service.dtos.StudentDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.cloud.openfeign.FeignClient;


import java.util.List;

@FeignClient(name = "STUDENT-SERVICE")
public interface StudentClient {

    @GetMapping("/students/viewAllStudents")
    List<StudentDto> viewAllStudents();

}