package org.example;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.event.CommandListener;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class FlightDao {
    private final MongoCollection<Document> flightsCollection;

    public FlightDao() {
        // Configurar MongoClient con CommandListener

        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString("mongodb+srv://jerov79:wGgmDLK6wfam1x2F@cluster0.fua1xqd.mongodb.net/?retryWrites=true&w=majority"))
                .build();

        MongoClient client = MongoClients.create(settings);
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


    public Document createFlightDocument(Flight flight) {
        return new Document("origin", flight.getOrigin())
                .append("destination", flight.getDestination())
                .append("price", flight.getPrice());
    }

    public void insertFlight(Document doc) {
        flightsCollection.insertOne(doc);
    }

    public List<Document> findAllFlights() {
        List<Document> docs = new ArrayList<>();
        for (Document doc : flightsCollection.find()) {
            docs.add(doc);
        }
        return docs;
    }

    public Document createUpdatedFlightDocument(Flight updatedFlight) {
        return new Document("origin", updatedFlight.getOrigin())
                .append("destination", updatedFlight.getDestination())
                .append("price", updatedFlight.getPrice());
    }

    public void replaceFlight(String origin, String destination, Document updatedDoc) {
        flightsCollection.replaceOne(Filters.and(Filters.eq("origin", origin), Filters.eq("destination", destination)), updatedDoc);
    }

    public void removeFlight(String origin, String destination) {
        flightsCollection.deleteOne(Filters.and(Filters.eq("origin", origin), Filters.eq("destination", destination)));
    }
}
