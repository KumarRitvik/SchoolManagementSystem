package com.ritvik.school.sms_service.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "holiday_notifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProcessedHolidayNotification {

    @Id
    private String eventId;
    private LocalDateTime sentAt;

}