package com.ritvik.school.teacher_service.controllers;

import com.ritvik.school.teacher_service.dtos.StudentDto;
import com.ritvik.school.teacher_service.pojos.FileResponse;
import com.ritvik.school.teacher_service.dtos.TeacherDto;

import com.ritvik.school.teacher_service.enums.TeacherType;
import com.ritvik.school.teacher_service.services.TeacherService;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/teachers")
public class TeacherController {

    private final TeacherService teacherService;

    TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    // View All Teachers Pagewise
    @GetMapping("/viewAllTeachers")
    public ResponseEntity<Page<TeacherDto>> viewAllPagewise(

            @RequestParam int page,

            @RequestParam int size,

            @RequestParam(defaultValue = "teacherId") String sortBy,

            @RequestParam(defaultValue = "asc") String direction) {

        return ResponseEntity.ok(

                teacherService.viewAllPagewise(
                        page,
                        size,
                        sortBy,
                        direction));

    }

    // Create teacher
    @PostMapping("/create")
    public ResponseEntity<TeacherDto> create(@RequestBody TeacherDto teacherDto) {

        TeacherDto teacher = teacherService.create(teacherDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(teacher);
    }

    // Update teacher
    @PutMapping("/update")
    public ResponseEntity<TeacherDto> update(@RequestBody TeacherDto teacherDto) {

        TeacherDto teacher = teacherService.update(teacherDto);

        return ResponseEntity.ok(teacher);
    }

    // View teacher by ID
    @GetMapping("/viewById")
    public ResponseEntity<TeacherDto> viewById(@RequestParam Long id) {

        TeacherDto teacher = teacherService.viewById(id);

        return ResponseEntity.ok(teacher);
    }

    // Delete teacher
    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(@RequestParam Long id) {

        teacherService.delete(id);

        return ResponseEntity.ok("Teacher deleted successfully.");
    }

    //SearchByName
    @GetMapping("/searchByName")
    public ResponseEntity<Page<TeacherDto>> searchByName(@RequestParam String name,
                                                         @RequestParam int page,
                                                         @RequestParam int size,
                                                         @RequestParam String sortBy,
                                                         @RequestParam String direction) {

        return ResponseEntity.ok(teacherService.searchByName(name, page, size, sortBy, direction));
    }

    @GetMapping("/searchByTeacherType")
    public ResponseEntity<List<TeacherDto>> searchByTeacherType(
            @RequestParam TeacherType teacherType) {

        return ResponseEntity.ok(
                teacherService.searchByTeacherType(teacherType));
    }

    //endpoint to upload file.
    @PostMapping("/{id}/photo")
    public ResponseEntity<String> uploadPhoto(@PathVariable Long id,
                                              @RequestParam("photo") MultipartFile file) {
        teacherService.uploadTeacherPhoto(id, file);
        return ResponseEntity.ok("Photo successfully uploaded.");


    }

    @GetMapping("/{id}/photo")
    public ResponseEntity<Resource> viewPhoto(
            @PathVariable Long id) {

        FileResponse fileResponse = teacherService.viewPhoto(id);

        //(1).parseMediaType(contentType) converts THE String contentType into MediaType
        // object which is required by browser so that browser may understand which
        // type of data is coming as browser cant understand contentType, it understands only MediaTyype.
        //(2)we added a header as headers.setContentType() which will add a header named ContentType
        // in the Resource that we are sending to the browser,as MediaType is present under the header ContentType..

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(
                MediaType.parseMediaType(fileResponse.getContentType()));

        //we Return Response(file present here like photo, pdf etc),Header which has MEDIATYPE so that browser
        //which type of mediafile has come, and also status as ok... All three packed in the ResponseEntity object.
        return ResponseEntity
                .ok()
                .headers(headers)
                .body(fileResponse.getResource());
    }

    @DeleteMapping("/{id}/photo")
    public ResponseEntity<TeacherDto> deletePhoto(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                teacherService.deletePhoto(id));
    }

    // micro services
    @GetMapping("/student/{studentId}")
    public ResponseEntity<StudentDto> getStudent(
            @PathVariable Long studentId) {

        StudentDto studentDto = teacherService.getStudentById(studentId);

        return ResponseEntity.ok(studentDto);
    }

    @PostMapping("/student")
    public ResponseEntity<StudentDto> createStudent(
            @RequestBody StudentDto studentDto) {

        StudentDto student =
                teacherService.createStudent(studentDto);

        return ResponseEntity.ok(student);
    }

    @PutMapping("/student")
    public ResponseEntity<StudentDto> updateStudent(
            @RequestBody StudentDto studentDto) {

        return ResponseEntity.ok(teacherService.updateStudent(studentDto));

    }

    @DeleteMapping("/student/{studentId}")
    public ResponseEntity<String> deleteStudent(
            @PathVariable Long studentId) {

        teacherService.deleteStudent(studentId);

        return ResponseEntity.ok("Student deleted successfully.");
    }

    // Microservice to CRUD student's photo from teacher's side.

    @PostMapping("student/{id}/photo")
    public ResponseEntity<String> uploadStudentPhoto(@PathVariable Long id,
                                                     @RequestParam("photo") MultipartFile file) throws IOException {

        teacherService.uploadStudentPhoto(id, file);
        return ResponseEntity.ok("photo is uploaded successfully");

    }

    @GetMapping("student/{id}/photo")
    public ResponseEntity<Resource> viewStudentPhoto(@PathVariable Long id) {


        FileResponse fileResponse = teacherService.viewStudentPhoto(id);

        //(1).parseMediaType(contentType) converts THE String contentType into MediaType
        // object which is required by browser so that browser may understand which
        // type of data is coming as browser cant understand contentType, it understands only MediaTyype.
        //(2)we added a header as headers.setContentType() which will add a header named ContentType
        // in the Resource that we are sending to the browser,as MediaType is present under the header ContentType..

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(
                MediaType.parseMediaType(fileResponse.getContentType()));

        //we Return Response(file present here like photo, pdf etc),Header which has MEDIATYPE so that browser
        //which type of mediafile has come, and also status as ok... All three packed in the ResponseEntity object.
        return ResponseEntity
                .ok()
                .headers(headers)
                .body(fileResponse.getResource());

    }

    @DeleteMapping("student/{id}/photo")
    public ResponseEntity<String> deleteStudentPhoto(
            @PathVariable Long id) {

        teacherService.deleteStudentPhoto(id);

        return ResponseEntity.ok("photo is deleted");

    }
}

