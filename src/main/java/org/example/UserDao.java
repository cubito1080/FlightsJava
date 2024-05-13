package org.example;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class UserDao {
    private final MongoCollection<Document> usersCollection;
    private final FlightDao flightDao; // Add a FlightDao instance

    public UserDao() {
        MongoClient client = MongoClients.create("mongodb+srv://jerov79:wGgmDLK6wfam1x2F@cluster0.fua1xqd.mongodb.net/?retryWrites=true&w=majority"); // Adjust the URI as needed
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
    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        for (Document doc : usersCollection.find()) {
            User user = new User(doc.getString("name"));
            List<Document> flightsList = new ArrayList<>();
            Object flights = doc.get("flights");
            if (flights instanceof List) {
                for (Object flightObj : (List<?>) flights) {
                    if (flightObj instanceof Document) {
                        Document flightDoc = (Document) flightObj;
                        Flight flight = new Flight(
                                flightDoc.getString("origin"),
                                flightDoc.getString("destination"),
                                flightDoc.getString("price")
                        );
                        user.addFlight(flight);
                    }
                }
            }
            userList.add(user);
        }
        return userList;
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
}
