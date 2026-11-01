package com.ritvik.school.teacher_service.services;


import com.ritvik.school.teacher_service.clients.StudentClient;
import com.ritvik.school.teacher_service.dtos.StudentDto;
import com.ritvik.school.teacher_service.pojos.FileResponse;
import com.ritvik.school.teacher_service.dtos.TeacherDto;
import com.ritvik.school.teacher_service.entities.Teacher;
import com.ritvik.school.teacher_service.enums.TeacherType;
import com.ritvik.school.teacher_service.exceptions.FileStorageException;
import com.ritvik.school.teacher_service.exceptions.ResourceNotFoundException;
import com.ritvik.school.teacher_service.exceptions.TeacherAlreadyExistsException;
import com.ritvik.school.teacher_service.exceptions.TeacherNotFoundException;
import com.ritvik.school.teacher_service.repositories.TeacherRepo;
import org.apache.catalina.webresources.FileResource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;


@Service
public class TeacherService implements TeacherServiceImpl {

        @Value("${file.upload-dir}")
        private String uploadDir;

        private final TeacherRepo teacherRepo;
        private final StudentClient studentClient;
        //Constructor based initialization of TeacherRepo
        public TeacherService(TeacherRepo teacherRepo, StudentClient studentClient){
            this.teacherRepo = teacherRepo;
            this.studentClient = studentClient;
        }


        // ================= DTO to -> ENTITY =================

        private Teacher toEntity(TeacherDto teacherDto) {

            Teacher teacher = new Teacher();

            teacher.setTeacherId(teacherDto.getTeacherId());
            teacher.setName(teacherDto.getName());
            teacher.setEmail(teacherDto.getEmail());
            teacher.setPhoneNumber(teacherDto.getPhoneNumber());
            teacher.setSalary(teacherDto.getSalary());
            teacher.setTeacherType(teacherDto.getTeacherType());
            teacher.setPhoto(teacherDto.getPhoto());


            return teacher;
        }

        // ================= ENTITY -> DTO =================

        private TeacherDto toDto(Teacher teacher) {

            TeacherDto teacherDto = new TeacherDto();

            teacherDto.setTeacherId(teacher.getTeacherId());
            teacherDto.setName(teacher.getName());
            teacherDto.setEmail(teacher.getEmail());
            teacherDto.setPhoneNumber(teacher.getPhoneNumber());
            teacherDto.setSalary(teacher.getSalary());
            teacherDto.setTeacherType(teacher.getTeacherType());
            teacherDto.setPhoto(teacher.getPhoto());

            return teacherDto;
        }

        // ================= VIEW ALL PAGEWISE=================

     public Page<TeacherDto> viewAllPagewise(

                int page,

                int size,

                String sortBy,

                String direction) {

            Sort sort = direction.equalsIgnoreCase("desc")

                    ? Sort.by(sortBy).descending()

                    : Sort.by(sortBy).ascending();

            Pageable pageable = PageRequest.of(page, size, sort);

            Page<Teacher> teacherPage = teacherRepo.findAll(pageable);

            Page<TeacherDto> dtoPage =
                    teacherPage.map(teacher ->this.toDto(teacher));
            return dtoPage;
        }


        // ================= CREATE =================

        @Override
        public TeacherDto create(TeacherDto teacherDto) {


            if (teacherRepo.findByEmail(teacherDto.getEmail()).isPresent()) {
                throw new TeacherAlreadyExistsException(
                        "Teacher already exists with email : "
                                + teacherDto.getEmail());
            }

            if (teacherRepo.findByPhoneNumber(teacherDto.getPhoneNumber()).isPresent()) {
                throw new TeacherAlreadyExistsException(
                        "Teacher already exists with number : "
                                + teacherDto.getPhoneNumber());
            }


            return toDto(teacherRepo.save(toEntity(teacherDto)));
        }

        // ================= UPDATE =================

        @Override
        public TeacherDto update(TeacherDto teacherDto) {

            Teacher teacher = teacherRepo.findById(teacherDto.getTeacherId())
                    .orElseThrow(() ->
                            new TeacherNotFoundException(
                                    "Teacher not found with id : "
                                            + teacherDto.getTeacherId()));

            teacher.setName(teacherDto.getName());
            teacher.setEmail(teacherDto.getEmail());
            teacher.setPhoneNumber(teacherDto.getPhoneNumber());
            teacher.setTeacherType(teacherDto.getTeacherType());
            teacher.setSalary(teacherDto.getSalary());

            Teacher updatedTeacher = teacherRepo.save(teacher);

            return toDto(updatedTeacher);
        }
        // ================= VIEW BY ID =================

        @Override
        public TeacherDto viewById(Long id) {

            Teacher teacher = teacherRepo.findById(id)
                    .orElseThrow(() ->
                            new TeacherNotFoundException(
                                    "Teacher not found with id : " + id));

            return toDto(teacher);
        }

        // ================= DELETE =================

        @Override
        public void delete(long id) {

            Teacher teacher = teacherRepo.findById(id)
                    .orElseThrow(() ->
                            new TeacherNotFoundException(
                                    "Teacher not found with id : " + id));

            teacherRepo.delete(teacher);

        }
        @Override
        public Page<TeacherDto> searchByName(String name, int page, int size, String sortBy, String direction) {

            Sort sort = direction.equalsIgnoreCase("desc")
                    ? Sort.by(sortBy).descending()
                    : Sort.by(sortBy).ascending();

            Pageable pageable = PageRequest.of(page, size, sort);

            Page<Teacher> teacherPage =
                    teacherRepo.findByNameContainingIgnoreCase(name, pageable);

            Page<TeacherDto> sortedSearchedTeacher= teacherPage.map((Teacher teacher) -> this.toDto(teacher));
            return sortedSearchedTeacher;
        }



        @Override
        public List<TeacherDto> searchByTeacherType(TeacherType teacherType) {
            List<Teacher> teachersByType=  teacherRepo.findByTeacherType(teacherType);
            List<TeacherDto> dtoByType= teachersByType.stream().map(teacherByType ->this.toDto(teacherByType))
            .toList();
            return dtoByType;
        }

        @Override
        public String uploadTeacherPhoto(Long id, MultipartFile file) {

            Teacher teacher = teacherRepo.findById(id)
                    .orElseThrow(() -> new TeacherNotFoundException("Teacher not found with id: " + id));

            if (file.isEmpty()) {
                throw new FileStorageException("Please select a file.", null);
            }

            try {
                // while configuring yml we set value of upload folder as "uploads/teachers". And here
                //we just initialised uploadDir variable with this string(upload/teachers)... this doesnot means
                //that we created a new folder. Application.yml only stores the configuration value.
                // It does NOT create the folder."file.upload-dir" only means:- "Whenever someone asks for
                // file.upload-dir, return the string uploads/teachers."

                //means create a new folder if not present
                Files.createDirectories(Paths.get(uploadDir));

                //upload mew photo first
                String newFileName =
                        UUID.randomUUID() + "_" + file.getOriginalFilename();

                Path newPhotoPath = Paths.get(uploadDir, newFileName);
                Files.copy(file.getInputStream(), newPhotoPath);

                // Delete OLD photo if it exists
                if (teacher.getPhoto() != null &&
                        !teacher.getPhoto().isBlank()) {

                    Path oldPhotoPath =
                            Paths.get(uploadDir, teacher.getPhoto());

                    Files.deleteIfExists(oldPhotoPath);
                }

                // Update database
                teacher.setPhoto(newFileName);

                Teacher savedTeacher =
                        teacherRepo.save(teacher);

                this.toDto(savedTeacher);
                return "Photo uploaded sucessfully";


            } catch (IOException e) {
                throw new RuntimeException("File upload failed.", e);
            }
        }


        @Override
        public FileResponse viewPhoto(Long id) {

            Teacher teacher = teacherRepo.findById(id)
                    .orElseThrow(() ->
                            new TeacherNotFoundException("Teacher not found with id: " + id));

            if (teacher.getPhoto() == null || teacher.getPhoto().isBlank()) {
                throw new ResourceNotFoundException("No photo uploaded for this teacher.");
            }

            Path photoPath = Paths.get(uploadDir, teacher.getPhoto());

            try {
                //Anologous to MultiPart... Resource is used to send file to browser just like
                //MultiPart is used to recieve file from the Browser.
                Resource resource = new UrlResource(photoPath.toUri());


                if (!resource.exists() || !resource.isReadable()) {
                    throw new ResourceNotFoundException("Photo not found.");
                }

                //this method (.probeContentType(path)) will return the filetype pdf,jpg,png,video,audio etc.
                String contentType = Files.probeContentType(photoPath);

                //this means that if there is no content type mentioned in the file tjen
                // instead of crashing out it still sends a content type:- application/octet-stream
                //i.e; unknown filetype will bw displayed @ browser
                if (contentType == null) {
                    contentType = "application/octet-stream";
                }

                return new FileResponse(resource, contentType);

            } catch (IOException e) {

                throw new FileStorageException("Unable to read photo.", e);

            }
        }
        @Override
        public TeacherDto deletePhoto(Long id) {

            Teacher teacher = teacherRepo.findById(id)
                    .orElseThrow(() ->
                            new TeacherNotFoundException(
                                    "Teacher not found with id : " + id));

            if (teacher.getPhoto() == null || teacher.getPhoto().isBlank()) {
                throw new ResourceNotFoundException(
                        "No photo found for teacher with id : " + id);
            }

            try {

                Path photoPath = Paths.get(uploadDir, teacher.getPhoto());

                Files.deleteIfExists(photoPath);

                teacher.setPhoto(null);

                Teacher savedTeacher = teacherRepo.save(teacher);

                return toDto(savedTeacher);

            } catch (IOException e) {

                throw new FileStorageException(
                        "Unable to delete photo.",
                        e);

            }
        }
        // micro services to CRUD students
        @Override
        public StudentDto getStudentById(Long studentId) {

            return studentClient.getStudentById(studentId);
        }
        @Override
        public StudentDto createStudent(StudentDto studentDto) {

        return studentClient.createStudent(studentDto);
         }

         @Override
         public StudentDto updateStudent(StudentDto studentDto) {

          return studentClient.updateStudent(studentDto);
          }

          @Override
          public void deleteStudent(Long studentId) {

            studentClient.deleteStudent(studentId);
          }

//Microservice to Upload,View and Delete student photo from teacher service

    @Override
          public String uploadStudentPhoto(Long id, MultipartFile file) throws IOException {
           return studentClient.uploadStudentPhoto(id, file);
    }

    @Override
    public FileResponse viewStudentPhoto(Long id) {
            return studentClient.viewStudentPhoto(id);
        }

        @Override
    public String deleteStudentPhoto(Long id) {
            return studentClient.deleteStudentPhoto(id);
        }


}


