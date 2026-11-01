package com.ritvik.school.student_service.service;

import com.ritvik.school.student_service.dtos.ClassRoomDto;
import com.ritvik.school.student_service.dtos.TeacherDto;
import com.ritvik.school.student_service.pojos.FileResponse;
import com.ritvik.school.student_service.enums.StudentType;
import com.ritvik.school.student_service.dtos.StudentDto;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpEntity;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public interface StudentService {

     Page<StudentDto> viewAllPagewise(

            int page,

            int size,

            String sortBy,

            String direction);

    StudentDto create(StudentDto studentDto);

    StudentDto update(StudentDto studentDto);

    StudentDto viewById(Long id);

    void delete(long id);



    public Page<StudentDto> searchByName(String name, int page, int size, String sortBy, String direction);
    Page<StudentDto> searchByStudentType(StudentType studentType, int page, int size,
                                         String sortBy, String direction);


    String uploadPhoto(Long id, MultipartFile file);
    FileResponse viewPhoto(Long id);
    StudentDto deletePhoto(Long id);


    TeacherDto viewYourTeachersById(long teacherId);

    ClassRoomDto viewYourClassRoomById(long classRoomId);

    // microservice to CRUD TeacherPhoto
    String uploadTeacherPhotoById(long id, MultipartFile file);

    HttpEntity<Resource> viewTeacherPhotoById(long id);

    void deleteTeacherPhoto(long id);

    List<StudentDto> viewAllStudents();
}
