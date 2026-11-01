
package com.ritvik.school.calender_service.producers;

import com.ritvik.school.calender_service.events.HolidayEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class HolidayEventProducer {

    private static final String TOPIC = "holiday-events";

    private final KafkaTemplate<String, HolidayEvent> kafkaTemplate;

    public HolidayEventProducer(
            KafkaTemplate<String, HolidayEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendHolidayEvent(HolidayEvent event) {

        kafkaTemplate.send(
                TOPIC,
                event.getEventId(),
                event
        );
    }
}