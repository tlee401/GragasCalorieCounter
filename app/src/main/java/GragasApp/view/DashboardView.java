package GragasApp.view;

import javax.swing.*;
// import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * The main dashboard view, which serves as the primary interface for an authenticated user.
 * This panel displays the user's profile information, a summary of their daily calorie intake,
 * and a table of food items logged for the current day. It also provides controls for
 * editing user data, adding food, and navigating to other views.
 */
public class DashboardView extends JPanel {

    // Profile Info
    private final JLabel nameLabel = new JLabel();
    private final JLabel ageLabel = new JLabel();
    private final JLabel weightLabel = new JLabel();
    private final JLabel heightLabel = new JLabel();
    private final JLabel targetWeightLabel = new JLabel(); 

    // Edit Buttons for Profile
    private final JButton editWeightButton; 
    private final JButton editTargetWeightButton;

    // Calorie Summary
    private final JLabel tdeeLabel = new JLabel();
    private final JLabel consumedLabel = new JLabel();
    private final JLabel remainingLabel = new JLabel();

    // Food Log
    private final JTextField foodInput;
    private final JButton addFoodButton;
    private final JTable foodLogTable;
    private final DefaultTableModel foodLogTableModel;

    // Actions
    private final JButton saveChangesButton;
    private final JButton switchUserButton;
    private final JButton viewAllLogsButton; 

    /**
     * Constructs the DashboardView panel and initializes all its UI components.
     * The layout is structured with user info and calorie summary at the top,
     * the daily food log in the center, and action buttons at the bottom.
     */
    public DashboardView() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // --- Top Panel (Profile Info & Calorie Summary) ---
        JPanel topPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        
        // Profile Info Panel (Updated to GridBagLayout)
        JPanel profilePanel = new JPanel(new GridBagLayout());
        profilePanel.setBorder(BorderFactory.createTitledBorder("User Profile"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 5, 2, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Initialize edit buttons
        editWeightButton = new JButton("Edit");
        editTargetWeightButton = new JButton("Edit");
        editWeightButton.setMargin(new Insets(2, 5, 2, 5));
        editTargetWeightButton.setMargin(new Insets(2, 5, 2, 5));

        // Row 0: Name
        gbc.gridx = 0; gbc.gridy = 0;
        profilePanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        profilePanel.add(nameLabel, gbc);

        // Row 1: Age
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
        profilePanel.add(new JLabel("Age:"), gbc);
        gbc.gridx = 1;
        profilePanel.add(ageLabel, gbc);

        // Row 2: Height
        gbc.gridx = 0; gbc.gridy = 2;
        profilePanel.add(new JLabel("Height:"), gbc);
        gbc.gridx = 1;
        profilePanel.add(heightLabel, gbc);

        // Row 3: Weight
        gbc.gridx = 0; gbc.gridy = 3;
        profilePanel.add(new JLabel("Weight:"), gbc);
        gbc.gridx = 1;
        profilePanel.add(weightLabel, gbc);
        gbc.gridx = 2; gbc.anchor = GridBagConstraints.EAST;
        profilePanel.add(editWeightButton, gbc);
        gbc.anchor = GridBagConstraints.WEST;

        // Row 4: Target Weight
        gbc.gridx = 0; gbc.gridy = 4;
        profilePanel.add(new JLabel("Target Weight:"), gbc);
        gbc.gridx = 1;
        profilePanel.add(targetWeightLabel, gbc);
        gbc.gridx = 2; gbc.anchor = GridBagConstraints.EAST;
        profilePanel.add(editTargetWeightButton, gbc);

        topPanel.add(profilePanel);

        // Calorie Summary Panel
        JPanel summaryPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        summaryPanel.setBorder(BorderFactory.createTitledBorder("Today's Calorie Summary"));
        summaryPanel.add(new JLabel("Goal (TDEE):"));
        summaryPanel.add(tdeeLabel);
        summaryPanel.add(new JLabel("Consumed:"));
        summaryPanel.add(consumedLabel);
        summaryPanel.add(new JLabel("Remaining:"));
        summaryPanel.add(remainingLabel);
        topPanel.add(summaryPanel);

        add(topPanel, BorderLayout.NORTH);

        // --- Center Panel (Food Log) ---
        JPanel logPanel = new JPanel(new BorderLayout(5, 5));
        logPanel.setBorder(BorderFactory.createTitledBorder("Today's Food Log"));

        String[] columnNames = {"Food", "Calories"};
        foodLogTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        foodLogTable = new JTable(foodLogTableModel);
        JScrollPane scrollPane = new JScrollPane(foodLogTable);
        logPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        foodInput = new JTextField(25);
        addFoodButton = new JButton("Add Food");
        inputPanel.add(new JLabel("Add Food:"));
        inputPanel.add(foodInput);
        inputPanel.add(addFoodButton);
        logPanel.add(inputPanel, BorderLayout.SOUTH);

        add(logPanel, BorderLayout.CENTER);
        
        // --- Bottom Panel (Action Buttons) ---
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        saveChangesButton = new JButton("Save Changes");
        switchUserButton = new JButton("Switch User");
        viewAllLogsButton = new JButton("View All Logs"); // ADDED
        actionPanel.add(viewAllLogsButton); // ADDED
        actionPanel.add(saveChangesButton);
        actionPanel.add(switchUserButton);
        add(actionPanel, BorderLayout.SOUTH);
    }
    
    /**
     * Updates the user profile information displayed on the dashboard.
     * @param name The user's name.
     * @param age The user's age.
     * @param weight The user's current weight, formatted as a string.
     * @param height The user's height, formatted as a string.
     * @param targetWeight The user's target weight, formatted as a string.
     */
    public void setProfileInfo(String name, String age, String weight, String height, String targetWeight) {
        nameLabel.setText(name);
        ageLabel.setText(age);
        weightLabel.setText(weight);
        heightLabel.setText(height);
        targetWeightLabel.setText(targetWeight); 
    }

    /**
     * Updates the calorie summary panel with calculated values.
     * @param tdee The user's Total Daily Energy Expenditure (Goal).
     * @param consumed The total calories consumed for the day.
     * @param remaining The remaining calories (TDEE - consumed).
     */
    public void setCalorieSummary(String tdee, String consumed, String remaining) {
        tdeeLabel.setText(tdee + " kcal");
        consumedLabel.setText(consumed + " kcal");
        
        double rem = Double.parseDouble(remaining);
        String remText = String.format("%.0f kcal", rem);
        remainingLabel.setText(remText);
        if (rem < 0) {
            remainingLabel.setForeground(Color.RED);
        } else {
            remainingLabel.setForeground(new Color(0, 153, 0)); // Darker green
        }
    }
    
    /**
     * Retrieves the text from the food input field and clears it.
     * @return The food description entered by the user.
     */
    public String getFoodInput() {
        String input = foodInput.getText();
        foodInput.setText("");
        return input;
    }

    /**
     * Gets the table model for the daily food log.
     * This allows the controller to directly manipulate the table's data.
     * @return The DefaultTableModel for the food log.
     */
    public DefaultTableModel getFoodLogTableModel() {
        return foodLogTableModel;
    }
    
    /**
     * Adds an ActionListener to the "Add Food" button.
     * @param listener The ActionListener to be notified of button clicks.
     */
    public void addAddFoodListener(ActionListener listener) {
        addFoodButton.addActionListener(listener);
    }
    
    /**
     * Adds an ActionListener to the "Edit Weight" button.
     * @param listener The ActionListener to be notified of button clicks.
     */
    public void addEditWeightListener(ActionListener listener) {
        editWeightButton.addActionListener(listener);
    }

    /**
     * Adds an ActionListener to the "Edit Target Weight" button.
     * @param listener The ActionListener to be notified of button clicks.
     */
    public void addEditTargetWeightListener(ActionListener listener) {
        editTargetWeightButton.addActionListener(listener);
    }

    /**
     * Adds an ActionListener to the "Save Changes" button.
     * @param listener The ActionListener to be notified of button clicks.
     */
    public void addSaveChangesListener(ActionListener listener) {
        saveChangesButton.addActionListener(listener);
    }

    /**
     * Adds an ActionListener to the "View All Logs" button.
     * @param listener The ActionListener to be notified of button clicks.
     */
    public void addViewAllLogsListener(ActionListener listener) {
        viewAllLogsButton.addActionListener(listener);
    }
    
    /**
     * Adds an ActionListener to the "Switch User" button.
     * @param listener The ActionListener to be notified of button clicks.
     */
    public void addSwitchUserListener(ActionListener listener) {
        switchUserButton.addActionListener(listener);
    }
}
