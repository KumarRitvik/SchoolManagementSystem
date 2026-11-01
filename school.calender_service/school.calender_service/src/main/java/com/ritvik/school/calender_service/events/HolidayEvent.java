package com.ritvik.school.calender_service.events;

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