package com.ritvik.school.teacher_service.repositories;

import com.ritvik.school.teacher_service.entities.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssignmentRepo extends JpaRepository<Assignment, Long> {
    List<Assignment> findAll();
}
