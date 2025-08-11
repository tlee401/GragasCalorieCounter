package GragasApp.model;

import java.util.Objects;

public class FoodEntry extends APICaller implements Loggable {
  private final String name;
  private double calories;

  public FoodEntry(String name, double calories){
    if (name == null || name.isBlank()) {
      throw new IllegalArgumentException("Food name cannot be blank");
    }
    if (calories < 0) {
      throw new IllegalArgumentException("Calories cannot be negative");
    }
    this.name = name;
    this.calories = calories;
  }

  // Stub class for entry with only name to make LLM CALL to retrieve calorie estimate
  public FoodEntry(String name) throws Exception{
    if (name == null || name.isBlank()) {
      throw new IllegalArgumentException("Food name cannot be blank");
    }
    this.name = name;
    this.calories = APICall("nutrition", name);
  }


  @Override
  public String getName() {
    return name;
  }

  @Override
  public double getCalories() {
    return calories;
  }

    @Override
    public String toString() {
        return "FoodEntry{" +
               "Name='" + name + '\'' +
               ", Calorie=" + calories +
               '}';
    }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof FoodEntry)) return false;
    FoodEntry that = (FoodEntry) o;
    return calories == that.calories && name.equals(that.name);
  }

  @Override
  // prevent equality bugs if switching from list to a hash based collection
  public int hashCode() {
    return Objects.hash(name, calories);
  }

}
