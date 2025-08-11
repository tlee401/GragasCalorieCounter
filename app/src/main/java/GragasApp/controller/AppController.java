package GragasApp.controller;

import GragasApp.model.*;
import GragasApp.view.MainView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * The main controller for the application. It handles user interactions from the View,
 * communicates with the Model, and updates the View with new data.
 */
public class AppController {

    private final MainView view;
    private final CSVHandler csvHandler;
    private final CalorieCalculator calorieCalculator;

    private UserProfile currentUser;

    public AppController(MainView view, CSVHandler csvHandler) {
        this.view = view;
        this.csvHandler = csvHandler;
        this.calorieCalculator = new MifflinStJeorCalculator();

        // Load existing profiles and populate the view
        loadInitialData();

        // Attach listeners to the view components
        attachListeners();
    }

    /**
     * Loads initial user profiles from CSV files and populates the user selection combo box.
     */
    private void loadInitialData() {
        try {
            csvHandler.loadUserProfilesFromCsvs();
            List<UserProfile> profiles = csvHandler.getUserProfiles();
            view.getDashboardView().setVisible(false); // Hide dashboard initially
            view.getUserSelectionView().populateUserList(profiles);
        } catch (IOException e) {
            view.showError("Failed to load user profiles from CSV files: " + e.getMessage());
        }
    }

    /**
     * Attaches all necessary action listeners to the GUI components in the MainView.
     */
    private void attachListeners() {
        // User Selection View Listeners
        view.getUserSelectionView().addLoadProfileListener(this::handleLoadProfile);
        view.getUserSelectionView().addCreateProfileListener(this::handleCreateProfile);

        // Dashboard View Listeners
        view.getDashboardView().addAddFoodListener(this::handleAddFood);
        view.getDashboardView().addSaveChangesListener(this::handleSaveChanges);
        view.getDashboardView().addSwitchUserListener(this::handleSwitchUser);
    }

    /**
     * Handles the "Load Profile" action from the UserSelectionView.
     */
    private void handleLoadProfile(ActionEvent e) {
        String selectedUserName = view.getUserSelectionView().getSelectedUser();
        if (selectedUserName == null || selectedUserName.isEmpty()) {
            view.showError("Please select a profile to load.");
            return;
        }

        Optional<UserProfile> userOpt = csvHandler.getUserProfiles().stream()
                .filter(p -> p.getName().equals(selectedUserName))
                .findFirst();

        if (userOpt.isPresent()) {
            currentUser = userOpt.get();
            updateDashboard();
            view.showDashboard();
        } else {
            view.showError("Could not find the selected profile.");
        }
    }

    /**
     * Handles the "Create Profile" action from the UserSelectionView.
     * It validates form input, creates a UserProfile, saves it, and switches to the dashboard.
     */
    private void handleCreateProfile(ActionEvent e) {
        try {
            // Retrieve data from the creation form
            String name = view.getUserSelectionView().getNewUserName();
            int age = view.getUserSelectionView().getAge();
            double height = view.getUserSelectionView().getHeight();
            double weight = view.getUserSelectionView().getWeight();
            double targetWeight = view.getUserSelectionView().getTargetWeight();
            Sex sex = view.getUserSelectionView().getSex();
            ActivityLevel activityLevel = view.getUserSelectionView().getActivityLevel();
            boolean useImperial = view.getUserSelectionView().isImperial();

            if (name.trim().isEmpty()) {
                view.showError("User name cannot be empty.");
                return;
            }

            // Create user profile
            if (useImperial) {
                currentUser = UserProfile.fromImperial(name, age, (int) height, activityLevel, sex, weight, targetWeight);
            } else {
                currentUser = new UserProfile(name, age, (int) height, activityLevel, sex, weight, targetWeight);
            }

            // Save the new user profile via CSVHandler
            csvHandler.saveUserProfileToCsv(currentUser);

            // Update the view
            updateDashboard();
            view.getUserSelectionView().addUserToList(currentUser); // Add to dropdown
            view.getUserSelectionView().clearCreationFields();
            view.showDashboard();

        } catch (NumberFormatException nfe) {
            view.showError("Invalid number format. Please check age, height, and weight.");
        } catch (IllegalArgumentException iae) {
            view.showError("Failed to create profile: " + iae.getMessage());
        } catch (IOException ioe) {
            view.showError("Failed to save the new profile: " + ioe.getMessage());
        }
    }

    /**
     * Handles adding a new food entry from the dashboard.
     */
    private void handleAddFood(ActionEvent e) {
        String foodDescription = view.getDashboardView().getFoodInput();
        if (foodDescription.trim().isEmpty()) {
            view.showError("Please enter a food name.");
            return;
        }

        try {
            // Use the service to get calorie info
            FoodEntry newFood = new FoodEntry(foodDescription);

            // Get today's log, or create it if it doesn't exist
            DailyLog todayLog = getTodaysLog();
            todayLog.addEntry(newFood);

            // Refresh the view
            updateFoodLogTable(todayLog);
            updateCalorieSummary();

        } catch (Exception ex) {
            view.showError("Could not add food: " + ex.getMessage());
        }
    }
    
    /**
     * Handles the "Save Changes" action from the dashboard.
     * Note: In this design, saving happens automatically on creation and explicitly via this button.
     * For simplicity, this re-saves the entire profile.
     */
    private void handleSaveChanges(ActionEvent e) {
        if (currentUser == null) return;
        try {
            // The CSVHandler provided only saves new users, not updates.
            // For a real app, CSVHandler would need an updateUser method.
            // We'll show a message instead.
            // view.showMessage("Save functionality requires updating the CSVHandler to handle existing files.\n" +
            //                  "For now, changes are in memory and will be lost on exit unless the profile is re-created.");
            // To implement this fully, you would call a method like:
            csvHandler.updateUserProfileToCsv(currentUser);
        } catch (Exception ex) {
            view.showError("Error saving profile: " + ex.getMessage());
        }
    }


    /**
     * Handles switching from the dashboard back to the user selection screen.
     */
    private void handleSwitchUser(ActionEvent e) {
        currentUser = null;
        view.showUserSelection();
    }

    /**
     * Populates the dashboard view with the current user's data.
     */
    private void updateDashboard() {
        if (currentUser == null) return;

        view.getDashboardView().setProfileInfo(
                currentUser.getName(),
                String.valueOf(currentUser.getAge()),
                String.format("%.1f kg (%.1f lbs)", currentUser.getWeightKg(), currentUser.getWeightLbs()),
                String.format("%.0f cm (%.1f in)", (double)currentUser.getHeightCm(), currentUser.getHeightInches())
        );

        DailyLog todayLog = getTodaysLog();
        updateFoodLogTable(todayLog);
        updateCalorieSummary();
    }

    /**
     * Updates the food log table on the dashboard with entries from the given log.
     */
    private void updateFoodLogTable(DailyLog log) {
        DefaultTableModel model = (DefaultTableModel) view.getDashboardView().getFoodLogTableModel();
        model.setRowCount(0); // Clear existing rows

        for (Loggable entry : log.getEntries()) {
            model.addRow(new Object[]{entry.getName(), String.format("%.1f", entry.getCalories())});
        }
    }

    /**
     * Recalculates and updates the calorie summary (TDEE, Consumed, Remaining).
     */
    private void updateCalorieSummary() {
        double bmr = calorieCalculator.calculateBmr(currentUser);
        double tdee = calorieCalculator.calculateTdee(currentUser);
        double consumed = getTodaysLog().getTotalCalories();
        double remaining = tdee - consumed;

        view.getDashboardView().setCalorieSummary(
                String.format("%.0f", tdee),
                String.format("%.0f", consumed),
                String.format("%.0f", remaining)
        );
    }
    
    /**
     * Retrieves the DailyLog for the current date. If one doesn't exist, it's created and added to the user profile.
     * @return The DailyLog for today.
     */
    private DailyLog getTodaysLog() {
        LocalDate today = LocalDate.now();
        Optional<DailyLog> logOpt = currentUser.getLogs().stream()
                .filter(log -> log.getDate().equals(today))
                .findFirst();

        if (logOpt.isPresent()) {
            return logOpt.get();
        } else {
            DailyLog newLog = new DailyLog(today);
            currentUser.addLog(newLog);
            return newLog;
        }
    }
}
