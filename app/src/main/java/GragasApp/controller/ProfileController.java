package GragasApp.controller;

import GragasApp.model.ActivityLevel;
import GragasApp.model.Sex;
import GragasApp.model.UserProfile;

public class ProfileController {
    private final UserProfileManager userProfileManager;

    public ProfileController(UserProfileManager userProfileManager) {
        this.userProfileManager = userProfileManager;
    }

    public void createProfile(String name, int age, int heightInches, double weightLbs, double targetLbs, Sex sex, ActivityLevel activityLevel) {
        userProfileManager.createProfile(name, age, heightInches, weightLbs, targetLbs, sex, activityLevel);
    }

    public void saveCurrentProfile() {
        userProfileManager.saveCurrentProfile();
    }

    public UserProfile getCurrentUserProfile() {
        return userProfileManager.getCurrentUserProfile();
    }
    
    public void setCurrentProfile(UserProfile profile) {
        userProfileManager.setCurrentUserProfile(profile);
    }
}
