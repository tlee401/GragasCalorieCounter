import static org.junit.Assert.*;

import java.util.*;
import GragasApp.model.*;
import org.junit.Test;


public class TestFoodEntryEquality {


  @Test
  public void listContainsAndRemoveByValue() {
    List<FoodEntry> log = new ArrayList<>();
    FoodEntry a = new FoodEntry("Oatmeal", 250);
    log.add(a);

    // contains by value
    assertTrue(log.contains(new FoodEntry("Oatmeal", 250)));

    // remove by value (different instance) also check that is it not case sensitive
    assertTrue(log.remove(new FoodEntry("oatmeal", 250)));
    assertTrue(log.isEmpty());
  }

  @Test
  public void notEqualWhenNameOrCaloriesDiffer() {
    FoodEntry base = new FoodEntry("Oatmeal", 250);
    assertNotEquals(new FoodEntry("Oatmeal", 200), base);
    assertNotEquals(new FoodEntry("Oat meal", 250), base);
  }

}
