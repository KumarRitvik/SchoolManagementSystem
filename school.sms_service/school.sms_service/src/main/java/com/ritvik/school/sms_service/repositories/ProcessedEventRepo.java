package com.ritvik.school.sms_service.repositories;

import com.ritvik.school.sms_service.entities.ProcessedEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProcessedEventRepo extends JpaRepository<ProcessedEvent, String> {
}