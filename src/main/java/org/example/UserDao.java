package org.example;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UserDao {
    private final MongoCollection<Document> usersCollection;
    private final FlightDao flightDao; // Add a FlightDao instance

    public UserDao() {
        // Configurar MongoClient con CommandListener
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString("mongodb+srv://jerov79:wGgmDLK6wfam1x2F@cluster0.fua1xqd.mongodb.net/?retryWrites=true&w=majority"))
                .build();

        MongoClient client = MongoClients.create(settings);
        MongoDatabase database = client.getDatabase("UserDB");
        usersCollection = database.getCollection("UsersCollection");
        flightDao = new FlightDao(); // Initialize the FlightDao instance
    }

    // Create User
    public void saveUser(User user) {
        Document doc = new Document("name", user.getName())
                .append("flights", user.getFlights().stream()
                        .map(f -> new Document("origin", f.getOrigin())
                                .append("destination", f.getDestination())
                                .append("price", f.getPrice()))
                        .collect(Collectors.toList()));
        usersCollection.insertOne(doc);
    }

    // Read All Users
    public List<String> getAllUserNames() {
        List<String> userNames = new ArrayList<>();
        for (Document doc : usersCollection.find()) {
            userNames.add(doc.getString("name"));
        }
        return userNames;
    }

    // Read User Flights
    public List<Flight> getUserFlights(String userName) {
        Document userDoc = usersCollection.find(Filters.eq("name", userName)).first();
        List<Flight> flights = new ArrayList<>();
        if (userDoc != null) {
            List<Document> flightDocs = (List<Document>) userDoc.get("flights");
            if (flightDocs != null) {
                for (Document flightDoc : flightDocs) {
                    Flight flight = new Flight(
                            flightDoc.getString("origin"),
                            flightDoc.getString("destination"),
                            flightDoc.getString("price")
                    );
                    flights.add(flight);
                }
            }
        }
        return flights;
    }

    // Update User
    public void updateUser(String name, User updatedUser) {
        Document updatedDoc = new Document("name", updatedUser.getName())
                .append("flights", updatedUser.getFlights().stream()
                        .map(f -> new Document("origin", f.getOrigin())
                                .append("destination", f.getDestination())
                                .append("price", f.getPrice()))
                        .collect(Collectors.toList()));
        usersCollection.replaceOne(Filters.eq("name", name), updatedDoc);
    }

    // Delete User
    public void deleteUser(String name) {
        usersCollection.deleteOne(Filters.eq("name", name));
    }

    // Add a Flight to a User
    public void addFlightToUser(String name, Flight flight) {
        flightDao.saveFlight(flight); // Save the flight using FlightDao
        Document flightDoc = new Document("origin", flight.getOrigin())
                .append("destination", flight.getDestination())
                .append("price", flight.getPrice());
        usersCollection.updateOne(Filters.eq("name", name), new Document("$push", new Document("flights", flightDoc)));
    }

    // Remove a Flight from a User
    public void removeFlightFromUser(String name, Flight flight) {
        Document flightDoc = new Document("origin", flight.getOrigin())
                .append("destination", flight.getDestination())
                .append("price", flight.getPrice());
        usersCollection.updateOne(Filters.eq("name", name), new Document("$pull", new Document("flights", flightDoc)));
        flightDao.deleteFlight(flight.getOrigin(), flight.getDestination()); // Delete the flight using FlightDao
    }


    public Document createUserDocument(User user) {
        return new Document("name", user.getName())
                .append("flights", user.getFlights().stream()
                        .map(flightDao::createFlightDocument)
                        .collect(Collectors.toList()));
    }

    public void insertUser(Document doc) {
        usersCollection.insertOne(doc);
    }

    public List<Document> findAllUsers() {
        List<Document> docs = new ArrayList<>();
        for (Document doc : usersCollection.find()) {
            docs.add(doc);
        }
        return docs;
    }

    public Document findUser(String userName) {
        return usersCollection.find(Filters.eq("name", userName)).first();
    }

    public Document createUpdatedUserDocument(User updatedUser) {
        return new Document("name", updatedUser.getName())
                .append("flights", updatedUser.getFlights().stream()
                        .map(flightDao::createFlightDocument)
                        .collect(Collectors.toList()));
    }

    public void replaceUser(String name, Document updatedDoc) {
        usersCollection.replaceOne(Filters.eq("name", name), updatedDoc);
    }

    public void removeUser(String name) {
        usersCollection.deleteOne(Filters.eq("name", name));
    }

    public Document createFlightDocument(Flight flight) {
        return new Document("origin", flight.getOrigin())
                .append("destination", flight.getDestination())
                .append("price", flight.getPrice());
    }

    public void addFlightToUser(String name, Document flightDoc) {
        usersCollection.updateOne(Filters.eq("name", name), new Document("$push", new Document("flights", flightDoc)));
    }

    public void removeFlightFromUser(String name, String origin, String destination) {
        Document flightDoc = createFlightDocument(new Flight(origin, destination, ""));
        usersCollection.updateOne(Filters.eq("name", name), new Document("$pull", new Document("flights", flightDoc)));
    }

}
