package com.ritvik.school.sms_service.entities;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "processed_events")
public class ProcessedEvent {

    @Id
    private String eventId;
    private LocalDateTime processedAt;
}
