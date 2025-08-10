package GragasApp.model;

public class MifflinStJeorCalculator implements CalorieCalculator{

  @Override
  // Uses formula from MifflinStJeor to calculate BMR and TDEE
  public double calculateBmr(UserProfile user) {
    // Units: weight kg, height cm
    double base = 10.0 * user.getWeightKg() + 6.25 * user.getHeightCm() - 5.0 * user.getAge();
    // if male +5 if female -161
    double sexOffset = (user.getSex() == Sex.Male) ? 5.0 : -161.0;
    return base + sexOffset;
  }

  @Override
  public double calculateTdee(UserProfile user) {
    // uses activity multipliser for TDEE
    return calculateBmr(user) * user.getActivityLevel().getMultiplier();
  }
}
