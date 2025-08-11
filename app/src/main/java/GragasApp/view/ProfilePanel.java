package GragasApp.view;

import GragasApp.controller.ProfileController;
import GragasApp.model.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import javax.swing.*;

public class ProfilePanel extends JPanel {
  private final JTextField name = new JTextField(16);
  private final JFormattedTextField age =
      new JFormattedTextField(NumberFormat.getIntegerInstance());
  private final JFormattedTextField heightIn =
      new JFormattedTextField(NumberFormat.getIntegerInstance());
  private final JFormattedTextField weightLbs =
      new JFormattedTextField(NumberFormat.getNumberInstance());
  private final JFormattedTextField targetLbs =
      new JFormattedTextField(NumberFormat.getNumberInstance());

  private final JComboBox<Sex> sex = new JComboBox<>(Sex.values());
  private final JComboBox<ActivityLevel> activity =
      new JComboBox<>(ActivityLevel.values());
  
  private final JButton create = new JButton("Create Profile");
  private final JButton save = new JButton("Save Changes");
  private final JButton back = new JButton("Back to Login");

  private final ProfileController controller;
  private final ActionListener backToLoginListener;
  private boolean isCreateMode = false;

  public ProfilePanel(ProfileController controller, ActionListener backToLoginListener) {
    this.controller = controller;
    this.backToLoginListener = backToLoginListener;
    setLayout(new GridBagLayout());
    GridBagConstraints c = new GridBagConstraints();
    c.insets = new Insets(6, 6, 6, 6);
    c.fill = GridBagConstraints.HORIZONTAL;
    
    JLabel title = new JLabel("Profile Management", SwingConstants.CENTER);
    title.setFont(new Font("Serif", Font.BOLD, 24));
    c.gridx = 0; c.gridy = 0; c.gridwidth = 2;
    add(title, c);

    int row = 1;
    addRow("Name", name, c, row++);
    addRow("Age (years)", age, c, row++);
    addRow("Height (in)", heightIn, c, row++);
    addRow("Weight (lbs)", weightLbs, c, row++);
    addRow("Target Weight (lbs)", targetLbs, c, row++);
    addRow("Sex", sex, c, row++);
    addRow("Activity", activity, c, row++);

    JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER));
    buttons.add(create);
    buttons.add(save);
    buttons.add(back);

    c.gridx = 0; c.gridy = row; c.gridwidth = 2;
    add(buttons, c);

    create.addActionListener(e -> {
      if (isCreateMode) {
        controller.createProfile(
            name.getText().trim(),
            parseInt(age),
            parseInt(heightIn),
            parseDouble(weightLbs),
            parseDouble(targetLbs),
            (Sex) sex.getSelectedItem(),
            (ActivityLevel) activity.getSelectedItem()
        );
        backToLoginListener.actionPerformed(null);
      }
    });

    save.addActionListener(e -> {
      UserProfile user = controller.getCurrentUserProfile();
      if (user != null) {
        user.setAge(parseInt(age));
        user.setHeightInches(parseInt(heightIn));
        user.setWeightLbs(parseDouble(weightLbs));
        user.setTargetWeightLbs(parseDouble(targetLbs));
        user.setSex((Sex) sex.getSelectedItem());
        user.setActivityLevel((ActivityLevel) activity.getSelectedItem());
        controller.saveCurrentProfile();
        JOptionPane.showMessageDialog(this, "Profile updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
      }
    });

    back.addActionListener(backToLoginListener);
    
    // Initial state for tabs
    updateFieldsFromProfile(controller.getCurrentUserProfile());
  }
  
  public void clearFieldsForNewProfile() {
      name.setText("");
      name.setEditable(true);
      age.setText("");
      heightIn.setText("");
      weightLbs.setText("");
      targetLbs.setText("");
      sex.setSelectedItem(null);
      activity.setSelectedItem(null);
      create.setVisible(true);
      save.setVisible(false);
      back.setVisible(true);
      isCreateMode = true;
  }
  
  public void updateFieldsFromProfile(UserProfile user) {
    if (user != null) {
      name.setText(user.getName());
      name.setEditable(false);
      age.setText(String.valueOf(user.getAge()));
      heightIn.setText(String.valueOf(user.getHeightInches()));
      weightLbs.setText(String.valueOf(user.getWeightLbs()));
      targetLbs.setText(String.valueOf(user.getTargetWeightLbs()));
      sex.setSelectedItem(user.getSex());
      activity.setSelectedItem(user.getActivityLevel());
      create.setVisible(false);
      save.setVisible(true);
      back.setVisible(false);
      isCreateMode = false;
    }
  }

  private static int parseInt(JFormattedTextField f) {
    Object v = f.getValue();
    return v == null ? 0 : ((Number) v).intValue();
  }

  private static double parseDouble(JFormattedTextField f) {
    Object v = f.getValue();
    return v == null ? 0.0 : ((Number) v).doubleValue();
  }
    
  private void addRow(String label, JComponent field, GridBagConstraints c, int row) {
    c.gridx = 0; c.gridy = row; c.gridwidth = 1; add(new JLabel(label), c);
    c.gridx = 1; c.gridy = row; c.gridwidth = 1; add(field, c);
  }
  
  public void focusNameField() {
    name.requestFocusInWindow();
  }
}
