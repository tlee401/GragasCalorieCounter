package GragasApp.view;
import GragasApp.controller.*;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class LoginPanel extends JPanel {

  public interface Listener {
    void onOpenExisting(String name);
    void onCreateNew();
  }

  private final JComboBox<String> names;
  private final JButton open = new JButton("Open");
  private final JButton create = new JButton("Create new profile");

  public LoginPanel(ProfileController controller, Listener listener) {
    setLayout(new GridBagLayout());
    GridBagConstraints c = new GridBagConstraints();
    c.insets = new Insets(8, 8, 8, 8);
    c.fill = GridBagConstraints.HORIZONTAL;
    c.weightx = 1;

    JLabel title = new JLabel("Select your profile");
    title.setFont(title.getFont().deriveFont(Font.BOLD, 16f));
    c.gridx = 0; c.gridy = 0; c.gridwidth = 2;
    add(title, c);

    //TODO add logic to get only the profile names from the user
    List<String> profileNames = controller.listOfProfiles();
    names = new JComboBox<>(profileNames.toArray(new String[0]));
    names.setEditable(false);

    c.gridy = 1; c.gridwidth = 2;
    add(names, c);

    JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    buttons.add(open);
    buttons.add(create);

    c.gridy = 2; c.gridwidth = 2;
    add(buttons, c);

    open.addActionListener(e -> {
      String selected = (String) names.getSelectedItem();
      if (selected == null || selected.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Please choose a profile name.");
        return;
      }
      listener.onOpenExisting(selected);
    });

    create.addActionListener(e -> listener.onCreateNew());

    // If there are no profiles yet, disable Open
    open.setEnabled(!profileNames.isEmpty());
  }
}

