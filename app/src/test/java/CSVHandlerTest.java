import GragasApp.model.*;
// import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

/**
 * A test class for the CSVHandler to demonstrate its functionality.
 * This class creates a UserProfile, saves it to a CSV, and then
 * reads it back to verify the data was correctly written and parsed.
 */
public class CSVHandlerTest {

    public static void main(String[] args) {
        // --- 1. Create a UserProfile to test with ---
        System.out.println("--- Creating test UserProfile ---");
        UserProfile user1 = UserProfile.fromImperial(
            "TestUser", 30, 70, // name, age, height in inches
            ActivityLevel.MODERATE, Sex.MALE,
            180, 175); // weight in lbs, target weight in lbs

        // Add some daily logs with food entries
        DailyLog log1 = new DailyLog(LocalDate.now());
        
        try {
            log1.addEntry(new FoodEntry("Oatmeal"));
            log1.addEntry(new FoodEntry("Coffee"));
        } catch (Exception e) {
            System.err.println("Failed to create FoodEntry for log1: " + e.getMessage());
            e.printStackTrace();
        }
        user1.addLog(log1);

        DailyLog log2 = new DailyLog(LocalDate.now().minusDays(1));
        try {
            log2.addEntry(new FoodEntry("Chicken Salad"));
            log2.addEntry(new FoodEntry("Apple"));
        } catch (Exception e) {
            System.err.println("Failed to create FoodEntry for log2: " + e.getMessage());
            e.printStackTrace();
        }
        user1.addLog(log2);

        System.out.println("Original UserProfile created: " + user1.getName());
        System.out.println("Original UserProfile has " + user1.getLogs().size() + " daily logs.");

        // --- 2. Test saving and loading with CSVHandler ---
        CSVHandler csvHandler = new CSVHandler();
        String fileName = user1.getName() + ".csv";
        // File testFile = new File(fileName);

        try {
            // Save the user profile to a CSV file
            System.out.println("\n--- Saving UserProfile to " + fileName + " ---");
            csvHandler.saveUserProfileToCsv(user1);
            System.out.println("Save successful!");

            // Create a new handler to simulate a fresh application start
            CSVHandler newCsvHandler = new CSVHandler();
            
            // Load user profiles from the directory
            System.out.println("\n--- Loading UserProfiles from CSV files ---");
            newCsvHandler.loadUserProfilesFromCsvs();

            // Get the loaded user profiles
            List<UserProfile> loadedUsers = newCsvHandler.getUserProfiles();
            if (loadedUsers.isEmpty()) {
                System.out.println("No users loaded. Test failed.");
                return;
            }
            UserProfile loadedUser = loadedUsers.get(2);
            System.out.println("Loaded " + loadedUsers.size() + " user(s).");
            System.out.println("Loaded UserProfile: " + loadedUser.getName());

            // --- 3. Compare original and loaded data ---
            System.out.println("\n--- Verifying data integrity ---");
            boolean success = true;
            
            // Check core user profile attributes
            if (!user1.getName().equals(loadedUser.getName())) {
                success = false;
                System.out.println("Error: Name mismatch. Expected: " + user1.getName() + ", Found: " + loadedUser.getName());
            }
            if (user1.getWeightKg() != loadedUser.getWeightKg()) {
                success = false;
                System.out.println("Error: Weight mismatch. Expected: " + user1.getWeightKg() + ", Found: " + loadedUser.getWeightKg());
            }
            
            // Check daily logs and entries
            if (user1.getLogs().size() != loadedUser.getLogs().size()) {
                success = false;
                System.out.println("Error: Log count mismatch. Expected: " + user1.getLogs().size() + ", Found: " + loadedUser.getLogs().size());
            } else {
                // Assuming the logs are in the same order
                for (int i = 0; i < user1.getLogs().size(); i++) {
                    DailyLog originalLog = user1.getLogs().get(i);
                    DailyLog loadedLog = loadedUser.getLogs().get(i);
                    if (!originalLog.getDate().equals(loadedLog.getDate())) {
                         success = false;
                         System.out.println("Error: Log date mismatch for log " + i);
                    }
                    if (originalLog.getEntries().size() != loadedLog.getEntries().size()) {
                         success = false;
                         System.out.println("Error: Entry count mismatch for log " + originalLog.getDate());
                    }
                    // A more thorough test would compare each individual food entry
                }
            }

            if (success) {
                System.out.println("Verification successful! Data loaded matches original data.");
            } else {
                System.out.println("Verification failed. Data does not match.");
            }

        } catch (IOException e) {
            System.err.println("An error occurred during file operations: " + e.getMessage());
            e.printStackTrace();
        } finally {
            System.out.println("\nCleaned up test file: " + fileName);
            // Clean up: delete the created file
            // if (testFile.exists()) {
            //     if (testFile.delete()) {
            //         System.out.println("\nCleaned up test file: " + fileName);
            //     } else {
            //         System.err.println("\nFailed to delete the test file: " + fileName);
            //     }
            // }
        }
    }
}


