package GragasApp.view;

import GragasApp.model.*;

import javax.swing.table.AbstractTableModel;
import java.util.List;
import java.util.Collections;

public class EntriesTable extends AbstractTableModel {
  private final String[] cols = {"Name", "Calories"};
  private List<? extends Loggable> data = Collections.emptyList();

  public EntriesTable(List<? extends Loggable> data) {
    this.data = data;
  }

  public void setData(List<? extends Loggable> data) {
    this.data = data;
    fireTableDataChanged();
  }

  @Override public int getRowCount() { return data == null ? 0 : data.size(); }
  @Override public int getColumnCount() { return cols.length; }
  @Override public String getColumnName(int col) { return cols[col]; }

  @Override
  public Object getValueAt(int rowIndex, int columnIndex) {
    Loggable e = data.get(rowIndex);
    return columnIndex == 0 ? e.getName() : e.getCalories();
  }
}

