package org.example;

import java.util.ArrayList;
import java.util.List;


public class User {
    private final String name;
    private final List<Flight> flights;

    public User(String name) {
        this.name = name;
        this.flights = new ArrayList<>();
    }

    public void addFlight(Flight flight) {
        this.flights.add(flight);
    }

    public List<Flight> getFlights() {
        return flights;
    }

    public String getName() {
        return name;
    }
}


