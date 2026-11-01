package com.ritvik.school.student_service.consumers;

import com.ritvik.school.student_service.kafka.events.StudentCreatedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class StudentEventConsumer2 {

    @KafkaListener(
            topics = "student-events",
            groupId = "student-group-2"
    )
    public void consume(StudentCreatedEvent event) {

        System.out.println("===== CONSUMER 2 =====");

        System.out.println("ID    : " + event.getStudentId());
        System.out.println("Name  : " + event.getName());
        System.out.println("Email : " + event.getEmail());
    }
}