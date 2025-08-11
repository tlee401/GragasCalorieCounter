package GragasApp.controller;

import GragasApp.model.CalorieCalculator;
import GragasApp.model.MifflinStJeorCalculator;
import GragasApp.model.UserProfile;

public class CalculatorController {
    private final UserProfileManager userProfileManager;
    private final CalorieCalculator calculator;

    public CalculatorController(UserProfileManager userProfileManager) {
        this.userProfileManager = userProfileManager;
        this.calculator = new MifflinStJeorCalculator();
    }

    public CalcResult calculateBmrAndTdee() {
        UserProfile user = userProfileManager.getCurrentUserProfile();
        if (user != null) {
            double bmr = calculator.calculateBmr(user);
            double tdee = calculator.calculateTdee(user);
            return new CalcResult(bmr, tdee);
        }
        return null;
    }
}
