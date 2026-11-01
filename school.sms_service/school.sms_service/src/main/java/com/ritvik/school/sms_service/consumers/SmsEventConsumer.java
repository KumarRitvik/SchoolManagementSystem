package com.ritvik.school.sms_service.consumers;

import com.ritvik.school.sms_service.entities.ProcessedEvent;
import com.ritvik.school.sms_service.kafka.events.StudentCreatedEvent;
import com.ritvik.school.sms_service.repositories.ProcessedEventRepo;
import com.ritvik.school.sms_service.services.SmsService;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class SmsEventConsumer {

    private final ProcessedEventRepo processedEventRepo;
    private final SmsService smsService;

    public SmsEventConsumer(
            ProcessedEventRepo processedEventRepo,
            SmsService smsService
    ) {
        this.processedEventRepo = processedEventRepo;
        this.smsService = smsService;
    }

    @RetryableTopic(attempts = "4", retryTopicSuffix = "-sms-retry", dltTopicSuffix = "-sms-dlt")
    @KafkaListener(topics = "student-events", groupId = "sms-service-group")
    public void consume(StudentCreatedEvent event) {

        // Idempotency check
        if (processedEventRepo.existsById(event.getEventId())) {
            System.out.println("Event already processed: " + event.getEventId());

            return;
        }

        System.out.println("===== STUDENT EVENT RECEIVED FOR SMS =====");
        System.out.println("Event ID : " + event.getEventId());
        System.out.println("Student ID : " + event.getStudentId());
        System.out.println("Name : " + event.getName());
        System.out.println("Phone : " + event.getPhone());

        // Send SMS
        smsService.sendStudentCreatedSms(event);

        // Mark event as processed
        ProcessedEvent processedEvent = new ProcessedEvent();

        processedEvent.setEventId(event.getEventId());
        processedEvent.setProcessedAt(LocalDateTime.now());

        processedEventRepo.save(processedEvent);
        System.out.println("Event successfully processed: " + event.getEventId());
    }

    @DltHandler
    public void handleDlt(StudentCreatedEvent event) {

        System.out.println("===== EVENT MOVED TO SMS DLT =====");

        System.out.println("Event ID : " + event.getEventId());
        System.out.println("Student ID : " + event.getStudentId());
        System.out.println("Name : " + event.getName());
        System.out.println("Phone : " + event.getPhone());
    }
}