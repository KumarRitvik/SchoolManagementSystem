package com.ritvik.school.student_service.repositoriy;

import com.ritvik.school.student_service.entities.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EnrollmentRepo extends JpaRepository<Enrollment, Long> {
    Optional<Enrollment> findById(Long aLong);
}
