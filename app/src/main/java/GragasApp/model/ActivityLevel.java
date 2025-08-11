package GragasApp.model;

/**
 * Represents common physical activity levels used to scale BMR to TDEE
 * each enum stores a multiplier to BMR
 */
public enum ActivityLevel {
  SEDENTARY(1.20),
  LIGHT(1.375),
  MODERATE(1.55),
  ACTIVE(1.725),
  VERY_ACTIVE(1.90);

  private final double multiplier;
  /**
   * Creates an activity level with the given TDEE multiplier.
   *
   * @param m the factor by which to scale BMR to estimate TDEE
   */
  ActivityLevel(double m) {
    this.multiplier = m;
  }
  /**
   * Returns the activity multiplier for this level.
   *
   * @return the TDEE multiplier
   */
  public double getMultiplier() {
    return multiplier;
  }
}
