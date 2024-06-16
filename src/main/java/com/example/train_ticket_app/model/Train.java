package com.example.train_ticket_app.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Train {
    private List<String> sectionASeats;
    private List<String> sectionBSeats;

    public Train() {
        sectionASeats = new ArrayList<>(Arrays.asList("A1", "A2", "A3", "A4", "A5"));
        sectionBSeats = new ArrayList<>(Arrays.asList("B1", "B2", "B3", "B4", "B5"));
    }

    // Getters and Setters
    public List<String> getSectionASeats() {
        return sectionASeats;
    }

    public void setSectionASeats(List<String> sectionASeats) {
        this.sectionASeats = sectionASeats;
    }

    public List<String> getSectionBSeats() {
        return sectionBSeats;
    }

    public void setSectionBSeats(List<String> sectionBSeats) {
        this.sectionBSeats = sectionBSeats;
    }
}

