package GragasApp.controller;

import GragasApp.model.ActivityLevel;
import GragasApp.model.CSVHandler;
import GragasApp.model.Sex;
import GragasApp.model.UserProfile;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
// import java.util.Optional;

public class UserProfileManager {
    private final CSVHandler csvHandler = new CSVHandler();
    private List<UserProfile> userProfiles = new ArrayList<>();
    private UserProfile currentUserProfile;

    public UserProfileManager() {
        try {
            csvHandler.loadUserProfilesFromCsvs();
            userProfiles = csvHandler.getUserProfiles();
        } catch (IOException e) {
            System.err.println("Error loading user profiles from CSVs: " + e.getMessage());
        }
    }

    public List<String> getProfileNames() {
        List<String> names = new ArrayList<>();
        for (UserProfile profile : userProfiles) {
            names.add(profile.getName());
        }
        return names;
    }

    public void selectProfileByName(String name) {
        this.currentUserProfile = userProfiles.stream()
                .filter(p -> p.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    public void createProfile(String name, int age, int heightInches, double weightLbs, double targetLbs, Sex sex, ActivityLevel activityLevel) {
        UserProfile newProfile = UserProfile.fromImperial(name, age, heightInches, activityLevel, sex, weightLbs, targetLbs);
        userProfiles.add(newProfile);
        currentUserProfile = newProfile;
        try {
            csvHandler.saveUserProfileToCsv(newProfile);
        } catch (IOException e) {
            System.err.println("Error saving new profile: " + e.getMessage());
        }
    }
    
    public void saveCurrentProfile() {
        if (currentUserProfile != null) {
            try {
                csvHandler.saveUserProfileToCsv(currentUserProfile);
            } catch (IOException e) {
                System.err.println("Error saving current profile: " + e.getMessage());
            }
        }
    }

    public UserProfile getCurrentUserProfile() {
        return currentUserProfile;
    }
    
    public void setCurrentUserProfile(UserProfile profile) {
        this.currentUserProfile = profile;
    }
    
    public void logout() {
        this.currentUserProfile = null;
    }
}
