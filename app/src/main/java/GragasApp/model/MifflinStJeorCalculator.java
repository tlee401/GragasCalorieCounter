package GragasApp.model;

public class MifflinStJeorCalculator implements CalorieCalculator{

  @Override
  public double calculateBmr(UserProfile user) {
    // Units: weight kg, height cm
    double base = 10.0 * user.getWeightKg() + 6.25 * user.getHeightCm() - 5.0 * user.getAge();
    double sexOffset = (user.getSex() == Sex.Male) ? 5.0 : -161.0;
    return base + sexOffset;
  }

  @Override
  public double calculateTdee(UserProfile user) {
    return calculateBmr(user) * user.getActivityLevel().getMultiplier();
  }
}
