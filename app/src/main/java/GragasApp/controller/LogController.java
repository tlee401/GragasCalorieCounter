package GragasApp.controller;

import GragasApp.model.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class LogController {
    private final UserProfileManager userProfileManager;
    private final CalorieLookupService calorieLookupService;

    public LogController(UserProfileManager userProfileManager, CalorieLookupService calorieLookupService) {
        this.userProfileManager = userProfileManager;
        this.calorieLookupService = calorieLookupService;
    }

    public void addFood(String foodDescription) throws Exception {
        UserProfile user = userProfileManager.getCurrentUserProfile();
        if (user == null) {
            throw new IllegalStateException("No user profile is currently selected.");
        }

        FoodEntry entry = new FoodEntry(foodDescription, calorieLookupService.estimateCalories(foodDescription));
        LocalDate today = LocalDate.now();

        Optional<DailyLog> logForDate = user.getLogs().stream()
                .filter(log -> log.getDate().equals(today))
                .findFirst();

        if (logForDate.isPresent()) {
            logForDate.get().addEntry(entry);
        } else {
            DailyLog newLog = new DailyLog(today);
            newLog.addEntry(entry);
            user.addLog(newLog);
        }

        userProfileManager.saveCurrentProfile();
    }

    public void removeFood(Loggable entry, LocalDate date) {
        UserProfile user = userProfileManager.getCurrentUserProfile();
        if (user == null) {
            throw new IllegalStateException("No user profile is currently selected.");
        }

        user.getLogs().stream()
                .filter(log -> log.getDate().equals(date))
                .findFirst()
                .ifPresent(log -> log.removeEntry(entry));

        userProfileManager.saveCurrentProfile();
    }

    public List<Loggable> getEntries(LocalDate date) {
        UserProfile user = userProfileManager.getCurrentUserProfile();
        if (user == null) {
            return List.of();
        }

        return user.getLogs().stream()
                .filter(log -> log.getDate().equals(date))
                .findFirst()
                .map(DailyLog::getEntries)
                .orElse(List.of());
    }

    public int getTotalCaloriesForToday() {
        UserProfile user = userProfileManager.getCurrentUserProfile();
        if (user == null) {
            return 0;
        }
        return user.getLogs().stream()
                .filter(log -> log.getDate().equals(LocalDate.now()))
                .findFirst()
                .map(DailyLog::getTotalCalories)
                .orElse(0);
    }
}
