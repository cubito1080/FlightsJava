package org.example;

import com.amadeus.Amadeus;
import com.amadeus.Params;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.FlightOfferSearch;

import java.util.Arrays;
import java.util.Scanner;

public class FlightSearch {
    private static Amadeus amadeus = Amadeus
            .builder("fOAkvzrTMnEJLsL2r8FJqEkFZsU0AGuV", "I2ZrjKquCArEiAbv")
            .build();

    public static void main(String[] args){
        UserDao userDao = new UserDao();
        FlightDao flightDao = new FlightDao();

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter user name: ");
        String userName = scanner.nextLine();
        System.out.print("Enter origin airport code (e.g., JFK): ");
        String origin = scanner.nextLine().toUpperCase();
        System.out.print("Enter destination airport code (e.g., LAX): ");
        String destination = scanner.nextLine().toUpperCase();
        System.out.print("Enter departure date (YYYY-MM-DD): ");
        String departDate = scanner.nextLine();
        System.out.print("Enter return date (YYYY-MM-DD): ");
        String returnDate = scanner.nextLine();
        System.out.print("Enter number of adults: ");
        String adults = scanner.nextLine();

        try {
            FlightOfferSearch[] results = flights(origin, destination, departDate, adults, returnDate);
            if (results.length > 0) {
                System.out.println("Available flights found:");
                for (FlightOfferSearch flight : results) {
                    System.out.println("--------------------------------------------------");
                    System.out.println("Price: " + flight.getPrice().getTotal() + " " + flight.getPrice().getCurrency());
                    Flight newFlight = new Flight(origin, destination, String.valueOf(flight.getPrice().getTotal()));
                    flightDao.saveFlight(newFlight); // Save the flight in the Flights collection
                    userDao.addFlightToUser(userName, newFlight); // Add the flight to the user's flights
                }
            } else {
                System.out.println("No flights available.");
            }
        } catch (ResponseException e) {
            System.err.println("Error fetching flights: " + e.getDescription());
        }

        scanner.close();
    }

    public static FlightOfferSearch[] flights(String origin, String destination, String departDate, String adults, String returnDate) throws ResponseException {
        return amadeus.shopping.flightOffersSearch.get(
                Params.with("originLocationCode", origin)
                        .and("destinationLocationCode", destination)
                        .and("departureDate", departDate)
                        .and("returnDate", returnDate)
                        .and("adults", adults)
                        .and("max", 3));
    }
}
