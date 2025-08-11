package GragasApp.controller;

import GragasApp.model.DailyLog;
import GragasApp.model.Loggable;
import GragasApp.model.UserProfile;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The HistoryController is responsible for managing and retrieving historical
 * log data for a specific user profile.
 * It provides methods to access DailyLogs, which contain the food entries for a given day.
 */
public class HistoryController {

    private final UserProfile userProfile;

    /**
     * Constructs a new HistoryController for the given UserProfile.
     * @param userProfile The UserProfile whose history is to be managed.
     */
    public HistoryController(UserProfile userProfile) {
        // if (userProfile == null) {
        //     throw new IllegalArgumentException("UserProfile cannot be null.");
        // }
        this.userProfile = userProfile;
    }

    /**
     * Retrieves the DailyLog for a specific date.
     * @param date The date for which to retrieve the log.
     * @return The DailyLog object for the specified date, or null if no log exists for that date.
     */
    public DailyLog getLogForDate(LocalDate date) {
        return userProfile.getLogs().stream()
                .filter(log -> log.getDate().isEqual(date))
                .findFirst()
                .orElse(null);
    }

    /**
     * Retrieves a list of all DailyLog objects for this user.
     * @return A List of DailyLog objects.
     */
    public List<DailyLog> getDailyLogs() {
        return userProfile.getLogs();
    }

    /**
     * Retrieves a list of all dates for which a log exists in the user's history.
     * @return A List of LocalDate objects representing all logged dates.
     */
    public List<LocalDate> getAllLogDates() {
        return userProfile.getLogs().stream()
                .map(DailyLog::getDate)
                .sorted() // Sort the dates for a chronological view
                .collect(Collectors.toList());
    }

    /**
     * Retrieves the list of Loggable entries for a specific date.
     * This is a convenience method that delegates to the DailyLog object.
     * @param date The date for which to get the food entries.
     * @return A list of Loggable entries for the date, or an empty list if no log exists.
     */
    public List<Loggable> getEntriesForDate(LocalDate date) {
        DailyLog log = getLogForDate(date);
        if (log != null) {
            return log.getEntries();
        }
        return List.of();
    }

    /**
     * Retrieves the total calories consumed on a specific date.
     * @param date The date for which to get the calorie total.
     * @return The total calories for the date, or 0 if no log exists.
     */
    public int getTotalCaloriesForDate(LocalDate date) {
        DailyLog log = getLogForDate(date);
        if (log != null) {
            return log.getTotalCalories();
        }
        return 0;
    }
}

