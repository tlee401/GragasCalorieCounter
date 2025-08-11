package GragasApp.view;

import GragasApp.controller.UserProfileManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class HomePagePanel extends JPanel {
    private final JLabel welcomeLabel = new JLabel();
    private final JButton logoutButton = new JButton("Log Out");

    public HomePagePanel(UserProfileManager userProfileManager, ActionListener logoutListener) {
        setLayout(new BorderLayout(10, 10));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        welcomeLabel.setFont(new Font("Serif", Font.BOLD, 24));
        topPanel.add(welcomeLabel);

        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // The summary panel now lives on the home page.
        SummaryPanel summaryPanel = new SummaryPanel(userProfileManager);
        contentPanel.add(summaryPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.add(logoutButton);
        logoutButton.addActionListener(logoutListener);

        add(topPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    public void updateWelcomeMessage(String name) {
        welcomeLabel.setText("Welcome, " + name + "!");
    }
}
