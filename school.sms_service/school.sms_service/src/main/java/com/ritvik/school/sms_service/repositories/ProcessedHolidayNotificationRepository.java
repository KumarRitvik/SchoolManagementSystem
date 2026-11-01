package com.ritvik.school.sms_service.repositories;

import com.ritvik.school.sms_service.entities.ProcessedHolidayNotification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcessedHolidayNotificationRepository
        extends JpaRepository<ProcessedHolidayNotification, String> {
}