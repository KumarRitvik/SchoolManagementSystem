package com.ritvik.school.student_service.controllers;

import com.ritvik.school.student_service.dtos.ClassRoomDto;
import com.ritvik.school.student_service.dtos.TeacherDto;
import com.ritvik.school.student_service.pojos.FileResponse;
import com.ritvik.school.student_service.dtos.StudentDto;
import com.ritvik.school.student_service.enums.*;
import com.ritvik.school.student_service.producer.StudentEventProducer;
import com.ritvik.school.student_service.service.StudentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController {

    @Autowired
    StudentEventProducer studentEventProducer;

    private StudentServiceImpl studentServiceImpl;
    StudentController(StudentServiceImpl studentServiceImpl) {
        this.studentServiceImpl = studentServiceImpl;
    }


    @GetMapping("/viewAllStudents")
    public List<StudentDto> viewAllStudents() {

        return studentServiceImpl.viewAllStudents();
    }


    // View All students Pagewise
    @GetMapping("/viewAllStudentsPagewise")
    public ResponseEntity<Page<StudentDto>> viewAllPagewise(

            @RequestParam int page,

            @RequestParam int size,

            @RequestParam(defaultValue = "studentId") String sortBy,

            @RequestParam(defaultValue = "asc") String direction){

        return ResponseEntity.ok(

                studentServiceImpl.viewAllPagewise(
                        page,
                        size,
                        sortBy,
                        direction));

    }

    // Create student
    @PostMapping("/create")
    public ResponseEntity<StudentDto> create(@RequestBody StudentDto studentDto) {

        StudentDto student = studentServiceImpl.create(studentDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(student);
    }

    // Update student
    @PutMapping("/update")
    public ResponseEntity<StudentDto> update(@RequestBody StudentDto studentDto) {

        StudentDto student = studentServiceImpl.update(studentDto);

        return ResponseEntity.ok(student);
    }

    // View student by ID
    @GetMapping("/viewById")
    public ResponseEntity<StudentDto> viewById(@RequestParam Long id) {

        StudentDto student = studentServiceImpl.viewById(id);

        return ResponseEntity.ok(student);
    }

    // Delete student
    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(@RequestParam Long id) {

        studentServiceImpl.delete(id);

        return ResponseEntity.ok("Student deleted successfully.");
    }


    //microservicecalls
    @GetMapping("/viewYourTeachersById")
    public ResponseEntity<TeacherDto> viewYourTeachersById( @RequestParam Long teacherId) {

        TeacherDto teacherDto = studentServiceImpl.viewYourTeachersById(teacherId);

        return ResponseEntity.ok(teacherDto);
    }
    @GetMapping("viewYourClassRoomById")
    public ResponseEntity<ClassRoomDto> viewYourClassRoomById(@RequestParam ("classRoomId")Long classRoomId) {
        return ResponseEntity.ok(studentServiceImpl.viewYourClassRoomById(classRoomId));
    }

    //SearchByName
    @GetMapping("/searchByName")
    public ResponseEntity<Page<StudentDto>> searchByName(@RequestParam String name,
                                                         @RequestParam int page,
                                                         @RequestParam int size,
                                                         @RequestParam String sortBy,
                                                         @RequestParam String direction){

        return ResponseEntity.ok(studentServiceImpl.searchByName(name, page, size, sortBy, direction));

    }

    //SearchByStudentType
    @GetMapping("/searchByStudentType")
        public ResponseEntity<Page<StudentDto>> searchByStudentType(@RequestParam StudentType studentType,
                                                                    @RequestParam int page,
                                                                    @RequestParam int size,
                                                                    @RequestParam String sortBy,
                                                                    @RequestParam String direction){
            return ResponseEntity.ok(
                    studentServiceImpl.searchByStudentType(studentType,page,size,sortBy,direction));
        }


            //endpoint to upload file.
    @PostMapping("/{id}/photo")
    public ResponseEntity<String> uploadPhoto(

            @PathVariable Long id,

            @RequestParam("photo") MultipartFile file){

        return ResponseEntity.ok(

                studentServiceImpl.uploadPhoto(id,file));

    }

    //view Student's phtoto endpoint
    @GetMapping("/{id}/photo")
    public ResponseEntity<Resource> viewPhoto(
            @PathVariable Long id) {

        FileResponse fileResponse = studentServiceImpl.viewPhoto(id);

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
    public ResponseEntity<StudentDto> deletePhoto(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                studentServiceImpl.deletePhoto(id));
    }
    //microservice to CrRUD TeacherPhoto

    @PostMapping("/teacher/{id}/photo")
    public ResponseEntity<String> uploadPhoto(@PathVariable long id,
                                              @RequestParam("photo") MultipartFile file){
        studentServiceImpl.uploadTeacherPhotoById(id,file);
        return ResponseEntity.status(HttpStatus.CREATED).body("Teacher uploaded successfully.");
    }
    @GetMapping("/teacher/{id}/photo")
    public ResponseEntity<Resource> viewTeacherPhotoById(@PathVariable long id) {
    return    studentServiceImpl.viewTeacherPhotoById(id);
    }

    @DeleteMapping("/teacher/{id}/photo")
    public ResponseEntity<String> deleteTeacherPhoto(@PathVariable("id") long id) {
        studentServiceImpl.deleteTeacherPhoto(id);
        return ResponseEntity.ok("Teacher photo deleted successfully");

    }

}

