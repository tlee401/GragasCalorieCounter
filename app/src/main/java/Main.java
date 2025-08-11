// Main.java
import GragasApp.controller.*;
import GragasApp.model.*;
import GragasApp.view.*;
import javax.swing.SwingUtilities;

/**
 * Main class to run the GragasApp.
 * This class serves as the application's entry point,
 * initializing all controllers, services, and the main GUI frame.
 */
public class Main {
    public static void main(String[] args) {
        // Run the GUI creation on the Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            // Instantiate all controllers and services
            MockProfileController profileController = new MockProfileController();
            MockCalculatorController calculatorController = new MockCalculatorController();
            MockLogController logController = new MockLogController();
            CalorieLookupService calorieLookupService = new CalorieLookupService();

            // Create the main frame and pass the dependencies
            MainFrame mainFrame = new MainFrame(
                profileController,
                calculatorController,
                logController,
                calorieLookupService
            );
            mainFrame.setVisible(true);
        });
    }
}
