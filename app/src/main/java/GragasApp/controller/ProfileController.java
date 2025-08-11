package GragasApp.controller;
import GragasApp.model.*;
import GragasApp.view.*;

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
}
