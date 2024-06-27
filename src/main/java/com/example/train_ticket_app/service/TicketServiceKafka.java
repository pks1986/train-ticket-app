package com.example.train_ticket_app.service;

import com.example.train_ticket_app.model.Ticket;
import com.example.train_ticket_app.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TicketServiceKafka {

    private static final double TICKET_PRICE = 20.0;
    private final TrainService trainService;
    private final KafkaProducerService kafkaProducerService;
    private final List<Ticket> tickets;

    @Autowired
    public TicketServiceKafka(TrainService trainService, KafkaProducerService kafkaProducerService) {
        this.trainService = trainService;
        this.kafkaProducerService = kafkaProducerService;
        this.tickets = new ArrayList<>();
    }

    public Ticket purchaseTicket(User user) {
        String seat = trainService.assignSeat();
        Ticket ticket = new Ticket("London", "France", user, TICKET_PRICE, seat);
        tickets.add(ticket);
        kafkaProducerService.sendMessage(ticket);
        return ticket;
    }

    public Optional<Ticket> getReceipt(String email) {
        return tickets.stream()
                .filter(ticket -> ticket.getUser().getEmail().equals(email))
                .findFirst();
    }

    public List<User> getUsersBySection(String section) {
        return tickets.stream()
                .filter(ticket -> ticket.getSeat().startsWith(section))
                .map(Ticket::getUser)
                .collect(Collectors.toList());
    }

    public boolean removeUser(String email) {
        return tickets.removeIf(ticket -> ticket.getUser().getEmail().equals(email));
    }

    public Optional<Ticket> modifySeat(String email, String newSeat) {
        Optional<Ticket> ticketOptional = getReceipt(email);
        if (ticketOptional.isPresent() && trainService.isValidSeat(newSeat)) {
            ticketOptional.get().setSeat(newSeat);
            return ticketOptional;
        }
        return Optional.empty();
    }
}

