package GragasApp.model;

/**
 * interface for estimating user Basal metabolic rate (BMR) and TDEE (Total daily Energy
 * expenditure.
 *
 */
public interface CalorieCalculator {

  /**
   * Calculates the Total Daily Energy Expenditure (TDEE) by adjusting BMR for
   * the user's activity level and any additional factors the implementation considers.
   * @param user the user profile (including activity level) used to scale BMR
   * @return estimated TDEE in kcal/day
   */
  double calculateBmr(UserProfile user);


  /**
   * Calculates the Basal Metabolic Rate (BMR) — the energy expended at rest.
   *
   * @param user the user profile providing the metrics (e.g., sex, age, height, weight)
   *             required by the chosen formula
   * @return estimated BMR in kcal/day
   */
  double calculateTdee(UserProfile user);

}
