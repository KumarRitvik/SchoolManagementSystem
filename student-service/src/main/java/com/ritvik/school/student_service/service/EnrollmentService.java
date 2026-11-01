package com.ritvik.school.student_service.service;

import com.ritvik.school.student_service.clients.AcademicClient;
import com.ritvik.school.student_service.dtos.EnrollmentDto;
import com.ritvik.school.student_service.dtos.SubjectDto;
import com.ritvik.school.student_service.entities.Enrollment;
import com.ritvik.school.student_service.entities.Student;
import com.ritvik.school.student_service.exceptions.EnrollmentNotFoundException;
import com.ritvik.school.student_service.repositoriy.EnrollmentRepo;
import com.ritvik.school.student_service.repositoriy.StudentRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class EnrollmentService implements EnrollmentServiceimpl {

    private final EnrollmentRepo enrollmentRepo;
    private  final AcademicClient academicClient;
    private final StudentRepo studentRepo;

    public EnrollmentService(EnrollmentRepo enrollmentRepo, AcademicClient academicClient, StudentRepo studentRepo){
        this.enrollmentRepo = enrollmentRepo;
        this.academicClient = academicClient;
        this.studentRepo = studentRepo;
    }

    private EnrollmentDto toEnrollmentDto(Enrollment enrollment) {
        EnrollmentDto enrollmentDto = new EnrollmentDto();

        enrollmentDto.setSubjectId(enrollment.getSubjectId());
        enrollmentDto.setSessionYear(enrollment.getSessionYear());
        enrollmentDto.setActive(enrollment.getActive());
        enrollmentDto.setEnrollmentId(enrollment.getEnrollmentId());
        enrollmentDto.setScoredTheoryMarks(enrollment.getScoredTheoryMarks());
        enrollmentDto.setScoredPracticeMarks(enrollment.getScoredPracticeMarks());
        enrollmentDto.setStudentId(enrollment.getStudent().getStudentId());
        enrollmentDto.setClassId(enrollment.getClassId());

        return enrollmentDto;
    }

    private Enrollment toEnrollment(EnrollmentDto enrollmentDto) {
        Enrollment enrollment = new Enrollment();

        enrollment.setEnrollmentId(enrollmentDto.getEnrollmentId());
        enrollment.setSessionYear(enrollmentDto.getSessionYear());
        enrollment.setActive(enrollmentDto.getActive());
        enrollment.setSubjectId(enrollmentDto.getSubjectId());
        enrollment.setScoredTheoryMarks(enrollmentDto.getScoredTheoryMarks());
        enrollment.setScoredPracticeMarks(enrollmentDto.getScoredPracticeMarks());
        enrollment.setClassId(enrollmentDto.getClassId());
        enrollment.getStudent().setStudentId(enrollmentDto.getStudentId());

        return enrollment;
    }

    @Override
    public EnrollmentDto createEnrollment(EnrollmentDto enrollmentDto) {

        Enrollment enrollment = new Enrollment();
        Student student = studentRepo.findById(enrollmentDto.getStudentId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Student not found with student ID : "
                                        + enrollmentDto.getStudentId()));

        enrollment.setStudent(student);
        enrollment.setSubjectId(enrollmentDto.getSubjectId());
        enrollment.setSessionYear(enrollmentDto.getSessionYear());
        enrollment.setActive(enrollmentDto.getActive());
        enrollment.setClassId(enrollmentDto.getClassId());

        SubjectDto subject =
                academicClient.getSubject(enrollmentDto.getSubjectId());

        if (enrollmentDto.getScoredTheoryMarks() <= subject.getTheoryMarks()
                && enrollmentDto.getScoredPracticeMarks() <= subject.getPracticalMarks()) {
            enrollment.setScoredPracticeMarks(enrollmentDto.getScoredPracticeMarks());
            enrollment.setScoredTheoryMarks(enrollmentDto.getScoredTheoryMarks());
            return this.toEnrollmentDto(enrollmentRepo.save(enrollment));

        } else {
            throw new RuntimeException(
                    "Scored marks cannot be greater than maximum marks");
        }
    }



    @Override
    public Page<EnrollmentDto> viewAllEnrollments(int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("desc")

                ? Sort.by(sortBy).descending()

                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Enrollment> enrollmentPage = enrollmentRepo.findAll(pageable);

               return enrollmentPage.map(this::toEnrollmentDto);


    }

    @Override
    public EnrollmentDto updateEnrollment(EnrollmentDto enrollmentDto) {
        Enrollment enrollment = enrollmentRepo.findById(enrollmentDto.getEnrollmentId())
                .orElseThrow(() ->
                        new EnrollmentNotFoundException(
                                "Enrollment not found with enrollment ID : "
                                        + enrollmentDto.getEnrollmentId()));

        enrollment.setSessionYear(enrollmentDto.getSessionYear());
        enrollment.setActive(enrollmentDto.getActive());
        enrollment.setSubjectId(enrollmentDto.getSubjectId());
        enrollment.setClassId(enrollmentDto.getClassId());
        enrollment.getStudent().setStudentId(enrollmentDto.getStudentId());


        SubjectDto subject =
                academicClient.getSubject(enrollmentDto.getSubjectId());

        if (enrollmentDto.getScoredTheoryMarks() <= subject.getTheoryMarks()
                && enrollmentDto.getScoredPracticeMarks() <= subject.getPracticalMarks()) {

            enrollment.setScoredTheoryMarks(enrollmentDto.getScoredTheoryMarks());
            enrollment.setScoredPracticeMarks(enrollmentDto.getScoredPracticeMarks());
        } else {
            throw new RuntimeException("Scored marks cannot be greater than maximum marks");
        }
        return this.toEnrollmentDto(enrollmentRepo.save(enrollment));
    }


    @Override
    public EnrollmentDto viewEnrollmentById(long enrollmentId) {

        return this.toEnrollmentDto(enrollmentRepo.findById(enrollmentId).orElseThrow(() ->
                new EnrollmentNotFoundException("Enrollment not found with enrollment ID : "
                        + enrollmentId)));

    }

    @Override
        public void deleteEnrollment ( long enrollmentId){
        enrollmentRepo.deleteById(enrollmentId);

        }
    @Override
    public SubjectDto viewSubjectById(long subjectId) {
    return academicClient.getSubject(subjectId);
    }
}
