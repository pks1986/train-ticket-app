package com.example.train_ticket_app;

import com.example.train_ticket_app.model.Ticket;
import com.example.train_ticket_app.model.User;
import com.example.train_ticket_app.repository.TicketRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TicketControllerIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private TicketRepository ticketRepository;

    @BeforeEach
    public void setup() {
        ticketRepository.deleteAll();
    }

    @AfterEach
    public void teardown() {
        ticketRepository.deleteAll();
    }

    @Test
    public void testPurchaseTicket() {
        User user = new User("John", "Doe", "john.doe@example.com");

        webTestClient.post()
                .uri("/api/purchase")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(user)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Ticket.class)
                .value(ticket -> {
                    assertNotNull(ticket);
                    assertEquals("London", ticket.getFrom());
                    assertEquals("France", ticket.getTo());
                    assertEquals(20.0, ticket.getPricePaid());
                    assertEquals("John", ticket.getUser().getFirstName());
                    assertEquals("Doe", ticket.getUser().getLastName());
                    assertEquals("john.doe@example.com", ticket.getUser().getEmail());
                });
    }

    @Test
    public void testGetReceipt() {
        User user = new User("John", "Doe", "john.doe@example.com");
        Ticket ticket = new Ticket("London", "France", user, 20.0, "A1");
        ticketRepository.save(ticket);

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/receipt")
                        .queryParam("email", "john.doe@example.com")
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody(Ticket.class)
                .value(receipt -> {
                    assertNotNull(receipt);
                    assertEquals("London", receipt.getFrom());
                    assertEquals("France", receipt.getTo());
                    assertEquals(20.0, receipt.getPricePaid());
                    assertEquals("A1", receipt.getSeat());
                    assertEquals("John", receipt.getUser().getFirstName());
                    assertEquals("Doe", receipt.getUser().getLastName());
                    assertEquals("john.doe@example.com", receipt.getUser().getEmail());
                });
    }

    @Test
    public void testGetReceiptNotFound() {
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/receipt")
                        .queryParam("email", "nonexistent@example.com")
                        .build())
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    public void testGetUsersBySection() {
        User user = new User("John", "Doe", "john.doe@example.com");
        Ticket ticket = new Ticket("London", "France", user, 20.0, "A1");
        ticketRepository.save(ticket);

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/users")
                        .queryParam("section", "A")
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(User.class)
                .value(users -> {
                    assertNotNull(users);
                    assertEquals(1, users.size());
                    assertEquals("John", users.get(0).getFirstName());
                    assertEquals("Doe", users.get(0).getLastName());
                    assertEquals("john.doe@example.com", users.get(0).getEmail());
                });
    }

    @Test
    public void testRemoveUser() {
        User user = new User("John", "Doe", "john.doe@example.com");
        Ticket ticket = new Ticket("London", "France", user, 20.0, "A1");
        ticketRepository.save(ticket);

        webTestClient.delete()
                .uri(uriBuilder -> uriBuilder.path("/api/remove")
                        .queryParam("email", "john.doe@example.com")
                        .build())
                .exchange()
                .expectStatus().isNoContent();

        assertEquals(0, ticketRepository.count());
    }

    @Test
    public void testRemoveUserNotFound() {
        webTestClient.delete()
                .uri(uriBuilder -> uriBuilder.path("/api/remove")
                        .queryParam("email", "nonexistent@example.com")
                        .build())
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    public void testModifySeat() {
        User user = new User("John", "Doe", "john.doe@example.com");
        Ticket ticket = new Ticket("London", "France", user, 20.0, "A1");
        ticketRepository.save(ticket);

        webTestClient.put()
                .uri(uriBuilder -> uriBuilder.path("/api/modify-seat")
                        .queryParam("email", "john.doe@example.com")
                        .queryParam("newSeat", "A2")
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody(Ticket.class)
                .value(modifiedTicket -> {
                    assertNotNull(modifiedTicket);
                    assertEquals("A2", modifiedTicket.getSeat());
                });
    }

    @Test
    public void testModifySeatBadRequest() {
        webTestClient.put()
                .uri(uriBuilder -> uriBuilder.path("/api/modify-seat")
                        .queryParam("email", "nonexistent@example.com")
                        .queryParam("newSeat", "A2")
                        .build())
                .exchange()
                .expectStatus().isBadRequest();
    }
}

