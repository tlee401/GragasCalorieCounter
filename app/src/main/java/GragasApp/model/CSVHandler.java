package GragasApp.model;
import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.*;

public class CSVHandler {
    private List<UserProfile> userProfiles = new ArrayList<>();
    private static final String FILE_EXTENSION = ".csv";

    // Method to save a single UserProfile to a CSV file
    public void saveUserProfileToCsv(UserProfile user) throws IOException {
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

    // Method to load all UserProfiles from CSV files in the current directory
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

    private boolean isUserProfileCsv(Path filePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath.toFile()))) {
            String header = reader.readLine();
            return header != null && header.startsWith("UserProfile,Name,Age");
        }
    }

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

    public List<UserProfile> getUserProfiles() {
        return Collections.unmodifiableList(userProfiles);
    }
}
