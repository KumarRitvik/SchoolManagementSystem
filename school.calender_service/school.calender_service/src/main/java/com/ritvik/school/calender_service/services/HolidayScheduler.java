package com.ritvik.school.calender_service.services;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class HolidayScheduler {

    private final GoogleCalendarService googleCalendarService;

    public HolidayScheduler(GoogleCalendarService googleCalendarService) {
        this.googleCalendarService = googleCalendarService;
    }
    //means Every day at 9:00 AM Spring's scheduler executes method:- checkHolidays() i.e; har roz 9
    //bje subeh spring checKHoliday() method call karega...
    @Scheduled(cron = "0 * * * * *")
    public void checkHolidays() {
        try {
            googleCalendarService.publishUpcomingHolidayEvents();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}