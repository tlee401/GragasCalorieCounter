package GragasApp.view;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import GragasApp.model.DailyLog;
import GragasApp.model.Loggable;

/**
 * A view that displays all historical daily logs for a user.
 * It shows a list of dates and the food entries for the selected date.
 */
public class AllLogsView extends JPanel {

    private final JList<LocalDate> dateList;
    private final DefaultListModel<LocalDate> dateListModel;
    private final JTable logEntriesTable;
    private final DefaultTableModel logEntriesTableModel;
    private final JButton backButton;

    public AllLogsView() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createTitledBorder("All Daily Logs"));

        // --- West Panel (Date List) ---
        dateListModel = new DefaultListModel<>();
        dateList = new JList<>(dateListModel);
        dateList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        // Custom cell renderer to format the date nicely
        dateList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component renderer = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof LocalDate) {
                    setText(((LocalDate) value).format(DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy")));
                }
                return renderer;
            }
        });

        JScrollPane dateScrollPane = new JScrollPane(dateList);
        dateScrollPane.setPreferredSize(new Dimension(250, 0));
        add(dateScrollPane, BorderLayout.WEST);

        // --- Center Panel (Log Entries Table) ---
        String[] columnNames = {"Food", "Calories"};
        logEntriesTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        logEntriesTable = new JTable(logEntriesTableModel);
        JScrollPane tableScrollPane = new JScrollPane(logEntriesTable);
        add(tableScrollPane, BorderLayout.CENTER);

        // --- South Panel (Action Button) ---
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backButton = new JButton("Back to Dashboard");
        actionPanel.add(backButton);
        add(actionPanel, BorderLayout.SOUTH);
    }

    /**
     * Populates the list of dates from the user's daily logs.
     * @param logs The list of DailyLog objects.
     */
    public void populateLogDates(List<DailyLog> logs) {
        dateListModel.clear();
        logs.stream()
            .map(DailyLog::getDate)
            .sorted(Comparator.reverseOrder()) // Show most recent dates first
            .forEach(dateListModel::addElement);
    }

    /**
     * Updates the food entries table based on the selected log.
     * @param log The DailyLog to display.
     */
    public void updateLogEntries(DailyLog log) {
        logEntriesTableModel.setRowCount(0); // Clear previous entries
        if (log != null) {
            for (Loggable entry : log.getEntries()) {
                logEntriesTableModel.addRow(new Object[]{entry.getName(), String.format("%.1f", entry.getCalories())});
            }
        }
    }

    public LocalDate getSelectedDate() {
        return dateList.getSelectedValue();
    }

    public void addDateSelectionListener(ListSelectionListener listener) {
        dateList.addListSelectionListener(listener);
    }

    public void addBackToDashboardListener(ActionListener listener) {
        backButton.addActionListener(listener);
    }
}
