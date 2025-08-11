package GragasApp.controller;
import GragasApp.model.*;
import java.util.List;

public interface ProfileController {
  void createProfile(String name,
      int age,
      int heightInches,
      double weightLbs,
      double targetWeightLbs,
      Sex sex,
      ActivityLevel activity);
  void loadProfile(String profileId); // wire however you like later
  void saveCurrentProfile();

  // New for login
  List<UserProfile> listOfProfiles();        // unique names
  void selectProfileByName(String name);  // set current user in the model
}
