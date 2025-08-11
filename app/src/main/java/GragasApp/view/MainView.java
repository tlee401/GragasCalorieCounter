package GragasApp.view;

import javax.swing.*;
import java.awt.*;

/**
 * The main window (JFrame) of the application.
 * It uses a CardLayout to switch between the UserSelectionView and the DashboardView.
 */
public class MainView extends JFrame {

    private final CardLayout cardLayout;
    private final JPanel mainPanel;
    private final UserSelectionView userSelectionView;
    private final DashboardView dashboardView;

    // Panel names for CardLayout
    private static final String USER_SELECTION_PANEL = "UserSelectionPanel";
    private static final String DASHBOARD_PANEL = "DashboardPanel";

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

        // Add panels to the main panel with names
        mainPanel.add(userSelectionView, USER_SELECTION_PANEL);
        mainPanel.add(dashboardView, DASHBOARD_PANEL);

        // Add the main panel to the frame
        add(mainPanel);
    }

    // Getters for the sub-views so the controller can access them
    public UserSelectionView getUserSelectionView() {
        return userSelectionView;
    }

    public DashboardView getDashboardView() {
        return dashboardView;
    }

    // Methods to switch between panels
    public void showUserSelection() {
        cardLayout.show(mainPanel, USER_SELECTION_PANEL);
    }

    public void showDashboard() {
        cardLayout.show(mainPanel, DASHBOARD_PANEL);
    }

    // Convenience methods for showing dialogs
    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Information", JOptionPane.INFORMATION_MESSAGE);
    }
}
