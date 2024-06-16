package com.example.train_ticket_app.model;

import java.util.Objects;

public class Ticket {
    private String from;
    private String to;
    private User user;
    private double pricePaid;
    private String seat;

    public Ticket() {}

    public Ticket(String from, String to, User user, double pricePaid, String seat) {
        this.from = from;
        this.to = to;
        this.user = user;
        this.pricePaid = pricePaid;
        this.seat = seat;
    }

    // Getters and Setters
    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public double getPricePaid() {
        return pricePaid;
    }

    public void setPricePaid(double pricePaid) {
        this.pricePaid = pricePaid;
    }

    public String getSeat() {
        return seat;
    }

    public void setSeat(String seat) {
        this.seat = seat;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ticket ticket = (Ticket) o;
        return Objects.equals(user, ticket.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user);
    }
}

