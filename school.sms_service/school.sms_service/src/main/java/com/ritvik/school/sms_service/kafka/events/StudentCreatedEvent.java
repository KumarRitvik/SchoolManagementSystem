package com.ritvik.school.sms_service.kafka.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentCreatedEvent {

    private String eventId;
    private Long studentId;
    private String name;
    private String email;
    private String phone;

}