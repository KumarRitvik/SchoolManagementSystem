package com.ritvik.school.student_service.repositoriy;

import com.ritvik.school.student_service.entities.ProcessedEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProcessedEventRepo
        extends JpaRepository<ProcessedEvent, String> {
}