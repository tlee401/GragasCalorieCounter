package GragasApp.view;
import GragasApp.controller.*;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Vector;

public class LoginPanel extends JPanel {

  public interface Listener {
    void onOpenExisting(String name);
    void onCreateNew();
  }

  private final JComboBox<String> names;
  private final JButton open = new JButton("Open");
  private final JButton create = new JButton("Create new profile");
  private final UserProfileManager userProfileManager;

  public LoginPanel(UserProfileManager userProfileManager, Listener listener) {
    this.userProfileManager = userProfileManager;
    setLayout(new GridBagLayout());
    GridBagConstraints c = new GridBagConstraints();
    c.insets = new Insets(8, 8, 8, 8);
    c.fill = GridBagConstraints.HORIZONTAL;
    c.weightx = 1;

    JLabel title = new JLabel("Select your profile", SwingConstants.CENTER);
    title.setFont(title.getFont().deriveFont(Font.BOLD, 16f));
    c.gridx = 0; c.gridy = 0; c.gridwidth = 2;
    add(title, c);

    List<String> profileNames = userProfileManager.getProfileNames();
    names = new JComboBox<>(new Vector<>(profileNames));
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
      if (selected != null && !selected.isEmpty()) {
        listener.onOpenExisting(selected);
      } else {
        JOptionPane.showMessageDialog(this, "Please select a profile or create a new one.", "No Profile Selected", JOptionPane.WARNING_MESSAGE);
      }
    });
    
    create.addActionListener(e -> listener.onCreateNew());
  }

  public void refreshProfileList() {
    names.removeAllItems();
    userProfileManager.getProfileNames().forEach(names::addItem);
  }
}

