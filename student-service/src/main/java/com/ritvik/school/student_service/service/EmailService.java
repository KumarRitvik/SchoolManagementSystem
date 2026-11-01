package com.ritvik.school.student_service.service;

import com.ritvik.school.student_service.kafka.events.StudentCreatedEvent;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendStudentCreatedEmail(StudentCreatedEvent event) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(event.getEmail());
        message.setSubject("Student Registration Successful");
        message.setText(
                "Hello " + event.getName() + ",\n\n" +
                        "Your student registration was successful.\n" +
                        "Student ID: " + event.getStudentId() + "\n\n" +
                        "Regards,\nSchool Management System"
        );

        mailSender.send(message);
    }
}
