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
 * This panel is composed of a list of selectable dates on the west and a table on the center
 * that shows the food entries for the selected date. It provides a way for users to review
 * their past caloric intake.
 */
public class AllLogsView extends JPanel {

    private final JList<LocalDate> dateList;
    private final DefaultListModel<LocalDate> dateListModel;
    private final JTable logEntriesTable;
    private final DefaultTableModel logEntriesTableModel;
    private final JButton backButton;

    /**
     * Constructs the AllLogsView panel and initializes all its UI components.
     * It sets up the date list, the log entries table, and the navigation button.
     */
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
     * Populates the list of dates from the user's collection of daily logs.
     * The dates are sorted in reverse chronological order.
     * @param logs The list of DailyLog objects from the user's profile.
     */
    public void populateLogDates(List<DailyLog> logs) {
        dateListModel.clear();
        logs.stream()
            .map(DailyLog::getDate)
            .sorted(Comparator.reverseOrder()) // Show most recent dates first
            .forEach(dateListModel::addElement);
    }

    /**
     * Updates the food entries table to display the contents of a specific daily log.
     * If the provided log is null, the table is cleared.
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

    /**
     * Gets the date currently selected in the date list.
     * @return The selected LocalDate, or null if there is no selection.
     */
    public LocalDate getSelectedDate() {
        return dateList.getSelectedValue();
    }

    /**
     * Adds an ActionListener to handle changes in the date list selection.
     * @param listener The ListSelectionListener to be notified of selection changes.
     */
    public void addDateSelectionListener(ListSelectionListener listener) {
        dateList.addListSelectionListener(listener);
    }

    /**
     * Adds an ActionListener to the "Back to Dashboard" button.
     * @param listener The ActionListener to be notified of button clicks.
     */
    public void addBackToDashboardListener(ActionListener listener) {
        backButton.addActionListener(listener);
    }
}
