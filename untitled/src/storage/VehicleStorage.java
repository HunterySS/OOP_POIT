package storage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import model.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles JSON serialization and deserialization of vehicle lists.
 */
public class VehicleStorage {
    private static final ObjectMapper mapper = new ObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT);

    /**
     * Saves list of vehicles to JSON file.
     */
    public static void saveToFile(List<Vehicle> vehicles, File file) throws IOException {
        mapper.writeValue(file, vehicles);
    }

    /**
     * Loads list of vehicles from JSON file.
     * Works with both new and old JSON formats.
     */
    public static List<Vehicle> loadFromFile(File file) throws IOException {
        try {
            // Try to load with type information (format saved by this program)
            return mapper.readValue(file,
                    mapper.getTypeFactory().constructCollectionType(List.class, Vehicle.class));
        } catch (IOException e) {
            // If fails, try to load legacy format (manually created JSON)
            System.out.println("Trying to load legacy format...");
            return loadLegacyFormat(file);
        }
    }

    /**
     * Loads vehicles from legacy JSON format (without type field).
     */
    private static List<Vehicle> loadLegacyFormat(File file) throws IOException {
        List<Vehicle> vehicles = new ArrayList<>();
        JsonNode rootNode = new ObjectMapper().readTree(file);

        if (rootNode.isArray()) {
            ArrayNode arrayNode = (ArrayNode) rootNode;
            for (JsonNode node : arrayNode) {
                Vehicle vehicle = parseLegacyVehicle(node);
                if (vehicle != null) {
                    vehicles.add(vehicle);
                }
            }
        }

        if (vehicles.isEmpty()) {
            throw new IOException("No valid vehicles found in file");
        }

        return vehicles;
    }

    /**
     * Parses a vehicle from legacy JSON format (without type field).
     * Determines vehicle type by checking which fields are present.
     */
    private static Vehicle parseLegacyVehicle(JsonNode node) {
        String brand = node.has("brand") ? node.get("brand").asText() : "Unknown";
        int year = node.has("year") ? node.get("year").asInt() : 2000;
        double price = node.has("price") ? node.get("price").asDouble() : 0.0;

        // Check for ElectricCar (has batteryRange field)
        if (node.has("batteryRange")) {
            int doors = node.has("doors") ? node.get("doors").asInt() : 4;
            int batteryRange = node.get("batteryRange").asInt();
            return new ElectricCar(brand, year, price, doors, batteryRange);
        }
        // Check for Truck (has loadCapacity field)
        else if (node.has("loadCapacity")) {
            double capacity = node.get("loadCapacity").asDouble();
            return new Truck(brand, year, price, capacity);
        }
        // Check for Bus (has passengerCapacity field)
        else if (node.has("passengerCapacity")) {
            int capacity = node.get("passengerCapacity").asInt();
            return new Bus(brand, year, price, capacity);
        }
        // Check for Motorcycle (has hasSidecar field)
        else if (node.has("hasSidecar")) {
            boolean hasSidecar = node.get("hasSidecar").asBoolean();
            return new Motorcycle(brand, year, price, hasSidecar);
        }
        // Check for Car (has doors field)
        else if (node.has("doors")) {
            int doors = node.get("doors").asInt();
            return new Car(brand, year, price, doors);
        }
        else {
            // Default to Car if type cannot be determined
            System.out.println("Warning: Unknown vehicle type, creating as Car");
            return new Car(brand, year, price, 4);
        }
    }
}