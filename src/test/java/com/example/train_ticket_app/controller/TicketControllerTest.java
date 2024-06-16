package com.example.train_ticket_app.controller;

import com.example.train_ticket_app.model.Ticket;
import com.example.train_ticket_app.model.User;
import com.example.train_ticket_app.service.TicketService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TicketControllerTest {

    @InjectMocks
    private TicketController ticketController;

    @Mock
    private TicketService ticketService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testPurchaseTicket() {
        User user = new User("John", "Doe", "john.doe@example.com");

        Ticket ticket = new Ticket("London", "France", user, 20.0, "A1");

        when(ticketService.purchaseTicket(any(User.class))).thenReturn(ticket);

        ResponseEntity<Ticket> response = ticketController.purchaseTicket(user);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("A1", response.getBody().getSeat());
    }

    @Test
    public void testGetReceipt() {
        User user = new User("John", "Doe", "john.doe@example.com");

        Ticket ticket = new Ticket("London", "France", user, 20.0, "A1");

        when(ticketService.getReceipt(any(String.class))).thenReturn(Optional.of(ticket));

        ResponseEntity<Ticket> response = ticketController.getReceipt("john.doe@example.com");

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("A1", response.getBody().getSeat());
    }

    @Test
    public void testGetUsersBySection() {
        User user = new User("John", "Doe", "john.doe@example.com");

        when(ticketService.getUsersBySection(any(String.class))).thenReturn(Collections.singletonList(user));

        ResponseEntity<List<User>> response = ticketController.getUsersBySection("A");

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().contains(user));
    }

    @Test
    public void testRemoveUser() {
        when(ticketService.removeUser(any(String.class))).thenReturn(true);

        ResponseEntity<Void> response = ticketController.removeUser("john.doe@example.com");

        assertNotNull(response);
        assertEquals(204, response.getStatusCodeValue());
    }

    @Test
    public void testModifySeat() {
        User user = new User("John", "Doe", "john.doe@example.com");

        Ticket ticket = new Ticket("London", "France", user, 20.0, "A2");

        when(ticketService.modifySeat(any(String.class), any(String.class))).thenReturn(Optional.of(ticket));

        ResponseEntity<Ticket> response = ticketController.modifySeat("john.doe@example.com", "A2");

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("A2", response.getBody().getSeat());
    }
}

