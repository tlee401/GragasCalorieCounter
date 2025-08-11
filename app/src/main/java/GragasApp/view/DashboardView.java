package GragasApp.view;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * The main dashboard view. Displays user info, calorie summary, and the daily food log.
 */
public class DashboardView extends JPanel {

    // Profile Info
    private final JLabel nameLabel = new JLabel();
    private final JLabel ageLabel = new JLabel();
    private final JLabel weightLabel = new JLabel();
    private final JLabel heightLabel = new JLabel();

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

    public DashboardView() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // --- Top Panel (Profile Info & Calorie Summary) ---
        JPanel topPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        
        // Profile Info Panel
        JPanel profilePanel = new JPanel(new GridLayout(4, 2, 5, 5));
        profilePanel.setBorder(BorderFactory.createTitledBorder("User Profile"));
        profilePanel.add(new JLabel("Name:"));
        profilePanel.add(nameLabel);
        profilePanel.add(new JLabel("Age:"));
        profilePanel.add(ageLabel);
        profilePanel.add(new JLabel("Weight:"));
        profilePanel.add(weightLabel);
        profilePanel.add(new JLabel("Height:"));
        profilePanel.add(heightLabel);
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

        // Table
        String[] columnNames = {"Food", "Calories"};
        foodLogTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table cells not editable
            }
        };
        foodLogTable = new JTable(foodLogTableModel);
        JScrollPane scrollPane = new JScrollPane(foodLogTable);
        logPanel.add(scrollPane, BorderLayout.CENTER);

        // Input area
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
        actionPanel.add(saveChangesButton);
        actionPanel.add(switchUserButton);
        add(actionPanel, BorderLayout.SOUTH);
    }
    
    // --- Public methods for Controller interaction ---

    public void setProfileInfo(String name, String age, String weight, String height) {
        nameLabel.setText(name);
        ageLabel.setText(age);
        weightLabel.setText(weight);
        heightLabel.setText(height);
    }

    public void setCalorieSummary(String tdee, String consumed, String remaining) {
        tdeeLabel.setText(tdee + " kcal");
        consumedLabel.setText(consumed + " kcal");
        
        // Change color based on remaining calories
        double rem = Double.parseDouble(remaining);
        String remText = String.format("%.0f kcal", rem);
        remainingLabel.setText(remText);
        if (rem < 0) {
            remainingLabel.setForeground(Color.RED);
        } else {
            remainingLabel.setForeground(Color.GREEN);
        }
    }
    
    public String getFoodInput() {
        String input = foodInput.getText();
        foodInput.setText(""); // Clear input field after getting value
        return input;
    }

    public DefaultTableModel getFoodLogTableModel() {
        return foodLogTableModel;
    }
    
    public void addAddFoodListener(ActionListener listener) {
        addFoodButton.addActionListener(listener);
    }

    public void addSaveChangesListener(ActionListener listener) {
        saveChangesButton.addActionListener(listener);
    }
    
    public void addSwitchUserListener(ActionListener listener) {
        switchUserButton.addActionListener(listener);
    }
}
