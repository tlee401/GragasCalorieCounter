package GragasApp.model;

/**
 * {@link CalorieCalculator} implementation using the Mifflin–St Jeor equation.
 *
 */
public class MifflinStJeorCalculator implements CalorieCalculator{

  /**
   * Computes BMR using the user’s target weight:
   *
   * @param user profile providing age, height, sex, and target weight
   * @return estimated BMR (kcal/day) at target weight
   */
  @Override
  public double calculateBmr(UserProfile user) {
    // Units: weight kg, height cm
    double base = 10.0 * user.getTargetWeightKg() + 6.25 * user.getHeightCm() - 5.0 * user.getAge();
    // if male +5 if female -161
    double sexOffset = (user.getSex() == Sex.MALE) ? 5.0 : -161.0;
    return base + sexOffset;
  }

  /**
   * Computes BMR using the user’s current weight:
   *
   * @param user profile providing age, height, sex, and target weight
   * @return estimated BMR (kcal/day) at target weight
   */
  public double calculateCurrentBmr(UserProfile user) {
    // Units: weight kg, height cm
    double base = 10.0 * user.getWeightKg() + 6.25 * user.getHeightCm() - 5.0 * user.getAge();
    // if male +5 if female -161
    double sexOffset = (user.getSex() == Sex.MALE) ? 5.0 : -161.0;
    return base + sexOffset;
  }

  /**
   * calculates TDEE by applying multiplier to BMR
   *
   * @param user the user profile providing the metrics (e.g., sex, age, height, weight)
   *             required by the chosen formula
   *
   * @return estimated TDEE (kcal/day)
   */
  @Override
  public double calculateTdee(UserProfile user) {
    // uses activity multipliser for TDEE
    return calculateBmr(user) * user.getActivityLevel().getMultiplier();
  }
}
