package com.ritvik.school.student_service.consumers;

import com.ritvik.school.student_service.entities.ProcessedEvent;
import com.ritvik.school.student_service.kafka.events.StudentCreatedEvent;
import com.ritvik.school.student_service.repositoriy.ProcessedEventRepo;
import com.ritvik.school.student_service.service.EmailService;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class StudentEventConsumer {

    private final ProcessedEventRepo processedEventRepo;
    private final EmailService emailService;

    public StudentEventConsumer(ProcessedEventRepo processedEventRepo, EmailService emailService) {
        this.processedEventRepo = processedEventRepo;
        this.emailService = emailService;
    }


    @RetryableTopic(attempts = "4", retryTopicSuffix = "-student-retry", dltTopicSuffix = "-student-dlt")
    @KafkaListener(topics = "student-events", groupId = "student-group")
    //@kafkaListener:-Ye Spring ko batata hai:"is method ko Kafka messages receive karne ke liye use karo."
    //"student-events" topic se messages receive karne ke liye.
    // groupId:-Ye batata hai ki ye consumer kis consumer group ka hissa hai.
    //@RetryableTopic:-Ye Spring ko batata hai ki agar ye method fail ho jaye to retry karna hai(jere 4 times)
    public void consume(StudentCreatedEvent event) {

        System.out.println("===== STUDENT EVENT RECEIVED =====");
        if (processedEventRepo.existsById(event.getEventId())) {
            System.out.println("Event already processed: " + event.getEventId());
            return;
        }

        System.out.println("===== STUDENT EVENT RECEIVED =====");

        emailService.sendStudentCreatedEmail(event);

        ProcessedEvent processedEvent = new ProcessedEvent();
        processedEvent.setEventId(event.getEventId());
        processedEvent.setProcessedAt(LocalDateTime.now());

        processedEventRepo.save(processedEvent);

        System.out.println("ID    : " + event.getStudentId());
        System.out.println("Name  : " + event.getName());
        System.out.println("Email : " + event.getEmail());
        System.out.println("Phone : " + event.getPhone());


    }
    //@DltHandler:- sabhi retry over hone k baad control is method par ayega, aur yr ise handle karega
        @DltHandler
        public void handleDlt(StudentCreatedEvent event) {

            System.out.println("===== EVENT MOVED TO DLT =====");

            System.out.println("ID    : " + event.getStudentId());
            System.out.println("Name  : " + event.getName());
            System.out.println("Email : " + event.getEmail());
            System.out.println("Phone : " + event.getPhone());
        }

}