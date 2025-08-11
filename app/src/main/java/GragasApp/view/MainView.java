package GragasApp.view;

import javax.swing.*;
import java.awt.*;

/**
 * The main window (JFrame) of the application.
 * This class serves as the root container for all other UI panels (views).
 * It uses a CardLayout to manage and switch between different views like
 * the user selection screen, the main dashboard, and the all logs view.
 */
public class MainView extends JFrame {

    private final CardLayout cardLayout;
    private final JPanel mainPanel;
    private final UserSelectionView userSelectionView;
    private final DashboardView dashboardView;
    private final AllLogsView allLogsView; 


    // Panel names for CardLayout
    private static final String USER_SELECTION_PANEL = "UserSelectionPanel";
    private static final String DASHBOARD_PANEL = "DashboardPanel";
    private static final String ALL_LOGS_PANEL = "AllLogsPanel"; 

    /**
     * Constructs the MainView frame, initializes the CardLayout, and adds
     * all subordinate view panels to it.
     */
    public MainView() {
        setTitle("Gragas Calorie Counter");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center on screen

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Initialize panels
        userSelectionView = new UserSelectionView();
        dashboardView = new DashboardView();
        allLogsView = new AllLogsView(); 

        // Add panels to the main panel with names
        mainPanel.add(userSelectionView, USER_SELECTION_PANEL);
        mainPanel.add(dashboardView, DASHBOARD_PANEL);
        mainPanel.add(allLogsView, ALL_LOGS_PANEL); 

        // Add the main panel to the frame
        add(mainPanel);
    }

    /**
     * Returns the instance of the UserSelectionView.
     * Allows the controller to access and attach listeners to this view.
     * @return The UserSelectionView panel.
     */
    public UserSelectionView getUserSelectionView() {
        return userSelectionView;
    }

    /**
     * Returns the instance of the DashboardView.
     * Allows the controller to access and attach listeners to this view.
     * @return The DashboardView panel.
     */
    public DashboardView getDashboardView() {
        return dashboardView;
    }

    /**
     * Returns the instance of the AllLogsView.
     * Allows the controller to access and attach listeners to this view.
     * @return The AllLogsView panel.
     */
    public AllLogsView getAllLogsView() {
        return allLogsView;
    }

    /**
     * Switches the view to the user selection panel.
     */
    public void showUserSelection() {
        cardLayout.show(mainPanel, USER_SELECTION_PANEL);
    }

    /**
     * Switches the view to the main dashboard panel.
     */
    public void showDashboard() {
        cardLayout.show(mainPanel, DASHBOARD_PANEL);
    }

    /**
     * Switches the view to the all logs panel.
     */
    public void showAllLogs() {
        cardLayout.show(mainPanel, ALL_LOGS_PANEL);
    }

    /**
     * Displays a standardized error message dialog.
     * @param message The error message to display.
     */
    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Displays a standardized informational message dialog.
     * @param message The information message to display.
     */
    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Information", JOptionPane.INFORMATION_MESSAGE);
    }
}
