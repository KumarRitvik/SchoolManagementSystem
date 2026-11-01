package com.ritvik.school.teacher_service.clients;


import com.ritvik.school.teacher_service.dtos.StudentDto;
import com.ritvik.school.teacher_service.pojos.FileResponse;
import org.apache.catalina.webresources.FileResource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
    public class StudentClient {

    private final RestTemplate restTemplate;

    public StudentClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public StudentDto getStudentById(Long studentId) {

        return restTemplate.getForObject(
                "http://STUDENT-SERVICE/students/viewById?id=" + studentId,
                StudentDto.class
        );
    }

    public StudentDto createStudent(StudentDto studentDto) {

        return restTemplate.postForObject(
                "http://STUDENT-SERVICE/students/create",
                studentDto,
                StudentDto.class);
    }


    public StudentDto updateStudent(StudentDto studentDto) {

        return restTemplate.exchange(
                "http://STUDENT-SERVICE/students/update",
                HttpMethod.PUT,
                new HttpEntity<>(studentDto),
                StudentDto.class
        ).getBody();
    }


    public void deleteStudent(Long studentId) {

        restTemplate.delete("http://STUDENT-SERVICE/students/delete?id=" + studentId);
    }

    //students photos CRUD
    public String uploadStudentPhoto(@PathVariable Long id,

                                     @RequestParam("photo") MultipartFile file) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("photo", new ByteArrayResource(file.getBytes()) {@Override
                                             public String getFilename()
                                            {return file.getOriginalFilename();}});

        HttpEntity<MultiValueMap<String, Object>> request =
                new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(
                "http://STUDENT-SERVICE/students/"+id+"/photo", request, String.class);
        return response.getBody();
    }


        public FileResponse viewStudentPhoto(Long id) {

            ResponseEntity<Resource> response =
                    restTemplate.getForEntity(
                            "http://STUDENT-SERVICE/students/"+ id +"/photo",
                            Resource.class
                    );

            MediaType contentType = response.getHeaders().getContentType();

            String contentTypeString =
                    contentType != null ? contentType.toString() : null;

            return new FileResponse(
                    response.getBody(),
                    contentTypeString);
        }

    public String deleteStudentPhoto(@PathVariable Long id) {
        restTemplate.delete(
                "http://STUDENT-SERVICE/students/"+id+"/photo");
        return "Photo deleted successfully";
    }
}