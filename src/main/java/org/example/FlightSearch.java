package org.example;

import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

public class FlightSearch {
    private static UserDao userDao = new UserDao();
    private static FlightDao flightDao = new FlightDao();

    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        // Desactivar los registros de MongoDB
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        ch.qos.logback.classic.Logger rootLogger = loggerContext.getLogger("org.mongodb.driver");
        rootLogger.setLevel(Level.OFF);

        while (true) {
            System.out.println("1. Agregar un vuelo");
            System.out.println("2. Eliminar un vuelo");
            System.out.println("3. Ejecutar pruebas de rendimiento");
            System.out.println("4. Salir");
            System.out.print("Elige una opción: ");
            int option = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (option) {
                case 1:
                    // Agregar un vuelo
                    System.out.print("Introduce el nombre del usuario: ");
                    String userName = scanner.nextLine();
                    System.out.print("Introduce el código del aeropuerto de origen (por ejemplo, JFK): ");
                    String origin = scanner.nextLine().toUpperCase();
                    System.out.print("Introduce el código del aeropuerto de destino (por ejemplo, LAX): ");
                    String destination = scanner.nextLine().toUpperCase();
                    System.out.print("Introduce el precio del vuelo: ");
                    String price = scanner.nextLine();
                    Flight newFlight = new Flight(origin, destination, price);
                    flightDao.saveFlight(newFlight); // Guarda el vuelo en la colección de vuelos
                    userDao.addFlightToUser(userName, newFlight); // Añade el vuelo a los vuelos del usuario
                    break;
                case 2:
                    // Eliminar un vuelo
                    System.out.print("Introduce el nombre del usuario: ");
                    userName = scanner.nextLine();
                    System.out.print("Introduce el código del aeropuerto de origen del vuelo: ");
                    origin = scanner.nextLine().toUpperCase();
                    System.out.print("Introduce el código del aeropuerto de destino del vuelo: ");
                    destination = scanner.nextLine().toUpperCase();
                    flightDao.deleteFlight(origin, destination); // Borra el vuelo de la colección de vuelos
                    userDao.removeFlightFromUser(userName, origin, destination); // Borra el vuelo de los vuelos del usuario
                    break;
                case 3:
                    // Ejecutar pruebas de rendimiento
                    try {
                        Options opt = new OptionsBuilder()
                                .include(FlightDaoBenchmark.class.getSimpleName())
                                .include(UserDaoBenchmark.class.getSimpleName())
                                .forks(1)
                                .build();
                        new Runner(opt).run();
                    } catch (RunnerException e) {
                        e.printStackTrace();
                    }
                    break;
                case 4:
                    // Salir
                    System.out.println("Saliendo...");
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println("Opción no válida. Por favor, elige una opción del 1 al 4.");
            }
        }
    }
}
