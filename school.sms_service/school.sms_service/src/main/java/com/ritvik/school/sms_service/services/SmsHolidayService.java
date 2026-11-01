package com.ritvik.school.sms_service.services;

import com.ritvik.school.sms_service.kafka.events.HolidayEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SmsHolidayService {

    private final SmsService smsService;
    private final String testStudentPhone;

    public SmsHolidayService(
            SmsService smsService,
            @Value("${sms.test-recipient}") String testStudentPhone) {

        this.smsService = smsService;
        this.testStudentPhone = testStudentPhone;
    }

    public void sendHolidaySms(HolidayEvent event) {

        String message = "School Holiday: " + event.getHolidayName() + " on "
                        + event.getHolidayDate() + ".";

        smsService.sendSms(testStudentPhone, message);
    }
}