package com.ritvik.school.student_service.configurations;

import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import com.ritvik.school.student_service.kafka.events.StudentCreatedEvent;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {

    @Bean
    public ProducerFactory<String, StudentCreatedEvent> producerFactory() {  //---------<-----------------<------
        //here we are configuring our producer i.e; which kafka broker(here: 9092) server it should connect to
        //to which producer group(student-group) this producer must belong, A serializer type(StringSerializer)
        //to convert "Key" to bytes, A Json serializer to convert "Value" which is in
        // Json format into bytes. This configuration is stored in a Hashmap named
        // "config"
        Map<String, Object> config = new HashMap<>();

        config.put(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                "localhost:9092"
        );

        config.put(
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class
        );

        config.put(
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                JsonSerializer.class
        );

        //hamne configuration ko DefaultKafkaProducerFactory ke constructor me as parameter pass kar diya hai.
        // Ye factory hamare liye producer objects create karega.
        return new DefaultKafkaProducerFactory<>(config);
    }

    @Bean
    public KafkaTemplate<String, StudentCreatedEvent> kafkaTemplate() {

        return new KafkaTemplate<>(this.producerFactory());//-------------------------------------------------->--^
    }
    //now jab app start hoga to Bean KafkaTemplste kar ban kar container me pada rahega with required configguration.
    // Ab ham is KafkaTemplate ko inject karke use kar sakte hai to send messages to kafka topic.
}