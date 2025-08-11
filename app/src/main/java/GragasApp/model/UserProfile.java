package GragasApp.model;

import java.util.ArrayList;
import java.util.List;
/**
 * Domain model representing a user's profile and daily logs.
 *
 * The canonical (stored) units are metric: height in centimeters and weight in kilograms.
 * Convenience getters/setters are provided for imperial units (inches, pounds); conversions
 * are handled by {Units}. This object is typically used as input to
 * calorie calculators such as {MifflinStJeorCalculator}. Also used by CSV handler for persistence
 *
 */
public class UserProfile {

  private String name;
  private int age;                 // years
  private int heightCm;            // canonical: centimeters
  private ActivityLevel activityLevel;
  private Sex sex;
  private double weightKg;         // canonical: kilograms
  private double targetWeightKg;   // canonical: kilograms
  private List<DailyLog> logs = new ArrayList<>();

  /**
   * Creates an empty {UserProfile}. All fields are left at their defaults and should be set
   * via setters before use.
   */
  public UserProfile() {
  }

  /**
   * Creates a {UserProfile} using metric units.
   *
   * @param name            user's display name
   * @param age             age in years
   * @param heightCm        height in centimeters
   * @param activityLevel   daily activity level
   * @param sex             biological sex (as required by BMR equations)
   * @param weightKg        current weight in kilograms
   * @param targetWeightKg  target (goal) weight in kilograms
   */
  public UserProfile(String name, int age, int heightCm,
      ActivityLevel activityLevel, Sex sex,
      double weightKg, double targetWeightKg) {
    this.name = name;
    this.age = age;
    this.heightCm = heightCm;
    this.activityLevel = activityLevel;
    this.sex = sex;
    this.weightKg = weightKg;
    this.targetWeightKg = targetWeightKg;
  }
  /**
   * Creates a {UserProfile} using  imperial units but converts to metric before storing.
   *
   */
  public static UserProfile fromImperial(String name, int age, int heightInches,
      ActivityLevel activityLevel, Sex sex,
      double weightLbs, double targetWeightLbs) {
    return new UserProfile(
        name,
        age,
        Units.inchesToCmInt(heightInches),
        activityLevel,
        sex,
        Units.lbsToKg(weightLbs),
        Units.lbsToKg(targetWeightLbs)
    );
  }

  // Getters and setters (metric canonical)
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getAge() {
    return age;
  }

  public void setAge(int age) {
    this.age = age;
  }

  public int getHeightCm() {
    return heightCm;
  }

  public void setHeightCm(int heightCm) {
    this.heightCm = heightCm;
  }

  public ActivityLevel getActivityLevel() {
    return activityLevel;
  }

  public void setActivityLevel(ActivityLevel activityLevel) {
    this.activityLevel = activityLevel;
  }

  public Sex getSex() {
    return sex;
  }

  public void setSex(Sex sex) {
    this.sex = sex;
  }

  public double getWeightKg() {
    return weightKg;
  }

  public void setWeightKg(double weightKg) {
    this.weightKg = weightKg;
  }

  public double getTargetWeightKg() {
    return targetWeightKg;
  }

  public void setTargetWeightKg(double targetWeightKg) {
    this.targetWeightKg = targetWeightKg;
  }

  public List<DailyLog> getLogs() {
    return logs;
  }

  public void setLogs(List<DailyLog> logs) {
    this.logs = logs;
  }

  public void addLog(DailyLog log) {
    this.logs.add(log);
  }

  // Convenience accessors for imperial units
  public double getWeightLbs() {
    return Units.kgToLbs(weightKg);
  }

  public void setWeightLbs(double weightLbs) {
    this.weightKg = Units.lbsToKg(weightLbs);
  }

  public double getTargetWeightLbs() {
    return Units.kgToLbs(targetWeightKg);
  }

  public void setTargetWeightLbs(double targetWeightLbs) {
    this.targetWeightKg = Units.lbsToKg(targetWeightLbs);
  }

  public double getHeightInches() {
    return Units.cmToInches(heightCm);
  }

  public void setHeightInches(double heightInches) {
    this.heightCm = Units.inchesToCmInt(heightInches);
  }
}