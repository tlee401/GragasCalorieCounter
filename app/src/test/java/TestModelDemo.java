import static org.junit.Assert.fail;

import GragasApp.model.*;

public class TestModelDemo {

  public static void main(String[] args) {

    APICaller apiCaller = new APICaller();
    try {
            double calories = apiCaller.APICall("nutrition", "1lb brisket and fries");
            System.out.println("Calories returned: " + calories);

        } catch (Exception e) {
            fail("API call threw an exception: " + e.getMessage());
        }

  }
}
