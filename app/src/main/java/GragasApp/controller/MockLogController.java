package GragasApp.controller;
import GragasApp.model.*;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

/**
 * Mock LogController for testing the GUI.
 * Provides a simple list of entries.
 */
public class MockLogController {

    private final List<Loggable> mockEntries = new ArrayList<>();

    public MockLogController() {
        // Add some mock entries for demonstration
        mockEntries.add(new FoodEntry("Mock Cereal", 350));
        mockEntries.add(new FoodEntry("Mock Milk", 150));
    }

    public void addFood(FoodEntry entry, LocalDate date) {
        System.out.println("Mock: Adding food '" + entry.getName() + "' to log for " + date);
        mockEntries.add(entry);
    }

    public void removeFood(FoodEntry entry, LocalDate date) {
        System.out.println("Mock: Removing food '" + entry.getName() + "' from log for " + date);
        mockEntries.remove(entry);
    }

    public List<Loggable> getEntries(LocalDate date) {
        System.out.println("Mock: Getting entries for " + date);
        return mockEntries;
    }
}
