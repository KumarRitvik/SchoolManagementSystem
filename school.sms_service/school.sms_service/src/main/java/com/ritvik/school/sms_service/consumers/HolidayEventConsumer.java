package com.ritvik.school.sms_service.consumers;

import com.ritvik.school.sms_service.clients.StudentClient;
import com.ritvik.school.sms_service.dtos.StudentDto;
import com.ritvik.school.sms_service.entities.ProcessedHolidayNotification;
import com.ritvik.school.sms_service.kafka.events.HolidayEvent;
import com.ritvik.school.sms_service.repositories.ProcessedHolidayNotificationRepository;
import com.ritvik.school.sms_service.services.SmsService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class HolidayEventConsumer {

    private final SmsService smsService;
    private final ProcessedHolidayNotificationRepository processedHolidayNotificationRepository;
    private  final StudentClient studentClient;

    public HolidayEventConsumer(
            SmsService smsService,
            ProcessedHolidayNotificationRepository processedHolidayNotificationRepository, StudentClient studentClient) {
        this.smsService = smsService;
        this.processedHolidayNotificationRepository = processedHolidayNotificationRepository;
        this.studentClient = studentClient;
    }


    @KafkaListener(topics = "holiday-events", groupId = "sms-holiday-group", containerFactory = "holidayKafkaListenerContainerFactory")
    public void consume(HolidayEvent event) {

        if (processedHolidayNotificationRepository.existsById(event.getEventId())) {
            return;
        }

        String message = "School Holiday: " + event.getHolidayName() + " on " + event.getHolidayDate();

        List<StudentDto> studentsDto =studentClient.viewAllStudents();
        for(StudentDto student: studentsDto){
            if (Long.valueOf(123L).equals(student.getStudentId())) {
                smsService.sendSms(student.getPhoneNumber(), message);
            }
        }
        ProcessedHolidayNotification notification = new ProcessedHolidayNotification(event.getEventId(), LocalDateTime.now());
        processedHolidayNotificationRepository.save(notification);
    }

}