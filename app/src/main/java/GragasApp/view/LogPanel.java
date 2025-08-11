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

  private final JTable table;
  private final EntriesTable tableModel;

  // In a real app, the controller would refresh this from the model
  private final List<Loggable> entries = LogController.getEntries();

  public LogPanel(LogController controller, CalorieLookupService lookup) {
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

    // Actions
    addFood.addActionListener(e -> {
      String desc = freeText.getText().trim();
      if (desc.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Please enter a food description.");
        return;
      }
      addFood.setEnabled(false);

      // TODO change when I have a better Idea of how the API call works
      // Background API call to keep UI responsive
      // SwingWorker<Integer, Void> worker = new SwingWorker<>() {
      //   @Override
      //   protected Integer doInBackground() throws Exception {
      //     return lookup.estimateCalories(desc);
      //   }
      //   @Override
      //   protected void done() {
      //     addFood.setEnabled(true);
      //     try {
      //       int calories = get();
      //       FoodEntry entry = new FoodEntry(desc, calories);
      //       controller.addFood(entry, LocalDate.now());
      //       // For demo, update local list. In production, controller should refresh from model.
      //       entries.add(entry);
      //       tableModel.fireTableDataChanged();
      //       freeText.setText("");
      //     } catch (Exception ex) {
      //       JOptionPane.showMessageDialog(LogPanel.this,
      //           "Could not estimate calories: " + ex.getMessage(),
      //           "Lookup error",
      //           JOptionPane.ERROR_MESSAGE);
      //     }
      //   }
      // };
      // worker.execute();
    });

    removeSelected.addActionListener(e -> {
      int row = table.getSelectedRow();
      if (row < 0) return;
      Loggable entry = entries.get(row);
      controller.removeFood((FoodEntry) entry, LocalDate.now());
      entries.remove(row);
      tableModel.fireTableRowsDeleted(row, row);
    });
  }
}
