package com.ritvik.school.student_service.clients;

import com.ritvik.school.student_service.configurations.FeignConfig;
import com.ritvik.school.student_service.dtos.TeacherDto;
import com.ritvik.school.student_service.pojos.FileResponse;
import feign.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(name = "TEACHER-SERVICE",configuration = FeignConfig.class
)
public interface TeacherClient {

    @GetMapping("/teachers/viewById")
    TeacherDto viewYourTeacherById(@RequestParam Long id);


    // TeacherPhoto CRUD

    @PostMapping(value="/teachers/{id}/photo",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    String uploadTeacherPhoto(@PathVariable("id") Long id,@RequestPart("photo") MultipartFile file);

    @GetMapping("/teachers/{id}/photo")
    ResponseEntity<Resource> viewTeacherPhotoById(@RequestParam("id")Long id);

    @DeleteMapping("/teachers/{id}/photo")
    void deleteTeacherPhotoById(@PathVariable Long id);

}
