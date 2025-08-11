import GragasApp.view.*;
import org.junit.Test;
import static org.junit.Assert.fail;

/**
 * Contains "smoke tests" for the Swing View components using JUnit 4.
 * These tests ensure that each view can be instantiated without throwing exceptions,
 * which confirms that the component initialization and layout logic is syntactically correct.
 */
public class ViewSmokeTestsJunit4 {

    /**
     * Tests that the MainView frame can be instantiated successfully.
     */
    @Test
    public void testMainViewInstantiation() {
        try {
            new MainView();
        } catch (Exception e) {
            fail("MainView instantiation threw an exception: " + e.getMessage());
        }
    }

    /**
     * Tests that the DashboardView panel can be instantiated successfully.
     */
    @Test
    public void testDashboardViewInstantiation() {
        try {
            new DashboardView();
        } catch (Exception e) {
            fail("DashboardView instantiation threw an exception: " + e.getMessage());
        }
    }

    /**
     * Tests that the UserSelectionView panel can be instantiated successfully.
     */
    @Test
    public void testUserSelectionViewInstantiation() {
        try {
            new UserSelectionView();
        } catch (Exception e) {
            fail("UserSelectionView instantiation threw an exception: " + e.getMessage());
        }
    }

    /**
     * Tests that the AllLogsView panel can be instantiated successfully.
     */
    @Test
    public void testAllLogsViewInstantiation() {
        try {
            new AllLogsView();
        } catch (Exception e) {
            fail("AllLogsView instantiation threw an exception: " + e.getMessage());
        }
    }
}

