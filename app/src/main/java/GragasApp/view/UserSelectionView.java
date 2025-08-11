package GragasApp.view;

import GragasApp.model.ActivityLevel;
import GragasApp.model.Sex;
import GragasApp.model.UserProfile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Panel for selecting an existing user or creating a new one.
 */
public class UserSelectionView extends JPanel {

    // Components for selecting existing user
    private final JComboBox<String> userComboBox;
    private final JButton loadProfileButton;

    // Components for creating a new user
    private final JTextField nameField;
    private final JTextField ageField;
    private final JTextField heightField;
    private final JTextField weightField;
    private final JTextField targetWeightField;
    private final JComboBox<Sex> sexComboBox;
    private final JComboBox<ActivityLevel> activityLevelComboBox;
    private final JToggleButton unitToggle; // Metric vs Imperial
    private final JButton createProfileButton;
    private final JLabel heightLabel;
    private final JLabel weightLabel;
    private final JLabel targetWeightLabel;


    public UserSelectionView() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // --- Existing User Panel ---
        JPanel existingUserPanel = new JPanel(new GridBagLayout());
        existingUserPanel.setBorder(BorderFactory.createTitledBorder("Load Existing Profile"));
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.1;
        existingUserPanel.add(new JLabel("Select Profile:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.9;
        userComboBox = new JComboBox<>();
        existingUserPanel.add(userComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        loadProfileButton = new JButton("Load Profile");
        existingUserPanel.add(loadProfileButton, gbc);

        // --- New User Panel ---
        JPanel newUserPanel = new JPanel(new GridBagLayout());
        newUserPanel.setBorder(BorderFactory.createTitledBorder("Create New Profile"));

        GridBagConstraints newUserGbc = new GridBagConstraints();
        newUserGbc.insets = new Insets(5, 5, 5, 5);
        newUserGbc.anchor = GridBagConstraints.WEST;
        
        // Input fields
        nameField = new JTextField(20);
        ageField = new JTextField(5);
        heightField = new JTextField(10);
        weightField = new JTextField(10);
        targetWeightField = new JTextField(10);
        sexComboBox = new JComboBox<>(Sex.values());
        activityLevelComboBox = new JComboBox<>(ActivityLevel.values());
        createProfileButton = new JButton("Create Profile");

        // Unit selection
        unitToggle = new JToggleButton("Change units");
        heightLabel = new JLabel("Height (cm):");
        weightLabel = new JLabel("Weight (kg):");
        targetWeightLabel = new JLabel("Target Weight (kg):");

        unitToggle.addActionListener(e -> updateUnitLabels());

        int y = 0;
        newUserGbc.gridx = 0;
        newUserGbc.gridy = y;
        newUserPanel.add(new JLabel("Name:"), newUserGbc);
        newUserGbc.gridx = 1;
        newUserPanel.add(nameField, newUserGbc);
        
        y++;
        newUserGbc.gridx = 0;
        newUserGbc.gridy = y;
        newUserPanel.add(new JLabel("Age:"), newUserGbc);
        newUserGbc.gridx = 1;
        newUserPanel.add(ageField, newUserGbc);
        
        y++;
        newUserGbc.gridx = 0;
        newUserGbc.gridy = y;
        newUserPanel.add(new JLabel("Sex:"), newUserGbc);
        newUserGbc.gridx = 1;
        newUserPanel.add(sexComboBox, newUserGbc);

        y++;
        newUserGbc.gridx = 0;
        newUserGbc.gridy = y;
        newUserPanel.add(new JLabel("Activity Level:"), newUserGbc);
        newUserGbc.gridx = 1;
        newUserPanel.add(activityLevelComboBox, newUserGbc);

        y++;
        newUserGbc.gridx = 0;
        newUserGbc.gridy = y;
        newUserGbc.gridwidth = 2;
        newUserPanel.add(unitToggle, newUserGbc);
        newUserGbc.gridwidth = 1;

        y++;
        newUserGbc.gridx = 0;
        newUserGbc.gridy = y;
        newUserPanel.add(heightLabel, newUserGbc);
        newUserGbc.gridx = 1;
        newUserPanel.add(heightField, newUserGbc);

        y++;
        newUserGbc.gridx = 0;
        newUserGbc.gridy = y;
        newUserPanel.add(weightLabel, newUserGbc);
        newUserGbc.gridx = 1;
        newUserPanel.add(weightField, newUserGbc);

        y++;
        newUserGbc.gridx = 0;
        newUserGbc.gridy = y;
        newUserPanel.add(targetWeightLabel, newUserGbc);
        newUserGbc.gridx = 1;
        newUserPanel.add(targetWeightField, newUserGbc);

        y++;
        newUserGbc.gridx = 0;
        newUserGbc.gridy = y;
        newUserGbc.gridwidth = 2;
        newUserGbc.anchor = GridBagConstraints.CENTER;
        newUserPanel.add(createProfileButton, newUserGbc);

        // --- Add sub-panels to the main panel ---
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.weighty = 0.1;
        add(existingUserPanel, gbc);

        gbc.gridy = 1;
        gbc.weighty = 0.9;
        add(newUserPanel, gbc);
    }

    private void updateUnitLabels() {
        if (unitToggle.isSelected()) {
            heightLabel.setText("Height (in):");
            weightLabel.setText("Weight (lbs):");
            targetWeightLabel.setText("Target Weight (lbs):");
        } else {
            heightLabel.setText("Height (cm):");
            weightLabel.setText("Weight (kg):");
            targetWeightLabel.setText("Target Weight (kg):");
        }
    }
    
    // --- Public methods for Controller interaction ---
    public void populateUserList(List<UserProfile> profiles) {
        userComboBox.removeAllItems();
        for (UserProfile profile : profiles) {
            userComboBox.addItem(profile.getName());
        }
    }
    
    public void addUserToList(UserProfile profile) {
        userComboBox.addItem(profile.getName());
    }

    public String getSelectedUser() {
        return (String) userComboBox.getSelectedItem();
    }

    public void addLoadProfileListener(ActionListener listener) {
        loadProfileButton.addActionListener(listener);
    }


    public void addCreateProfileListener(ActionListener listener) {
        createProfileButton.addActionListener(listener);
    }

    // Getters for creation form
    public String getNewUserName() { return nameField.getText(); }
    public int getAge() { return Integer.parseInt(ageField.getText()); }
    public double getUserHeight() { return Double.parseDouble(heightField.getText()); }
    public double getWeight() { return Double.parseDouble(weightField.getText()); }
    public double getTargetWeight() { return Double.parseDouble(targetWeightField.getText()); }
    public Sex getSex() { return (Sex) sexComboBox.getSelectedItem(); }
    public ActivityLevel getActivityLevel() { return (ActivityLevel) activityLevelComboBox.getSelectedItem(); }
    public boolean isImperial() { return unitToggle.isSelected(); }
    
    public void clearCreationFields() {
        nameField.setText("");
        ageField.setText("");
        heightField.setText("");
        weightField.setText("");
        targetWeightField.setText("");
        sexComboBox.setSelectedIndex(0);
        activityLevelComboBox.setSelectedIndex(0);
        unitToggle.setSelected(false);
        updateUnitLabels();
    }
}
