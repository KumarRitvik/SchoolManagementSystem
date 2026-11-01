package com.ritvik.school.teacher_service.repositories;

import com.ritvik.school.teacher_service.entities.Teacher;

import com.ritvik.school.teacher_service.enums.TeacherType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeacherRepo extends JpaRepository<Teacher, Long> {

    Page<Teacher> findAll(Pageable pageable);

    Optional<Teacher> findByEmail(String email);
    Optional<Teacher> findByPhoneNumber(@NotBlank @Pattern(
            regexp = "^[6-9]\\d{9}$",
            message = "Invalid phone number") String number);
    List<Teacher> findByTeacherType(TeacherType teacherType);
    Page<Teacher> findByNameContainingIgnoreCase(String name, org.springframework.data.domain.Pageable pageable);


}