package GragasApp.model;

public interface CalorieCalculator {

  double calculateBmr(UserProfile user);
  double calculateTdee(UserProfile user);

}
