package GragasApp.view;

import GragasApp.controller.HistoryController;
import GragasApp.model.DailyLog;

import javax.swing.*;
import java.awt.*;
import java.util.Collections;
import java.util.List;
import java.time.LocalDate;


public class LogHistoryPanel extends JPanel {
    private final HistoryController controller;
    private final JPanel historyListPanel;

    public LogHistoryPanel(HistoryController controller) {
        this.controller = controller;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel title = new JLabel("Log History", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 24));
        add(title, BorderLayout.NORTH);

        historyListPanel = new JPanel();
        historyListPanel.setLayout(new BoxLayout(historyListPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(historyListPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void refresh() {
        historyListPanel.removeAll();
        List<DailyLog> logs = controller.getDailyLogs();
        Collections.reverse(logs); // Display most recent first
        
        if (logs.isEmpty()) {
            historyListPanel.add(new JLabel("No log history available."));
        } else {
            for (DailyLog log : logs) {
                JPanel logEntryPanel = new JPanel(new BorderLayout());
                logEntryPanel.setBorder(BorderFactory.createTitledBorder("Log for " + log.getDate().toString() + " - Total Calories: " + log.getTotalCalories()));
                
                EntriesTable tableModel = new EntriesTable(log.getEntries());
                JTable table = new JTable(tableModel);
                JScrollPane tableScrollPane = new JScrollPane(table);
                logEntryPanel.add(tableScrollPane, BorderLayout.CENTER);
                
                historyListPanel.add(logEntryPanel);
                historyListPanel.add(Box.createVerticalStrut(10)); // Spacer
            }
        }
        historyListPanel.revalidate();
        historyListPanel.repaint();
    }
}