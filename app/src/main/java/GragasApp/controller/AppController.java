package GragasApp.controller;

import GragasApp.model.*;
import GragasApp.view.MainView;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * The main controller for the application, adhering to the MVC pattern.
 * This class acts as the intermediary between the View (GUI) and the Model (data and business logic).
 * It handles user interactions from the view, processes them, and updates the view in response.
 */
public class AppController {

    private final MainView view;
    private final CSVHandler csvHandler;
    private final CalorieCalculator calorieCalculator;

    private UserProfile currentUser;

    /**
     * Constructs the AppController.
     * It initializes the application by linking the main view with the data handler,
     * loading initial data, and attaching event listeners to the UI components.
     * @param view The main JFrame of the application.
     * @param csvHandler The handler responsible for reading/writing user data to CSV files.
     */
    public AppController(MainView view, CSVHandler csvHandler) {
        this.view = view;
        this.csvHandler = csvHandler;
        this.calorieCalculator = new MifflinStJeorCalculator();

        loadInitialData();
        attachListeners();
    }
    
    /**
     * Loads existing user profiles from CSV files at startup and populates the
     * user selection dropdown in the view.
     */
    private void loadInitialData() {
        try {
            csvHandler.loadUserProfilesFromCsvs();
            List<UserProfile> profiles = csvHandler.getUserProfiles();
            view.getDashboardView().setVisible(false);
            view.getUserSelectionView().populateUserList(profiles);
        } catch (IOException e) {
            view.showError("Failed to load user profiles from CSV files: " + e.getMessage());
        }
    }

    /**
     * Attaches all necessary event listeners from this controller to the UI components
     * in the various views (UserSelectionView, DashboardView, AllLogsView).
     */
    private void attachListeners() {
        // User Selection View Listeners
        view.getUserSelectionView().addLoadProfileListener(this::handleLoadProfile);
        view.getUserSelectionView().addCreateProfileListener(this::handleCreateProfile);

        // Dashboard View Listeners
        view.getDashboardView().addAddFoodListener(this::handleAddFood);
        view.getDashboardView().addSaveChangesListener(this::handleSaveChanges);
        view.getDashboardView().addSwitchUserListener(this::handleSwitchUser);
        view.getDashboardView().addEditWeightListener(this::handleEditWeight); 
        view.getDashboardView().addEditTargetWeightListener(this::handleEditTargetWeight); 
        view.getDashboardView().addViewAllLogsListener(this::handleViewAllLogs); 

        // All Logs View Listeners
        view.getAllLogsView().addBackToDashboardListener(_ -> view.showDashboard());
        view.getAllLogsView().addDateSelectionListener(this::handleDateSelectionChange);
    }

    /**
     * Handles the "Load Profile" button event. It retrieves the selected user from the view,
     * finds the corresponding UserProfile object, and updates the dashboard.
     * @param e The ActionEvent triggered by the button click.
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
     * Handles the "Create Profile" button event. It reads data from the input fields,
     * creates a new UserProfile object (handling metric/imperial conversion),
     * saves it to a CSV file, and navigates to the dashboard for the new user.
     * @param e The ActionEvent triggered by the button click.
     */
    private void handleCreateProfile(ActionEvent e) {
        try {
            String name = view.getUserSelectionView().getNewUserName();
            int age = view.getUserSelectionView().getAge();
            double height = view.getUserSelectionView().getUserHeight();
            double weight = view.getUserSelectionView().getWeight();
            double targetWeight = view.getUserSelectionView().getTargetWeight();
            Sex sex = view.getUserSelectionView().getSex();
            ActivityLevel activityLevel = view.getUserSelectionView().getActivityLevel();
            boolean useImperial = view.getUserSelectionView().isImperial();
            if (name.trim().isEmpty()) {
                view.showError("User name cannot be empty.");
                return;
            }
            if (useImperial) {
                currentUser = UserProfile.fromImperial(name, age, (int) height, activityLevel, sex, weight, targetWeight);
            } else {
                currentUser = new UserProfile(name, age, (int) height, activityLevel, sex, weight, targetWeight);
            }
            csvHandler.saveUserProfileToCsv(currentUser);
            updateDashboard();
            view.getUserSelectionView().addUserToList(currentUser);
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
     * Handles the "Add Food" button event. It takes the food description from the input field,
     * retrieves its calorie information, creates a new FoodEntry, adds it to today's log,
     * and updates the view to reflect the change.
     * @param e The ActionEvent triggered by the button click.
     */
    private void handleAddFood(ActionEvent e) {
        String foodDescription = view.getDashboardView().getFoodInput();
        if (foodDescription.trim().isEmpty()) {
            view.showError("Please enter a food name.");
            return;
        }
        try {
            FoodEntry newFood = new FoodEntry(foodDescription);
            DailyLog todayLog = getTodaysLog();
            todayLog.addEntry(newFood);
            updateFoodLogTable(todayLog);
            updateCalorieSummary();
        } catch (Exception ex) {
            view.showError("Could not add food: " + ex.getMessage());
        }
    }
    
    /**
     * Handles the "Save Changes" action, persisting the current user's state to a CSV file.
     * @param e The ActionEvent triggered by the button click.
     */
    private void handleSaveChanges(ActionEvent e) {
        if (currentUser == null) return;
        try {
            csvHandler.updateUserProfileToCsv(currentUser);
            view.showMessage("Profile saved successfully!");
        } catch (IOException ex) {
            view.showError("Error saving profile: " + ex.getMessage());
        }
    }
    
    /**
     * Handles the "Switch User" button event, returning the user to the initial
     * profile selection screen.
     * @param e The ActionEvent triggered by the button click.
     */
    private void handleSwitchUser(ActionEvent e) {
        currentUser = null;
        view.showUserSelection();
    }

    /**
     * Handles navigating to the AllLogsView. This populates the view with all historical
     * log dates for the current user and switches the main card layout to show it.
     * @param e The ActionEvent triggered by the button click.
     */
    private void handleViewAllLogs(ActionEvent e) {
        if (currentUser == null) return;

        // Populate the date list in the AllLogsView
        view.getAllLogsView().populateLogDates(currentUser.getLogs());

        // Clear any previously displayed log entries
        view.getAllLogsView().updateLogEntries(null);
        
        // Switch to the AllLogsView
        view.showAllLogs();
    }

    /**
     * Handles a change in the selected date in the AllLogsView.
     * It finds the log corresponding to the newly selected date and updates the
     * food entry table with its contents.
     * @param e The ListSelectionEvent triggered by the list selection.
     */
    private void handleDateSelectionChange(ListSelectionEvent e) {
        // Ensure the event is processed only once
        if (!e.getValueIsAdjusting()) {
            LocalDate selectedDate = view.getAllLogsView().getSelectedDate();
            if (selectedDate == null) return;

            // Find the log for the selected date and update the table
            currentUser.getLogs().stream()
                    .filter(log -> log.getDate().equals(selectedDate))
                    .findFirst()
                    .ifPresent(log -> view.getAllLogsView().updateLogEntries(log));
        }
    }


    /**
     * Handles editing the current user's weight. It prompts the user for a new
     * weight value and updates the UserProfile model and the dashboard view.
     * @param e The ActionEvent triggered by the button click.
     */
    private void handleEditWeight(ActionEvent e) {
        if (currentUser == null) return;
        String currentWeightStr = String.format("%.1f", currentUser.getWeightKg());
        String newWeightStr = JOptionPane.showInputDialog(view, "Enter new weight (kg):", currentWeightStr);
        
        if (newWeightStr != null && !newWeightStr.trim().isEmpty()) {
            try {
                double newWeight = Double.parseDouble(newWeightStr);
                if (newWeight <= 0) {
                    view.showError("Weight must be a positive number.");
                    return;
                }
                currentUser.setWeightKg(newWeight);
                updateDashboard(); // Refresh the view with new data and TDEE
            } catch (NumberFormatException nfe) {
                view.showError("Invalid input. Please enter a valid number for weight.");
            }
        }
    }
    
    /**
     * Handles editing the current user's target weight. It prompts the user for a new
     * target weight and updates the UserProfile model and the dashboard view.
     * @param e The ActionEvent triggered by the button click.
     */
    private void handleEditTargetWeight(ActionEvent e) {
        if (currentUser == null) return;
        String currentTargetStr = String.format("%.1f", currentUser.getTargetWeightKg());
        String newTargetStr = JOptionPane.showInputDialog(view, "Enter new target weight (kg):", currentTargetStr);

        if (newTargetStr != null && !newTargetStr.trim().isEmpty()) {
            try {
                double newTargetWeight = Double.parseDouble(newTargetStr);
                if (newTargetWeight <= 0) {
                    view.showError("Target weight must be a positive number.");
                    return;
                }
                currentUser.setTargetWeightKg(newTargetWeight);
                updateDashboard(); // Refresh the view
            } catch (NumberFormatException nfe) {
                view.showError("Invalid input. Please enter a valid number for target weight.");
            }
        }
    }

    /**
     * Populates the dashboard view with the current user's data. This method is called
     * whenever the dashboard needs to be refreshed with the latest model data.
     */
    private void updateDashboard() {
        if (currentUser == null) return;

        view.getDashboardView().setProfileInfo(
                currentUser.getName(),
                String.valueOf(currentUser.getAge()),
                String.format("%.1f kg (%.1f lbs)", currentUser.getWeightKg(), currentUser.getWeightLbs()),
                String.format("%.0f cm (%.1f in)", (double)currentUser.getHeightCm(), currentUser.getHeightInches()),
                String.format("%.1f kg (%.1f lbs)", currentUser.getTargetWeightKg(), currentUser.getTargetWeightLbs()) // ADDED
        );

        DailyLog todayLog = getTodaysLog();
        updateFoodLogTable(todayLog);
        updateCalorieSummary();
    }

    /**
     * Refreshes the food log table in the dashboard view with entries from a given log.
     * @param log The DailyLog whose entries should be displayed.
     */
    private void updateFoodLogTable(DailyLog log) {
        DefaultTableModel model = (DefaultTableModel) view.getDashboardView().getFoodLogTableModel();
        model.setRowCount(0);
        for (Loggable entry : log.getEntries()) {
            model.addRow(new Object[]{entry.getName(), String.format("%.1f", entry.getCalories())});
        }
    }

    /**
     * Recalculates the user's calorie summary (TDEE, consumed, remaining) and updates
     * the dashboard view.
     */
    private void updateCalorieSummary() {
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
     * Retrieves the DailyLog for the current date. If one does not exist, it creates a new
     * log, adds it to the current user's profile, and returns it.
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
