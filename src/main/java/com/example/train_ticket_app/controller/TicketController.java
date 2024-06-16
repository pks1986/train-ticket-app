package com.example.train_ticket_app.controller;

import com.example.train_ticket_app.model.Ticket;
import com.example.train_ticket_app.model.User;
import com.example.train_ticket_app.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @PostMapping("/purchase")
    public ResponseEntity<Ticket> purchaseTicket(@RequestBody User user) {
        Ticket ticket = ticketService.purchaseTicket(user);
        return ResponseEntity.ok(ticket);
    }

    @GetMapping("/receipt")
    public ResponseEntity<Ticket> getReceipt(@RequestParam String email) {
        Optional<Ticket> ticketOptional = ticketService.getReceipt(email);
        return ticketOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsersBySection(@RequestParam String section) {
        List<User> users = ticketService.getUsersBySection(section);
        return ResponseEntity.ok(users);
    }

    @DeleteMapping("/remove")
    public ResponseEntity<Void> removeUser(@RequestParam String email) {
        boolean removed = ticketService.removeUser(email);
        return removed ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @PutMapping("/modify-seat")
    public ResponseEntity<Ticket> modifySeat(@RequestParam String email, @RequestParam String newSeat) {
        Optional<Ticket> ticketOptional = ticketService.modifySeat(email, newSeat);
        return ticketOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }
}
