package com.ritvik.school.student_service.producer;

import com.ritvik.school.student_service.kafka.events.StudentCreatedEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class StudentEventProducer {

    private final KafkaTemplate<String, StudentCreatedEvent> kafkaTemplate;

    public StudentEventProducer(
            KafkaTemplate<String, StudentCreatedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
    public void sendStudentCreatedEvent(StudentCreatedEvent event) {
        kafkaTemplate.send("student-events", event.getStudentId().toString(), event);
    }
}