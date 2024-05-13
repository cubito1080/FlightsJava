package org.example;

public class Flight {
    private String origin;
    private String destination;
    private String price;

    // Constructor
    public Flight(String origin, String destination, String price) {
        this.origin = origin;
        this.destination = destination;
        this.price = price;
    }

    // Getter for origin
    public String getOrigin() {
        return origin;
    }

    // Setter for origin
    public void setOrigin(String origin) {
        this.origin = origin;
    }

    // Getter for destination
    public String getDestination() {
        return destination;
    }

    // Setter for destination
    public void setDestination(String destination) {
        this.destination = destination;
    }

    // Getter for price
    public String getPrice() {
        return price;
    }

    // Setter for price
    public void setPrice(String price) {
        this.price = price;
    }
}
