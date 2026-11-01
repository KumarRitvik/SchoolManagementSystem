package com.ritvik.school.calender_service.services;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;
import com.ritvik.school.calender_service.events.HolidayEvent;
import com.ritvik.school.calender_service.producers.HolidayEventProducer;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@Service
public class GoogleCalendarService {

    //So you're telling Google: Read events from this calendar.
    private static final String CALENDAR_ID =
            "ritvikkumar2013@gmail.com";

    //We want the holiday date interpreted according to India time,so initialized
    //INDIA_ZONE var of ZoneId class type to value "Asia/Kolkata".. different servers in
    // diferent locns acress globe may interpret it differently
    private static final ZoneId INDIA_ZONE =
            ZoneId.of("Asia/Kolkata");


    //.Your application obtained this object(Calender)
    // through the Google Calendar API configuration/OAuth setup.
    //Before Google allows your application to access the calendar, your application
    // needs authorization.

    //You have: credentials.json
    //This contains your Google OAuth application's credentials.

    //You also have:tokens which stores the OAuth authorization obtained after you authorized
    // the application in your browser.(warna login karna hoga bar bar)
    private final Calendar calendar;

    private final HolidayEventProducer holidayEventProducer;

    public GoogleCalendarService(
            Calendar calendar,
            HolidayEventProducer holidayEventProducer) {

        this.calendar = calendar;
        this.holidayEventProducer = holidayEventProducer;
    }



    public void publishUpcomingHolidayEvents() throws IOException {

        //added 1 to today"s date coz we want to fetch events for tomorrow, not today!
        LocalDate tomorrow = LocalDate.now(INDIA_ZONE).plusDays(1);


        DateTime timeMin = new DateTime(tomorrow.atStartOfDay(INDIA_ZONE //means: Tomorrow at
                // exactly midnight in India(tomorrow.atStartOfDay(INDIA_ZONE)=
                // 2026-09-28 00:00:00 +05:30)
                ).toInstant()//.toInstant() converts that into an absolute point in time.
                //like:- 2026-09-28 00:00:00 IST
                .toEpochMilli());//.toEpochMilli():- converts the Instant into a number
               // like:XXXXXXXXXXXX .That number represents the exact instant in time.

        // max time tomorrow se 1 din plus rahega
        DateTime timeMax = new DateTime(tomorrow.plusDays(1)
                                                .atStartOfDay(INDIA_ZONE)
                                                .toInstant()
                                                .toEpochMilli());
        //ye Event Object bhi google api dependency maven se download karne se mliti hai
        Events events = calendar.events()
                .list(CALENDAR_ID)//Ask Google Calendar for the events belonging to this particular calendar.
                .setTimeMin(timeMin)//Don't return events before this point.
                .setTimeMax(timeMax)//Don't return events after this point.
                .setSingleEvents(true)//matlab recurring nahi event hai(like every monday)
                                      // , ek bar hoga
                .setOrderBy("startTime")//Return the events ordered by their starting time.
                .execute();// now send this request to Google Calendar and get the response back in
                           // the form of an Events object.

        List<Event> eventList = events.getItems();

        if (eventList == null || eventList.isEmpty()) {
            return;
        }

        for (Event event : eventList) {

            String eventDate;
            //if event has only date like:- School Holiday
            //September 28
            //All day
            if (event.getStart().getDate() != null) {
                eventDate = event.getStart()
                        .getDate()
                        .toString();
            }
            //if event has timestamp along with date like:-
            //School Meeting
            //September 28
            //10:00 AM – 11:00 AM
            else {
                eventDate = event.getStart()
                        .getDateTime()
                        .toString();
            }
            //Converted Google Event into HolidayEvent
            HolidayEvent holidayEvent = new HolidayEvent(
                    event.getId(),
                    event.getSummary(),
                    eventDate
            );

            holidayEventProducer.sendHolidayEvent(holidayEvent);
        }
    }
}