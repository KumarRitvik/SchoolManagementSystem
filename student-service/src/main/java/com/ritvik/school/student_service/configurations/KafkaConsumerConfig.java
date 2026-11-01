package com.ritvik.school.student_service.configurations;

import com.ritvik.school.student_service.kafka.events.StudentCreatedEvent;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfig {

    @Bean
    public ConsumerFactory<String, StudentCreatedEvent> consumerFactory() {
        //here we are configuring our consumer i.e; which kafka broker(here: 9092) server it should connect to
        //to which consumer group(student-group) this consumer must belong, A deserializer type(StringDeSerializer)
        //to convert back "Key" from bytes back to String, A Json desrializer to convert back "Value" which is in
        // Json format into Object. This configuration is stored in a Hashmap named
        // "config"
        Map<String, Object> config = new HashMap<>();

        config.put(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                "localhost:9092"
        );

        config.put(
                ConsumerConfig.GROUP_ID_CONFIG,
                "student-group"
        );

        config.put(
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                StringDeserializer.class
        );

        config.put(
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                JsonDeserializer.class
        );

        // un config we used Json deserializer to convert Json into object
        // par kis object (StudentCreatedEvent) me convert karna hai ham yaha ye bata rahe hai
        JsonDeserializer<StudentCreatedEvent> deserializer =
                new JsonDeserializer<>(StudentCreatedEvent.class);
        //means trust Java classes coming from this package for deserialization. agr nahi likha to
        //Spring Kafka;s deserializer ise malicious samajhkar error de dega
        deserializer.addTrustedPackages(
                "com.ritvik.school.student_service.kafka.events"
        );

        // jis jis deserializer ki baat ki hamn ab actual me us us deserializer ke objects ko as parameter/asrgs ham
        // bhej denge along with config to the DefaultKafkaConsumerFactory.
        //         We provide:
        //         1. config                  -> Kafka consumer configuration
        //         2. StringDeserializer      -> actual key deserializer object
        //         3. deserializer            -> actual value deserializer object
        //
        //        // DefaultKafkaConsumerFactory implements ConsumerFactory,
        //        // therefore this object can be returned as ConsumerFactory.
        return new DefaultKafkaConsumerFactory<>(
                config,
                new StringDeserializer(),
                deserializer
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, StudentCreatedEvent>
    kafkaListenerContainerFactory() {

        ConcurrentKafkaListenerContainerFactory<String, StudentCreatedEvent>
                factory = new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(this.consumerFactory());

        return factory;
    }
    //now jab app start hoga to Bean ConcurrentKafkaListenerContainerFactory kar ban kar container
    // me pada rahega with required configuration.
    // IMPORTANT:- ek ListenerContainer hi Spring ko batata hai ki kaunsa consumer
    // kis topic se messages receive karega, Listener Container ko "kis class mein Consumer(jo upar configure
    // kiya hamne) inject karna hai" ye pata karne ki zarurat nahi hai, because Consumer inject hi nahi hota.
    // lISTENER Container Kafka Consumer ko manage karta hai aur received record ko registered listener(Student
    // EventConsumer here) ke method(consume() here) par invoke karta hai(jispe @KafkaListener likhs hai)

}