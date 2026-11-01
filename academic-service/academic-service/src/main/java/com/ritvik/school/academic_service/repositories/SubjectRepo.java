package com.ritvik.school.academic_service.repositories;

import com.ritvik.school.academic_service.entities.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubjectRepo extends JpaRepository<Subject, Long> {
    List<Subject> findAll();
}
