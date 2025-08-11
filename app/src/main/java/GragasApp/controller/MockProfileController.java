package GragasApp.controller;
import GragasApp.model.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Mock ProfileController for testing the GUI without full functionality.
 * This class provides a simple list of profile names and a placeholder
 * for a selected profile.
 */
public class MockProfileController{

    private final List<UserProfile> mockProfiles = new ArrayList<>();
    private UserProfile currentProfile;

    public MockProfileController() {
        // Create a mock profile to be "loaded"
        mockProfiles.add(UserProfile.fromImperial("John Doe", 25, 70, ActivityLevel.ACTIVE, Sex.MALE, 180, 175));
    }

    public void loadProfile(String profileId) {
        return;
    }

    public List<UserProfile> listOfProfiles() {
        List<UserProfile> names = new ArrayList<>();
        for (UserProfile profile : mockProfiles) {
            names.add(profile);
        }
        return names;
    }

    public void selectProfileByName(String name) {
        currentProfile = mockProfiles.stream()
                .filter(p -> p.getName().equals(name))
                .findFirst()
                .orElse(null);
        System.out.println("Selected profile: " + (currentProfile != null ? currentProfile.getName() : "None"));
    }

    public void createProfile(String name, int age, int heightInches, double weightLbs, double targetLbs, Sex sex, ActivityLevel activityLevel) {
        // This is a mock implementation, so we'll just print the data
        System.out.println("Mock: Creating new profile for " + name);
        UserProfile newProfile = UserProfile.fromImperial(name, age, heightInches, activityLevel, sex, weightLbs, targetLbs);
        mockProfiles.add(newProfile);
        currentProfile = newProfile;
    }

    public void saveCurrentProfile() {
        // Mock implementation to do nothing for now
        System.out.println("Mock: Saving current profile.");
    }
}
