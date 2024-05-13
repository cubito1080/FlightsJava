package org.example;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class FlightDao {
    private final MongoCollection<Document> flightsCollection;

    public FlightDao() {
        MongoClient client = MongoClients.create("mongodb+srv://jerov79:wGgmDLK6wfam1x2F@cluster0.fua1xqd.mongodb.net/?retryWrites=true&w=majority"); // Adjust the URI as needed
        MongoDatabase database = client.getDatabase("FlightsDB");
        flightsCollection = database.getCollection("FlightsCollection");
    }

    // Create Flight
    public void saveFlight(Flight flight) {
        Document doc = new Document("origin", flight.getOrigin())
                .append("destination", flight.getDestination())
                .append("price", flight.getPrice());
        flightsCollection.insertOne(doc);
    }

    // Read All Flights
    public List<Flight> getAllFlights() {
        List<Flight> flightList = new ArrayList<>();
        for (Document doc : flightsCollection.find()) {
            Flight flight = new Flight(
                    doc.getString("origin"),
                    doc.getString("destination"),
                    doc.getString("price")
            );
            flightList.add(flight);
        }
        return flightList;
    }

    // Update Flight
    public void updateFlight(String origin, String destination, Flight updatedFlight) {
        Document updatedDoc = new Document("origin", updatedFlight.getOrigin())
                .append("destination", updatedFlight.getDestination())
                .append("price", updatedFlight.getPrice());
        flightsCollection.replaceOne(Filters.and(Filters.eq("origin", origin), Filters.eq("destination", destination)), updatedDoc);
    }

    // Delete Flight
    public void deleteFlight(String origin, String destination) {
        flightsCollection.deleteOne(Filters.and(Filters.eq("origin", origin), Filters.eq("destination", destination)));
    }
}
