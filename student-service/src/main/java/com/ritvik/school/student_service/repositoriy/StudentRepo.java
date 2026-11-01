package com.ritvik.school.student_service.repositoriy;

import com.ritvik.school.student_service.dtos.StudentDto;
import com.ritvik.school.student_service.entities.Student;
import com.ritvik.school.student_service.enums.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepo extends JpaRepository<Student, Long> {

    List<Student> findAll();


    Page<Student> findAll(Pageable pageable);

    Optional<Student> findByEmail(String email);

    Optional<Student> findByPhoneNumber(@NotBlank @Pattern(
            regexp = "^[6-9]\\d{9}$",
            message = "Invalid phone number") String number);

    
    Page<Student> findByNameContainingIgnoreCase(String name, org.springframework.data.domain.Pageable pageable);

    Page<Student> findByStudentType(StudentType studentType, Pageable pageable);

    @Query(value = "SELECT DATABASE()", nativeQuery = true)
    String getCurrentDatabase();


}