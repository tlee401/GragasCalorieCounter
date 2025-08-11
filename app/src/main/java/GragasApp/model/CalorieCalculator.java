package GragasApp.model;

//interface can be used with other formulas for the calculations
public interface CalorieCalculator {

  double calculateBmr(UserProfile user);
  double calculateTdee(UserProfile user);

}
