package com.example.train_ticket_app.service;

import com.example.train_ticket_app.model.Ticket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

    private static final String TOPIC = "ticket-purchases";

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public void sendMessage(Ticket ticket) {
        kafkaTemplate.send(TOPIC, ticket);
    }
}

