package com.example.train_ticket_app.repository;

import com.example.train_ticket_app.model.Ticket;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class TicketRepository {

    private final List<Ticket> tickets = new ArrayList<>();

    public Ticket save(Ticket ticket) {
        tickets.add(ticket);
        return ticket;
    }

    public Optional<Ticket> findByEmail(String email) {
        return tickets.stream()
                .filter(ticket -> ticket.getUser().getEmail().equals(email))
                .findFirst();
    }

    public List<Ticket> findAll() {
        return new ArrayList<>(tickets);
    }

    public List<Ticket> findBySection(String section) {
        return tickets.stream()
                .filter(ticket -> ticket.getSeat().startsWith(section))
                .collect(Collectors.toList());
    }

    public boolean deleteByEmail(String email) {
        return tickets.removeIf(ticket -> ticket.getUser().getEmail().equals(email));
    }

    public void deleteAll() {
        tickets.clear();
    }

    public long count() {
        return tickets.size();
    }
}

