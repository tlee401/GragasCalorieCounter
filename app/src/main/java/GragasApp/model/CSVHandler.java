package GragasApp.model;
import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.*;

/**
 * CSV persistence utility for {@link UserProfile} objects and their daily logs.
 *
 * This class can:
 * Save a single UserProfile and its DailyLog entries to a CSV file
 * named <userName>.csv in the current working directory,
 * Update an already-saved @link UserProfile by overwriting its CSV file,
 * Load all user profiles from CSV files found in the current directory.
 *
 */

public class CSVHandler {
    /**
     * In-memory collection of loaded/saved users
     */
    private List<UserProfile> userProfiles = new ArrayList<>();

    /**
     * File extension appended to usernames to form CSV filenames.
     */
    private static final String FILE_EXTENSION = ".csv";

    /**
     * Saves a UserProfile to a CSV file named <userName>.csv and adds the
     * user to the in-memory list.
     *
     * The CSV will contain a UserProfile section followed by a DailyLog
     * section.
     *
     * @param user the profile to persist
     * @throws IOException if an I/O error occurs while writing the file
     * @throws IllegalArgumentException if another user with the same {@code name} is already
     *         present in the in-memory list
     */
    public void saveUserProfileToCsv(UserProfile user) throws IOException {
        // Check if a user with the same name already exists in the list
        for (UserProfile existingUser : userProfiles) {
            if (existingUser.getName().equals(user.getName())) {
                throw new IllegalArgumentException("A user with the name '" + user.getName() + "' already exists.");
            }
        }
        
        String fileName = user.getName() + FILE_EXTENSION;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            // Write UserProfile header and data
            writer.write("UserProfile,Name,Age,HeightCm,ActivityLevel,Sex,WeightKg,TargetWeightKg\n");
            writer.write(String.format("UserProfile,%s,%d,%d,%s,%s,%.2f,%.2f\n",
                    user.getName(), user.getAge(), user.getHeightCm(),
                    user.getActivityLevel(), user.getSex(),
                    user.getWeightKg(), user.getTargetWeightKg()));

            // Write DailyLog entries header
            writer.write("\nDailyLog,Date,LoggableName,Calories\n");

            // Write each Loggable entry on a new line
            for (DailyLog log : user.getLogs()) {
                for (Loggable entry : log.getEntries()) {
                    writer.write(String.format("DailyLog,%s,%s,%.2f\n",
                            log.getDate().toString(),
                            entry.getName(),
                            entry.getCalories()));
                }
            }
        }
        userProfiles.add(user);
    }

    /**
     * Overwrites the CSV file for an existing UserProfile and keeps the in-memory list unchanged.
     *
     * @param user the updated profile to persist
     * @throws IOException if an I/O error occurs while writing the file
     * @throws IllegalArgumentException if no user with the same name is present in the in-memory list
     */
    public void updateUserProfileToCsv(UserProfile user) throws IOException {
        boolean here = false;
        for (UserProfile existingUser : userProfiles) {
            if (existingUser.getName().equals(user.getName())) {
                here = true;
                break;
            }
        }
        if (!here) {
            throw new IllegalArgumentException("A user with the name '" + user.getName() + "' does not already exists.");
        }

        String fileName = user.getName() + FILE_EXTENSION;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            // Write UserProfile header and data
            writer.write("UserProfile,Name,Age,HeightCm,ActivityLevel,Sex,WeightKg,TargetWeightKg\n");
            writer.write(String.format("UserProfile,%s,%d,%d,%s,%s,%.2f,%.2f\n",
                    user.getName(), user.getAge(), user.getHeightCm(),
                    user.getActivityLevel(), user.getSex(),
                    user.getWeightKg(), user.getTargetWeightKg()));

            // Write DailyLog entries header
            writer.write("\nDailyLog,Date,LoggableName,Calories\n");

            // Write each Loggable entry on a new line
            for (DailyLog log : user.getLogs()) {
                for (Loggable entry : log.getEntries()) {
                    writer.write(String.format("DailyLog,%s,%s,%.2f\n",
                            log.getDate().toString(),
                            entry.getName(),
                            entry.getCalories()));
                }
            }
        }
    }

    /**
     * Loads all UserProfile instances from *.csv files in the current
     * working directory and appends them to the in-memory list.
     *
     * Only files whose first header line starts with {"UserProfile,Name,Age"}
     * are considered user profile CSVs
     *
     * @throws IOException if an I/O error occurs while enumerating or reading files
     */
    public void loadUserProfilesFromCsvs() throws IOException {
        Path currentDir = Paths.get(".");
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(currentDir, "*" + FILE_EXTENSION)) {
            for (Path filePath : stream) {
                if (isUserProfileCsv(filePath)) {
                    UserProfile user = readUserProfileFromCsv(filePath);
                    if (user != null) {
                        userProfiles.add(user);
                    }
                }
            }
        }
    }

    /**
     * checks whether a file appears to be a user profile CSV by
     * inspecting the first line for the expected header.
     *
     * @param filePath path to the candidate CSV file
     * @return {true} if the header begins with {@code "UserProfile,Name,Age"},
     * otherwise {false}
     * @throws IOException if an I/O error occurs while reading the file
     */
    private boolean isUserProfileCsv(Path filePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath.toFile()))) {
            String header = reader.readLine();
            return header != null && header.startsWith("UserProfile,Name,Age");
        }
    }

    /**
     * Parses a single CSV file into a {UserProfile}, including its {DailyLog} entries.
     *
     * Assumes the file uses the format documented at the class level. Lines that cannot be parsed
     * will cause the method to log a message to {System.err} and return {null}.
     *
     * @param filePath path to the CSV file to read
     * @return the reconstructed {UserProfile}, or {null} if a parsing error occurs
     * @throws IOException if an I/O error occurs while reading the file
     */
    private UserProfile readUserProfileFromCsv(Path filePath) throws IOException {
        UserProfile user = null;
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath.toFile()))) {
            String line;
            Map<LocalDate, DailyLog> dailyLogs = new HashMap<>();

            // Skip header lines
            reader.readLine(); // UserProfile header
            String userProfileLine = reader.readLine();
            if (userProfileLine != null && userProfileLine.startsWith("UserProfile,")) {
                String[] parts = userProfileLine.split(",");
                String name = parts[1];
                int age = Integer.parseInt(parts[2]);
                int heightCm = Integer.parseInt(parts[3]);
                ActivityLevel activityLevel = ActivityLevel.valueOf(parts[4]);
                Sex sex = Sex.valueOf(parts[5]);
                double weightKg = Double.parseDouble(parts[6]);
                double targetWeightKg = Double.parseDouble(parts[7]);

                user = new UserProfile(name, age, heightCm, activityLevel, sex, weightKg, targetWeightKg);
            }

            // Skip the DailyLog header
            reader.readLine();
            reader.readLine(); // Empty line

            // Read DailyLog entries
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("DailyLog,")) {
                    String[] parts = line.split(",");
                    LocalDate date = LocalDate.parse(parts[1]);
                    String foodName = parts[2];
                    double calories = Double.parseDouble(parts[3]);

                    DailyLog log = dailyLogs.getOrDefault(date, new DailyLog(date));
                    log.addEntry(new FoodEntry(foodName, calories));
                    dailyLogs.put(date, log);
                }
            }
            if (user != null) {
                for (DailyLog log : dailyLogs.values()) {
                    user.addLog(log);
                }
            }

        } catch (Exception e) {
            System.err.println("Error reading file: " + filePath + " - " + e.getMessage());
            return null; // Return null if there's an error
        }
        return user;
    }

    /**
     * Returns an unmodifiable view of the in-memory user profiles that have been
     * saved or loaded during this process lifetime.
     *
     * @return read-only list of users
     */
    public List<UserProfile> getUserProfiles() {
        return Collections.unmodifiableList(userProfiles);
    }
}
