package com.ritvik.school.sms_service.kafka.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HolidayEvent {
    private String eventId;
    private String holidayName;
    private String holidayDate;
}