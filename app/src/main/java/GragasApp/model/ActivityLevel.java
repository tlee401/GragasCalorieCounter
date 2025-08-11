package GragasApp.model;

public enum ActivityLevel {
  SEDENTARY(1.20),
  LIGHT(1.375),
  MODERATE(1.55),
  ACTIVE(1.725),
  VERY_ACTIVE(1.90);

  private final double multiplier;
  ActivityLevel(double m) {
    this.multiplier = m;
  }
  public double getMultiplier() {
    return multiplier;
  }
}
