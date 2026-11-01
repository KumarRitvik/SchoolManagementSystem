package com.ritvik.school.sms_service.services;

import com.ritvik.school.sms_service.kafka.events.StudentCreatedEvent;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SmsService {

    private final String fromNumber;

    public SmsService(
            @Value("${twilio.account-sid}") String accountSid,
            @Value("${twilio.auth-token}") String authToken,
            @Value("${twilio.from-number}") String fromNumber
    ) {
        this.fromNumber = fromNumber;

        Twilio.init(accountSid, authToken);
    }

    public void sendStudentCreatedSms(StudentCreatedEvent event) {

        String toNumber = "+91" + event.getPhone();
        Message message = Message.creator(new PhoneNumber(toNumber), new PhoneNumber(fromNumber), "sms_event_notifications").create();

        System.out.println("===== TWILIO SMS =====");
        System.out.println("Student ID : " + event.getStudentId());
        System.out.println("Student    : " + event.getName());
        System.out.println("To         : " + toNumber);
        System.out.println("Message SID: " + message.getSid());
        System.out.println("Status     : " + message.getStatus());
    }

    public void sendSms(String toNumber, String message) {

        if (!toNumber.startsWith("+")) {
            toNumber = "+91" + toNumber;
        }

        try {

            Message messageResponse = Message.creator(
                    new PhoneNumber(toNumber),new PhoneNumber(fromNumber),
                    "sms_event_notifications").create();
            System.out.println("===== TWILIO SMS RESULT =====");
            System.out.println("SMS SID : " + messageResponse.getSid());
            System.out.println("Status  : " + messageResponse.getStatus());

        } catch (Exception e) {
            e.printStackTrace();

            throw e;
        }
    }
}