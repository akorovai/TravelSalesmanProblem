package src.akorovai.climbingalgorithm;

import src.akorovai.climbingalgorithm.entities.City;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class App {
    private static final double INITIAL_TEMPERATURE = 100.0;
    private static final double MIN_TEMPERATURE = 0.001;
    private static final double COOLING_RATE = 0.9;

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Please provide the file path as an argument.");
            return;
        }

        String filePath = args[0];
        try {
            System.out.println("Reading the file...");
            String[] fileContent = Files.readAllLines(Paths.get(filePath)).toArray(String[]::new);
            System.out.println("Reading successfully completed!\n");

            System.out.println("Preparing data for analysis...");
            List<City> cities = parseCities(fileContent);
            System.out.println("Data preparation completed!\n");

            System.out.print("Press enter to continue: ");
            waitForEnterKey();

            System.out.println("Cities and Distances:");
            printCityDistances(cities);

            System.out.println("Solving TSP using simulated annealing algorithm...");
            List<Integer> initialRoute = generateInitialRoute(cities.size());
            System.out.println("Initial Route: " + initialRoute);

            List<Integer> bestRoute = simulatedAnnealing(cities, initialRoute);
            int bestDistance = calculateDistance(cities, bestRoute);

            System.out.println("Best route: " + bestRoute);
            System.out.println("Distance: " + bestDistance);

            System.out.println("Cities and Distances in Best Route:");
            printCityDistances(cities, bestRoute);
        } catch (IOException e) {
            System.out.println("Error occurred while reading the file: " + e.getMessage());
        }
    }

    private static List<City> parseCities(String[] fileContent) {
        int numberCities = Integer.parseInt(fileContent[0]);
        List<City> cities = new ArrayList<>(numberCities);

        for (int j = 1; j < fileContent.length; j++) {
            String[] elements = fileContent[j].split(" ");

            if (elements.length != 3) {
                throw new IllegalArgumentException("Invalid input format at line: " + j);
            }

            int cityIndex = Integer.parseInt(elements[0]);
            int otherCityIndex = Integer.parseInt(elements[1]);
            int distance = Integer.parseInt(elements[2]);

            validateCityIndex(numberCities, cityIndex);
            validateCityIndex(numberCities, otherCityIndex);

            City city = findOrCreateCity(cities, cityIndex);
            city.addNeighbor(otherCityIndex, distance);

            City otherCity = findOrCreateCity(cities, otherCityIndex);
            otherCity.addNeighbor(cityIndex, distance);
        }

        return cities;
    }

    private static void validateCityIndex(int numberCities, int cityIndex) {
        if (cityIndex < 0 || cityIndex >= numberCities) {
            throw new IllegalArgumentException("Invalid city index: " + cityIndex);
        }
    }

    private static City findOrCreateCity(List<City> cities, int cityIndex) {
        return cities.stream()
                .filter(city -> city.getIdCity() == cityIndex)
                .findFirst()
                .orElseGet(() -> {
                    City newCity = new City(cityIndex);
                    cities.add(newCity);
                    return newCity;
                });
    }

    private static void waitForEnterKey() {
        try {
            int input = System.in.read();
            while (input != '\n' && input != '\r') {
                input = System.in.read();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<Integer> generateInitialRoute(int numCities) {
        List<Integer> route = new ArrayList<>(numCities);
        for (int i = 0; i < numCities; i++) {
            route.add(i);
        }
        Collections.shuffle(route);
        return route;
    }

    private static int calculateDistance(List<City> cities, List<Integer> route) {
        int distance = 0;
        for (int i = 0; i < route.size(); i++) {
            int cityIndex1 = route.get(i);
            int cityIndex2 = route.get((i + 1) % route.size());
            City city1 = findCity(cities, cityIndex1);
            distance += city1.getDistanceToNeighbor(cityIndex2);
        }
        return distance;
    }

    private static List<Integer> simulatedAnnealing(List<City> cities, List<Integer> initialRoute) {
        List<Integer> currentRoute = new ArrayList<>(initialRoute);
        List<Integer> bestRoute = new ArrayList<>(initialRoute);
        int currentDistance = calculateDistance(cities, currentRoute);
        int bestDistance = currentDistance;
        double temperature = INITIAL_TEMPERATURE;

        int iteration = 1;

        while (temperature > MIN_TEMPERATURE) {
            List<Integer> newRoute = new ArrayList<>(currentRoute);
            int randomIndex1 = getRandomIndex(newRoute.size());
            int randomIndex2 = getRandomIndex(newRoute.size());

            Collections.swap(newRoute, randomIndex1, randomIndex2);

            int newDistance = calculateDistance(cities, newRoute);
            int deltaDistance = newDistance - currentDistance;

            if (acceptMove(deltaDistance, temperature)) {
                currentRoute = newRoute;
                currentDistance = newDistance;

                if (currentDistance < bestDistance) {
                    bestRoute = new ArrayList<>(currentRoute);
                    bestDistance = currentDistance;
                }
            }

            temperature *= COOLING_RATE;

            System.out.println("Iteration: " + iteration);
            System.out.println("Temperature: " + temperature);
            System.out.println("Current Distance: " + currentDistance);
            System.out.println("Current Route: " + currentRoute);
            System.out.println("--------------------------------------");

            iteration++;
        }

        return bestRoute;
    }

    private static int getRandomIndex(int size) {
        return (int) (Math.random() * size);
    }

    private static boolean acceptMove(int deltaDistance, double temperature) {
        return deltaDistance < 0 || Math.random() < Math.exp(-deltaDistance / temperature);
    }

    private static City findCity(List<City> cities, int cityIndex) {
        return cities.stream()
                .filter(city -> city.getIdCity() == cityIndex)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("City not found: " + cityIndex));
    }

    private static void printCityDistances(List<City> cities) {
        cities.forEach(city -> {
            System.out.println("City ID: " + city.getIdCity());
            System.out.println("Distances: " + city.getDistancesToNeighbors());

        });
    }

    private static void printCityDistances(List<City> cities, List<Integer> route) {
        for (int i = 0; i < route.size(); i++) {
            int cityIndex1 = route.get(i);
            int cityIndex2 = route.get((i + 1) % route.size());
            City city1 = findCity(cities, cityIndex1);
            int distance = city1.getDistanceToNeighbor(cityIndex2);
            System.out.println("From City " + cityIndex1 + " to City " + cityIndex2 + ": Distance = " + distance);
        }
    }
}
