package com.ritvik.school.sms_service.configurations;

import com.ritvik.school.sms_service.kafka.events.HolidayEvent;
import com.ritvik.school.sms_service.kafka.events.StudentCreatedEvent;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.support.serializer.JacksonJsonDeserializer;
import org.springframework.util.backoff.FixedBackOff;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfig {

    // ================= STUDENT CONSUMER =================

    @Bean
    public ConsumerFactory<String, StudentCreatedEvent> consumerFactory() {

        Map<String, Object> props = new HashMap<>();

        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "sms-service-group");
        JacksonJsonDeserializer<StudentCreatedEvent> deserializer =
                new JacksonJsonDeserializer<>(StudentCreatedEvent.class);

        deserializer.trustedPackages("com.ritvik.school.sms_service.kafka.events");
        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), deserializer.ignoreTypeHeaders());
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, StudentCreatedEvent>
    kafkaListenerContainerFactory() {

        ConcurrentKafkaListenerContainerFactory<String, StudentCreatedEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());

        return factory;
    }


    // ================= HOLIDAY CONSUMER =================

    @Bean
    public ConsumerFactory<String, HolidayEvent> holidayConsumerFactory() {

        Map<String, Object> props = new HashMap<>();

        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "sms-holiday-group");
        JacksonJsonDeserializer<HolidayEvent> deserializer =
                new JacksonJsonDeserializer<>(HolidayEvent.class);
        deserializer.trustedPackages("com.ritvik.school.sms_service.kafka.events");

        return new DefaultKafkaConsumerFactory<>(props,
                new StringDeserializer(), deserializer.ignoreTypeHeaders());
    }
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, HolidayEvent>
    holidayKafkaListenerContainerFactory(
            DefaultErrorHandler holidayKafkaErrorHandler) {

        ConcurrentKafkaListenerContainerFactory<String, HolidayEvent>
                factory = new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(holidayConsumerFactory());
        factory.setCommonErrorHandler(holidayKafkaErrorHandler);

        return factory;
    }


    // ================= HOLIDAY DLT =================

    @Bean
    public DefaultErrorHandler holidayKafkaErrorHandler(KafkaTemplate<String, HolidayEvent> kafkaTemplate) {

        DeadLetterPublishingRecoverer recoverer =
                new DeadLetterPublishingRecoverer(kafkaTemplate, (record, exception) ->
                                new TopicPartition(record.topic() + ".DLT", record.partition()));
        // 2 seconds between retries
        // 4 retries after the original attempt
        FixedBackOff fixedBackOff = new FixedBackOff(2000L, 4L);
        return new DefaultErrorHandler(recoverer, fixedBackOff);
    }

}