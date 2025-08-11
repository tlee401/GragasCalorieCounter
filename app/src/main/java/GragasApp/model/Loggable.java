package GragasApp.model;


/**
 * Interface for items that can be recorded in a {DailyLog} with a name
 * and a calorie value.
 *
 */
public interface Loggable {


  /**
   * Returns a name for this item (e.g., food name or activity label).
   *
   * @return the item name;
   */
  String getName();

  /**
   * Returns the calorie value for this item.
   *
   * @return the calorie amount
   */
  double getCalories();
}
