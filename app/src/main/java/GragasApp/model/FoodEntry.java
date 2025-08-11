package GragasApp.model;

import java.util.Objects;

/**
 * Represents a single food entry that can be logged in a {DailyLog}.
 *
 * A {FoodEntry} has a display {name} and a {calories} value (kcal).
 * It can be created either with an explicit calorie value or by looking up an
 * estimated calorie value via {APICaller#APICall(String, String)} using the
 * {"nutrition"} endpoint.
 *
 * Construction modes:
 *   Direct: {#FoodEntry(String, double)} — uses the provided calorie value.
 *       This constructor normalizes the name by trimming and lowercasing.
 *   Lookup: {#FoodEntry(String)} — queries a remote API for an
 *       estimated calorie value based on the text name.
 *
 */

public class FoodEntry extends APICaller implements Loggable {
  private final String name;
  private double calories;

  /**
   * Creates a food entry with an explicit calorie value.
   * @param name     the food name
   * @param calories the calories in kilocalories (kcal);
   * @throws IllegalArgumentException if {name} is blank or {calories} is negative
   */
  public FoodEntry(String name, double calories){
    if (name == null || name.isBlank()) {
      throw new IllegalArgumentException("Food name cannot be blank");
    }
    if (calories < 0) {
      throw new IllegalArgumentException("Calories cannot be negative");
    }
    this.name = name.trim().toLowerCase();
    this.calories = calories;
  }

  /**
   * Creates a food entry by querying a remote nutrition API to estimate calories.
   *
   * This constructor calls {#APICall(String, String)} with the {"nutrition"}
   * endpoint and the provided {name}. The resulting value is stored in {calories}.
   *
   * @param name the food name used for the API query;
   * @throws IllegalArgumentException if {name} is blank
   * @throws Exception if the API request fails or returns an unexpected response
   */
  public FoodEntry(String name) throws Exception{
    if (name == null || name.isBlank()) {
      throw new IllegalArgumentException("Food name cannot be blank");
    }
    this.name = name;
    this.calories = APICall("nutrition", name);
  }

  /**
   * Returns the food name associated with this entry.
   *
   * @return the food name
   */
  @Override
  public String getName() {
    return name;
  }

  /**
   * Returns the calorie value in kilocalories (kcal).
   *
   * @return calories for this entry
   */
  @Override
  public double getCalories() {
    return calories;
  }

  /**
   * Returns a string representation useful for debugging.
   *
   * @return a string containing the name and calorie value
   */
    @Override
    public String toString() {
        return "FoodEntry{" +
               "Name='" + name + '\'' +
               ", Calorie=" + calories +
               '}';
    }

  /**
   * Compares this entry to another for equality.
   *
   * <p>Two {@code FoodEntry} instances are equal if both their {@code name} and
   * {@code calories} are equal.
   *
   * @param o the object to compare with
   * @return {@code true} if equal; {@code false} otherwise
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof FoodEntry)) return false;
    FoodEntry that = (FoodEntry) o;
    return calories == that.calories && name.equals(that.name);
  }

  /**
   * Returns a hash code consistent with {@link #equals(Object)}.
   *
   * @return the hash code
   */
  @Override
  // prevent equality bugs if switching from list to a hash based collection
  public int hashCode() {
    return Objects.hash(name, calories);
  }

}
