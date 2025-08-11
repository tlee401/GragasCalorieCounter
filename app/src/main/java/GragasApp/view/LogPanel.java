package GragasApp.view;
import GragasApp.model.*;
import GragasApp.controller.*;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LogPanel extends JPanel {
  private final JTextArea freeText = new JTextArea(4, 40);
  private final JButton addFood = new JButton("Add Food");
  private final JButton removeSelected = new JButton("Remove Selected");
  private final JLabel totalCaloriesLabel = new JLabel("Total Calories Today: 0");

  private final JTable table;
  private final EntriesTable tableModel;
  private final LogController controller;
  private final CalorieLookupService lookup;
  private final List<Loggable> entries = new ArrayList<>();

  public LogPanel(LogController controller, CalorieLookupService lookup) {
    this.controller = controller;
    this.lookup = lookup;
    setLayout(new BorderLayout(8, 8));

    // Top: free text input and buttons
    JPanel top = new JPanel(new BorderLayout(6, 6));
    top.add(new JLabel("Describe what you ate:"), BorderLayout.NORTH);
    top.add(new JScrollPane(freeText), BorderLayout.CENTER);

    JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
    buttons.add(addFood);
    buttons.add(removeSelected);
    top.add(buttons, BorderLayout.SOUTH);

    add(top, BorderLayout.NORTH);

    // Center: table of today's entries
    tableModel = new EntriesTable(entries);
    table = new JTable(tableModel);
    add(new JScrollPane(table), BorderLayout.CENTER);
    
    // Bottom: Total calories display
    JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    bottomPanel.add(totalCaloriesLabel);
    add(bottomPanel, BorderLayout.SOUTH);


    addFood.addActionListener(e -> {
      String desc = freeText.getText().trim();
      if (desc.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Please enter a food description.", "Input Error", JOptionPane.WARNING_MESSAGE);
        return;
      }
      addFood.setEnabled(false);
      removeSelected.setEnabled(false);

      SwingWorker<Void, Void> worker = new SwingWorker<>() {
        @Override
        protected Void doInBackground() throws Exception {
          controller.addFood(desc);
          return null;
        }
        @Override
        protected void done() {
          addFood.setEnabled(true);
          removeSelected.setEnabled(true);
          try {
            get();
            refreshEntries();
            freeText.setText("");
          } catch (Exception ex) {
            JOptionPane.showMessageDialog(LogPanel.this,
                "Could not estimate calories: " + ex.getMessage(),
                "Lookup error",
                JOptionPane.ERROR_MESSAGE);
          }
        }
      };
      worker.execute();
    });

    removeSelected.addActionListener(e -> {
      int row = table.getSelectedRow();
      if (row < 0) return;
      Loggable entry = entries.get(row);
      controller.removeFood(entry, LocalDate.now());
      refreshEntries();
    });
  }
  
  public void refreshEntries() {
      List<Loggable> newEntries = controller.getEntries(LocalDate.now());
      entries.clear();
      entries.addAll(newEntries);
      tableModel.setData(entries);
      totalCaloriesLabel.setText("Total Calories Today: " + controller.getTotalCaloriesForToday());
  }
}
