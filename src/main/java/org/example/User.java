package org.example;

import java.util.ArrayList;
import java.util.List;


public class User {

    private final String name;
    private List<Flight> flights;

    public User(String name) {
        this.name = name;
    }

    public User(String name, List<Flight> flights) {
        this.name = name;
        this.flights = flights;
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


