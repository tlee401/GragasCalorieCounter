import GragasApp.model.*;

public class TestModelDemo {

  public static void main(String[] args) {
    CalorieCalculator calc = new MifflinStJeorCalculator();
    UserProfile u = new UserProfile(
        "Alex", 24, 178, ActivityLevel.MODERATE, Sex.MALE, 72.0,
        68.0
    );
    double bmr  = calc.calculateBmr(u);
    double tdee = calc.calculateTdee(u);
    System.out.println("BMR: " + bmr + " TDEE: " + tdee);

  }
}
