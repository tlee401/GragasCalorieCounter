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
 * The main controller for the application.
 * (MODIFIED to handle weight/target weight editing and saving)
 */
public class AppController {

    private final MainView view;
    private final CSVHandler csvHandler;
    private final CalorieLookupService calorieLookupService;
    private final CalorieCalculator calorieCalculator;

    private UserProfile currentUser;

    public AppController(MainView view, CSVHandler csvHandler) {
        this.view = view;
        this.csvHandler = csvHandler;
        this.calorieLookupService = new CalorieLookupService();
        this.calorieCalculator = new MifflinStJeorCalculator();

        loadInitialData();
        attachListeners();
    }
    
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

    private void attachListeners() {
        // User Selection View Listeners
        view.getUserSelectionView().addLoadProfileListener(this::handleLoadProfile);
        view.getUserSelectionView().addCreateProfileListener(this::handleCreateProfile);

        // Dashboard View Listeners
        view.getDashboardView().addAddFoodListener(this::handleAddFood);
        view.getDashboardView().addSaveChangesListener(this::handleSaveChanges);
        view.getDashboardView().addSwitchUserListener(this::handleSwitchUser);
        view.getDashboardView().addEditWeightListener(this::handleEditWeight); // ADDED
        view.getDashboardView().addEditTargetWeightListener(this::handleEditTargetWeight); // ADDED
    }

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

    private void handleCreateProfile(ActionEvent e) {
        try {
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

    private void handleAddFood(ActionEvent e) {
        String foodDescription = view.getDashboardView().getFoodInput();
        if (foodDescription.trim().isEmpty()) {
            view.showError("Please enter a food name.");
            return;
        }
        try {
            double calories = calorieLookupService.estimateCalories(foodDescription);
            FoodEntry newFood = new FoodEntry(foodDescription, calories);
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
     * (MODIFIED to be fully functional)
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
    
    private void handleSwitchUser(ActionEvent e) {
        currentUser = null;
        view.showUserSelection();
    }

    /**
     * Handles editing the current user's weight.
     * (NEW METHOD)
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
     * Handles editing the current user's target weight.
     * (NEW METHOD)
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
     * Populates the dashboard view with the current user's data.
     * (MODIFIED to pass target weight)
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

    private void updateFoodLogTable(DailyLog log) {
        DefaultTableModel model = (DefaultTableModel) view.getDashboardView().getFoodLogTableModel();
        model.setRowCount(0);
        for (Loggable entry : log.getEntries()) {
            model.addRow(new Object[]{entry.getName(), String.format("%.1f", entry.getCalories())});
        }
    }

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
