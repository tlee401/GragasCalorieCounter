package GragasApp.view;
import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;

import GragasApp.controller.*;
import GragasApp.model.*;
import java.awt.*;
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

  public ProfilePanel(ProfileController controller) {
    setLayout(new GridBagLayout());
    GridBagConstraints c = new GridBagConstraints();
    c.insets = new Insets(6, 6, 6, 6);
    c.fill = GridBagConstraints.HORIZONTAL;

    int row = 0;
    addRow("Name", name, c, row++);
    addRow("Age (years)", age, c, row++);
    addRow("Height (in)", heightIn, c, row++);
    addRow("Weight (lbs)", weightLbs, c, row++);
    addRow("Target (lbs)", targetLbs, c, row++);
    addRow("Sex", sex, c, row++);
    addRow("Activity", activity, c, row++);

    JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
    JButton create = new JButton("Create");
    JButton save = new JButton("Save");
    buttons.add(create);
    buttons.add(save);

    c.gridx = 0; c.gridy = row; c.gridwidth = 2;
    add(buttons, c);

    create.addActionListener(e -> {
      controller.createProfile(
          name.getText().trim(),
          parseInt(age),
          parseInt(heightIn),
          parseDouble(weightLbs),
          parseDouble(targetLbs),
          (Sex) sex.getSelectedItem(),
          (ActivityLevel) activity.getSelectedItem()
      );
    });

    save.addActionListener(e -> controller.saveCurrentProfile());
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
      c.gridx = 1; c.gridy = row; c.weightx = 1; add(field, c);
      c.weightx = 0;
    }
}
