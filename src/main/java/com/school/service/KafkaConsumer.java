package com.school.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {

    @KafkaListener(topics = "user-registration", groupId = "school-group")
    public void consume(String message) {
        System.out.println("Received Kafka message: " + message);
    }
}

