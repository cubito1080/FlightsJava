package org.example;

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;
import org.bson.Document;

@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class FlightDaoBenchmark {

    private FlightDao flightDao;
    private Document flightDoc;
    private Flight flight;
    private String origin;
    private String destination;

    @Setup
    public void setup() {
        flightDao = new FlightDao();
        flight = new Flight("JFK", "AK", "300");
        flightDoc = flightDao.createFlightDocument(flight);
        origin = "origin";
        destination = "destination";
    }

    @Benchmark
    public void saveFlightBenchmark() {
        flightDao.saveFlight(flight);
    }

    @Benchmark
    public void getAllFlightsBenchmark() {
        flightDao.getAllFlights();
    }

    @Benchmark
    public void updateFlightBenchmark() {
        Flight updatedFlight = new Flight("newOrigin", "newDestination", "newPrice");
        flightDao.updateFlight(origin, destination, updatedFlight);
    }

    @Benchmark
    public void deleteFlightBenchmark() {
        flightDao.deleteFlight(origin, destination);
    }

    @Benchmark
    public void createFlightDocumentBenchmark() {
        flightDao.createFlightDocument(flight);
    }

    @Benchmark
    public void insertFlightBenchmark() {
        flightDao.insertFlight(flightDoc);
    }

    @Benchmark
    public void findAllFlightsBenchmark() {
        flightDao.findAllFlights();
    }

    @Benchmark
    public void createUpdatedFlightDocumentBenchmark() {
        Flight updatedFlight = new Flight("newOrigin", "newDestination", "newPrice");
        flightDao.createUpdatedFlightDocument(updatedFlight);
    }

    @Benchmark
    public void replaceFlightBenchmark() {
        Flight updatedFlight = new Flight("newOrigin", "newDestination", "newPrice");
        Document updatedDoc = flightDao.createUpdatedFlightDocument(updatedFlight);
        flightDao.replaceFlight(origin, destination, updatedDoc);
    }

    @Benchmark
    public void removeFlightBenchmark() {
        flightDao.removeFlight(origin, destination);
    }
}
