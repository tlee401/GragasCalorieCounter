package GragasApp.view;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * The main dashboard view. Displays user info, calorie summary, and the daily food log.
 * (MODIFIED to include target weight and edit buttons)
 */
public class DashboardView extends JPanel {

    // Profile Info
    private final JLabel nameLabel = new JLabel();
    private final JLabel ageLabel = new JLabel();
    private final JLabel weightLabel = new JLabel();
    private final JLabel heightLabel = new JLabel();
    private final JLabel targetWeightLabel = new JLabel(); // ADDED

    // Edit Buttons for Profile
    private final JButton editWeightButton; // ADDED
    private final JButton editTargetWeightButton; // ADDED

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
    private final JButton viewAllLogsButton; // ADDED

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
        gbc.anchor = GridBagConstraints.WEST; // Reset anchor

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
    
    public void setProfileInfo(String name, String age, String weight, String height, String targetWeight) {
        nameLabel.setText(name);
        ageLabel.setText(age);
        weightLabel.setText(weight);
        heightLabel.setText(height);
        targetWeightLabel.setText(targetWeight); // ADDED
    }

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
    
    public String getFoodInput() {
        String input = foodInput.getText();
        foodInput.setText("");
        return input;
    }

    public DefaultTableModel getFoodLogTableModel() {
        return foodLogTableModel;
    }
    
    public void addAddFoodListener(ActionListener listener) {
        addFoodButton.addActionListener(listener);
    }
    
    // ADDED listeners for new buttons
    public void addEditWeightListener(ActionListener listener) {
        editWeightButton.addActionListener(listener);
    }

    public void addEditTargetWeightListener(ActionListener listener) {
        editTargetWeightButton.addActionListener(listener);
    }

    public void addSaveChangesListener(ActionListener listener) {
        saveChangesButton.addActionListener(listener);
    }

    // ADDED listener for the new button
    public void addViewAllLogsListener(ActionListener listener) {
        viewAllLogsButton.addActionListener(listener);
    }
    
    public void addSwitchUserListener(ActionListener listener) {
        switchUserButton.addActionListener(listener);
    }
}
