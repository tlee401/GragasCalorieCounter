import GragasApp.controller.AppController;
import GragasApp.model.CSVHandler;
import GragasApp.view.MainView;

import javax.swing.SwingUtilities;

/**
 * Main entry point for the Gragas Calorie Counting App.
 * Initializes the MVC components and starts the application.
 */
public class MainApp {
    public static void main(String[] args) {
        // The Swing application should be run on the Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            // Initialize the main components
            MainView mainView = new MainView();
            CSVHandler csvHandler = new CSVHandler();

            // The controller wires everything together
            new AppController(mainView, csvHandler);

            // Make the main window visible
            mainView.setVisible(true);
        });
    }
}
