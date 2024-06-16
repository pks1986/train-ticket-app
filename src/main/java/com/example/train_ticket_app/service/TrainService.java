package com.example.train_ticket_app.service;

import com.example.train_ticket_app.model.Train;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainService {
    private final Train train;

    public TrainService() {
        this.train = new Train();
    }

    public String assignSeat() {
        List<String> sectionASeats = train.getSectionASeats();
        List<String> sectionBSeats = train.getSectionBSeats();

        if (!sectionASeats.isEmpty()) {
            return sectionASeats.remove(0);
        } else if (!sectionBSeats.isEmpty()) {
            return sectionBSeats.remove(0);
        } else {
            throw new SeatAssignmentException("No seats available");
        }
    }

    public boolean isValidSeat(String seat) {
        return train.getSectionASeats().contains(seat) || train.getSectionBSeats().contains(seat);
    }
}

