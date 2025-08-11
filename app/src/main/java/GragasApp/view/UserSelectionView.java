package GragasApp.view;

import GragasApp.model.ActivityLevel;
import GragasApp.model.Sex;
import GragasApp.model.UserProfile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * A panel that provides the initial user interface for the application.
 * It allows a user to either select and load an existing profile from a dropdown
 * or create a new profile by filling out a form. It also handles unit selection
 * (metric/imperial) for new profile creation.
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


    /**
     * Constructs the UserSelectionView panel and initializes all its UI components.
     * The layout is split into two main sections: one for loading existing profiles
     * and one for creating new ones.
     */
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

        unitToggle.addActionListener(_ -> updateUnitLabels());

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

    /**
     * Updates the text labels for height and weight to reflect the currently
     * selected unit system (metric or imperial).
     */
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
    
    /**
     * Populates the user selection dropdown with a list of user profiles.
     * @param profiles A list of UserProfile objects to display.
     */
    public void populateUserList(List<UserProfile> profiles) {
        userComboBox.removeAllItems();
        for (UserProfile profile : profiles) {
            userComboBox.addItem(profile.getName());
        }
    }
    
    /**
     * Adds a single user profile to the selection dropdown.
     * @param profile The UserProfile to add.
     */
    public void addUserToList(UserProfile profile) {
        userComboBox.addItem(profile.getName());
    }

    /**
     * Gets the name of the user currently selected in the dropdown.
     * @return The selected user's name as a String.
     */
    public String getSelectedUser() {
        return (String) userComboBox.getSelectedItem();
    }

    /**
     * Adds an ActionListener to the "Load Profile" button.
     * @param listener The ActionListener to be notified of button clicks.
     */
    public void addLoadProfileListener(ActionListener listener) {
        loadProfileButton.addActionListener(listener);
    }


    /**
     * Adds an ActionListener to the "Create Profile" button.
     * @param listener The ActionListener to be notified of button clicks.
     */
    public void addCreateProfileListener(ActionListener listener) {
        createProfileButton.addActionListener(listener);
    }

    /**
     * Gets the user name from the new profile form.
     * @return The entered name.
     */
    public String getNewUserName() { return nameField.getText(); }

    /**
     * Gets the age from the new profile form.
     * @return The entered age.
     */
    public int getAge() { return Integer.parseInt(ageField.getText()); }


    /**
     * Gets the height from the new profile form.
     * @return The entered height.
     */
    public double getUserHeight() { return Double.parseDouble(heightField.getText()); }

    /**
     * Gets the weight from the new profile form.
     * @return The entered weight.
     */
    public double getWeight() { return Double.parseDouble(weightField.getText()); }

    /**
     * Gets the target weight from the new profile form.
     * @return The entered target weight.
     */
    public double getTargetWeight() { return Double.parseDouble(targetWeightField.getText()); }

    /**
     * Gets the selected sex from the new profile form.
     * @return The selected Sex enum value.
     */
    public Sex getSex() { return (Sex) sexComboBox.getSelectedItem(); }

    /**
     * Gets the selected activity level from the new profile form.
     * @return The selected ActivityLevel enum value.
     */
    public ActivityLevel getActivityLevel() { return (ActivityLevel) activityLevelComboBox.getSelectedItem(); }

    /**
     * Checks if the imperial unit system is selected.
     * @return true if imperial units are selected, false otherwise.
     */
    public boolean isImperial() { return unitToggle.isSelected(); }
    
    /**
     * Clears all input fields in the "Create New Profile" form.
     */
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
