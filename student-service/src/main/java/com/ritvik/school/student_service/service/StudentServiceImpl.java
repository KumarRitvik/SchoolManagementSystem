package com.ritvik.school.student_service.service;

import com.ritvik.school.student_service.clients.AcademicClient;
import com.ritvik.school.student_service.clients.TeacherClient;
import com.ritvik.school.student_service.configurations.FeesConfig;
import com.ritvik.school.student_service.dtos.ClassRoomDto;
import com.ritvik.school.student_service.dtos.StudentDto;
import com.ritvik.school.student_service.dtos.TeacherDto;
import com.ritvik.school.student_service.entities.Student;
import com.ritvik.school.student_service.enums.StudentType;
import com.ritvik.school.student_service.exceptions.FileStorageException;
import com.ritvik.school.student_service.exceptions.ResourceNotFoundException;
import com.ritvik.school.student_service.exceptions.StudentAlreadyExistsException;
import com.ritvik.school.student_service.exceptions.StudentNotFoundException;
import com.ritvik.school.student_service.kafka.events.StudentCreatedEvent;
import com.ritvik.school.student_service.producer.StudentEventProducer;
import com.ritvik.school.student_service.pojos.FileResponse;
import com.ritvik.school.student_service.repositoriy.ProcessedEventRepo;
import com.ritvik.school.student_service.repositoriy.StudentRepo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
   public class StudentServiceImpl implements StudentService {


        @Value("${file.upload-dir}")
        private String uploadDir;

        private final StudentRepo studentRepo;
        private final FeesConfig feesConfig;
        private final TeacherClient teacherClient;
        private final AcademicClient academicClient;
        private final StudentEventProducer  studentEventProducer;
        private final ProcessedEventRepo processedEventRepo;

    //Constructor based initialization of StudentRepo
        public StudentServiceImpl(StudentRepo studentRepo, FeesConfig feesConfig, TeacherClient teacherClient,
                                  AcademicClient academicClient, StudentEventProducer  studentEventProducer, ProcessedEventRepo processedEventRepo) {
            this.studentRepo = studentRepo;
            this.feesConfig = feesConfig;
            this.teacherClient = teacherClient;
            this.academicClient = academicClient;
            this.studentEventProducer = studentEventProducer;
            this.processedEventRepo = processedEventRepo;
        }


        // ================= DTO to -> ENTITY =================

        private Student toEntity(StudentDto studentDto) {

            Student student = new Student();

            student.setStudentId(studentDto.getStudentId());
            student.setName(studentDto.getName());
            student.setEmail(studentDto.getEmail());
            student.setPhoneNumber(studentDto.getPhoneNumber());
            student.setGender(studentDto.getGender());
            student.setDateOfBirth(studentDto.getDateOfBirth());
            student.setAddress(studentDto.getAddress());
            student.setStudentType(studentDto.getStudentType());
            student.setPhoto(studentDto.getPhoto());
            student.setSemFees(studentDto.getSemFees());
            student.setHostelerFees(studentDto.getHostelerFees());
            student.setGuardianName(studentDto.getGuardianName());
            student.setGuardianPhoneNumber(studentDto.getGuardianPhoneNumber());
            student.setTotalCalculatedFees(studentDto.getTotalCalculatedFees());



            return student;
        }

        // ================= ENTITY -> DTO =================

        private StudentDto toDto(Student student) {

            StudentDto studentDto = new StudentDto();

            studentDto.setStudentId(student.getStudentId());
            studentDto.setName(student.getName());
            studentDto.setEmail(student.getEmail());
            studentDto.setPhoneNumber(student.getPhoneNumber());
            studentDto.setGender(student.getGender());
            studentDto.setDateOfBirth(student.getDateOfBirth());
            studentDto.setAddress(student.getAddress());
            studentDto.setStudentType(student.getStudentType());
            studentDto.setPhoto(student.getPhoto());
            studentDto.setSemFees(student.getSemFees());
            studentDto.setHostelerFees(student.getHostelerFees());
            studentDto.setGuardianName(student.getGuardianName());
            studentDto.setGuardianPhoneNumber(student.getGuardianPhoneNumber());
            studentDto.setTotalCalculatedFees(student.getTotalCalculatedFees());

            return studentDto;
        }

        // ================= VIEW ALL =================

        @Override
        public List<StudentDto> viewAllStudents() {
            System.out.println("========== DATABASE CHECK ==========");
            System.out.println("DATABASE = " + studentRepo.getCurrentDatabase());
            System.out.println("STUDENT COUNT = " + studentRepo.count());
            System.out.println("===================================");

            return studentRepo.findAll().stream()
                    .map(this::toDto)
                    .toList();
        }


        // PageWise viewAll
        public Page<StudentDto> viewAllPagewise(


                int page,

                int size,

                String sortBy,

                String direction) {


            Sort sort = direction.equalsIgnoreCase("desc")

                    ? Sort.by(sortBy).descending()

                    : Sort.by(sortBy).ascending();

            Pageable pageable = PageRequest.of(page, size, sort);

            Page<Student> studentPage = studentRepo.findAll(pageable);

            Page<StudentDto> dtoPage =
                    studentPage.map(this::toDto);

            return dtoPage;
        }


        // ================= CREATE =================

        @Override
        public StudentDto create(StudentDto studentDto) {

            if (studentRepo.findByEmail(studentDto.getEmail()).isPresent()) {
                throw new StudentAlreadyExistsException(
                        "Student already exists with email : "
                                + studentDto.getEmail());
            }

            if (studentRepo.findByPhoneNumber(studentDto.getPhoneNumber()).isPresent()) {
                throw new StudentAlreadyExistsException(
                        "Student already exists with number : "
                                + studentDto.getPhoneNumber());
            }


            Student student= new Student();

            if(studentDto.getStudentType().equals(StudentType.HOSTELER)){
                studentDto.setTotalCalculatedFees(feesConfig.hostelerFeesCalculator().calculateFees(studentDto));
            }

            else if(studentDto.getStudentType().equals(StudentType.DAYSCHOLAR)) {
                studentDto.setTotalCalculatedFees(feesConfig.dayScholarFeesCalculator().calculateFees(studentDto));
            }

            System.out.println("========== CREATE START ==========");
            Student savedStudent = studentRepo.save(this.toEntity(studentDto));


            System.out.println("========== STUDENT SAVED ==========");
            System.out.println("Student ID = " + savedStudent.getStudentId());

            StudentCreatedEvent event = new StudentCreatedEvent(
                    UUID.randomUUID().toString(),
                    savedStudent.getStudentId(),
                    savedStudent.getName(),
                    savedStudent.getEmail(),
                    savedStudent.getPhoneNumber()
            );

            System.out.println("========== EVENT CREATED ==========");
            System.out.println("Event ID = " + event.getEventId());

            studentEventProducer.sendStudentCreatedEvent(event);

            System.out.println("========== EVENT SEND CALLED ==========");

            return this.toDto(savedStudent);
        }

        // ================= UPDATE =================

        @Override
        public StudentDto update(StudentDto studentDto) {

            Student student = studentRepo.findById(studentDto.getStudentId())
                    .orElseThrow(() ->
                            new StudentNotFoundException(
                                    "student not found with id : "
                                            + studentDto.getStudentId()));

            student.setStudentId(studentDto.getStudentId());
            student.setName(studentDto.getName());
            student.setEmail(studentDto.getEmail());
            student.setPhoneNumber(studentDto.getPhoneNumber());
            student.setGender(studentDto.getGender());
            student.setDateOfBirth(studentDto.getDateOfBirth());
            student.setAddress(studentDto.getAddress());
            student.setStudentType(studentDto.getStudentType());
            student.setPhoto(studentDto.getPhoto());
            student.setSemFees(studentDto.getSemFees());
            student.setHostelerFees(studentDto.getHostelerFees());
            student.setGuardianName(studentDto.getGuardianName());
            student.setGuardianPhoneNumber(studentDto.getGuardianPhoneNumber());

            if(studentDto.getStudentType().equals(StudentType.HOSTELER)){
                student.setTotalCalculatedFees(feesConfig.hostelerFeesCalculator().calculateFees(studentDto));
            }

            else if(studentDto.getStudentType().equals(StudentType.DAYSCHOLAR)) {
                student.setTotalCalculatedFees(feesConfig.dayScholarFeesCalculator().calculateFees(studentDto));
            }

            return this.toDto(studentRepo.save(student));
        }
        // ================= VIEW BY ID =================

        @Override
        public StudentDto viewById(Long id) {

            Student student = studentRepo.findById(id)
                    .orElseThrow(() ->
                            new StudentNotFoundException(
                                    "Student not found with id : " + id));

            return toDto(student);
        }

        // ================= DELETE =================

        @Override
        public void delete(long id) {

            Student student = studentRepo.findById(id)
                    .orElseThrow(() ->
                            new StudentNotFoundException(
                                    "Student not found with id : " + id));

            studentRepo.delete(student);

        }

        @Override
        public Page<StudentDto> searchByName(String name, int page, int size, String sortBy,
                                             String direction) {


            Sort sort = direction.equalsIgnoreCase("desc")
                    ? Sort.by(sortBy).descending()
                    : Sort.by(sortBy).ascending();

            Pageable pageable = PageRequest.of(page, size, sort);

            Page<Student> studentPage =
                    studentRepo.findByNameContainingIgnoreCase(name, pageable);

            Page<StudentDto> sortedSearchedStudent = studentPage.map((Student student) ->
                    this.toDto(student));

            return sortedSearchedStudent;
        }


        @Override
        public Page<StudentDto> searchByStudentType(StudentType studentType, int page, int size,
                                                    String sortBy, String direction) {
            Sort sort = direction.equalsIgnoreCase("desc")
                    ? Sort.by(sortBy).descending()
                    : Sort.by(sortBy).ascending();

            Pageable pageable = PageRequest.of(page, size, sort);

            Page<Student> studentPage =
                    studentRepo.findByStudentType(studentType, pageable);

            Page<StudentDto> sortedSearchedStudent = studentPage.map((Student student) ->
                    this.toDto(student));

            return sortedSearchedStudent;
        }

        @Override
        public String uploadPhoto(Long id, MultipartFile file) {

        Student student = studentRepo.findById(id)
                .orElseThrow(() -> new StudentNotFoundException("Student not found with id: " + id));

        if (file.isEmpty()) {
            throw new FileStorageException("Please select a file.", null);
        }

        try {
            // while configuring yml we set value of upload folder as "uploads/Students". And here
            //we just initialised uploadDir variable with this string(upload/Students)... this doesnot means
            //that we created a new folder. Application.yml only stores the configuration value.
            // It does NOT create the folder."file.upload-dir" only means:- "Whenever someone asks for
            // file.upload-dir, return the string uploads/Students."

            //means create a new folder if not present
            Files.createDirectories(Paths.get(uploadDir));

            //upload mew photo first
            String newFileName =
                    UUID.randomUUID() + "_" + file.getOriginalFilename();

            Path newPhotoPath = Paths.get(uploadDir, newFileName);
            Files.copy(file.getInputStream(), newPhotoPath);

            // Delete OLD photo if it exists
            if (student.getPhoto() != null &&
                    !student.getPhoto().isBlank()) {

                Path oldPhotoPath =
                        Paths.get(uploadDir, student.getPhoto());

                Files.deleteIfExists(oldPhotoPath);
            }

            // Update database
            student.setPhoto(newFileName);

            Student savedStudent =
                    studentRepo.save(student);

              this.toDto(savedStudent);
              return ("student's photo saved sucessfully");

        } catch (IOException e) {
            throw new RuntimeException("File upload failed.", e);
        }
    }


    @Override
    public FileResponse viewPhoto(Long id) {

        Student student = studentRepo.findById(id)
                .orElseThrow(() ->
                        new StudentNotFoundException("Student not found with id: " + id));

        if (student.getPhoto() == null || student.getPhoto().isBlank()) {
            throw new ResourceNotFoundException("No photo uploaded for this student.");
        }

        Path photoPath = Paths.get(uploadDir, student.getPhoto());

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
    public StudentDto deletePhoto(Long id) {

        Student student = studentRepo.findById(id)
                .orElseThrow(() ->
                        new StudentNotFoundException(
                                "Student not found with id : " + id));

        if (student.getPhoto() == null || student.getPhoto().isBlank()) {
            throw new ResourceNotFoundException(
                    "No photo found for Student with id : " + id);
        }

        try {

            Path photoPath = Paths.get(uploadDir, student.getPhoto());

            Files.deleteIfExists(photoPath);

            student.setPhoto(null);

            Student savedStudent = studentRepo.save(student);

            return this.toDto(savedStudent);

        } catch (IOException e) {

            throw new FileStorageException(
                    "Unable to delete photo.",
                    e);

        }
    }


    @Override
    public TeacherDto viewYourTeachersById(long teacherId) {
        return teacherClient.viewYourTeacherById(teacherId);
    }

    @Override
    public ClassRoomDto viewYourClassRoomById(long classRoomId) {
        return academicClient.viewYourClassRoomById(classRoomId);
    }

    // microservice to CRUD TeacherPhoto
    @Override
    public String uploadTeacherPhotoById(long id, MultipartFile file) {
            return teacherClient.uploadTeacherPhoto(id, file);
    }
    
    @Override
    public ResponseEntity<Resource> viewTeacherPhotoById(long id) {
        return teacherClient
                .viewTeacherPhotoById(id);
    }
    @Override
    public void deleteTeacherPhoto(long id) {
            teacherClient.deleteTeacherPhotoById(id);
    }


}




