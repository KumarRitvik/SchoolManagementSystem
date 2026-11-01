package com.ritvik.school.teacher_service.services;

import com.ritvik.school.teacher_service.dtos.StudentDto;
import com.ritvik.school.teacher_service.pojos.FileResponse;
import com.ritvik.school.teacher_service.dtos.TeacherDto;
import com.ritvik.school.teacher_service.enums.TeacherType;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface TeacherServiceImpl {

    Page<TeacherDto> viewAllPagewise(int page, int size, String sortBy, String direction);
    TeacherDto create(TeacherDto teacherDto);
    TeacherDto update(TeacherDto teacherDto);
    TeacherDto viewById(Long id);
    void delete(long id);



     Page<TeacherDto> searchByName(String name, int page, int size, String sortBy, String direction);
     List<TeacherDto> searchByTeacherType(TeacherType teacherType);


    String uploadTeacherPhoto(Long id, MultipartFile file);
    FileResponse viewPhoto(Long id);
    TeacherDto deletePhoto(Long id);

    // micro services to CRUD student
    StudentDto getStudentById(Long studentId);

    StudentDto createStudent(StudentDto studentDto);

    StudentDto updateStudent(StudentDto studentDto);

    void deleteStudent(Long studentId);


    //MicroService to CRUD student photos
    String uploadStudentPhoto(Long id, MultipartFile file) throws IOException;

    FileResponse viewStudentPhoto(Long id);

    String deleteStudentPhoto(Long id);
}
