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

    @Setup
    public void setup() {
        flightDao = new FlightDao();
        Flight flight = new Flight("origin", "destination", "price");
        flightDoc = flightDao.createFlightDocument(flight);
    }

    @Benchmark
    public void insertFlightBenchmark() {
        flightDao.insertFlight(flightDoc);
    }
}
