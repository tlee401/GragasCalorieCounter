package GragasApp.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//stub class need to add the loggable Entry list
public class DailyLog {
  private final LocalDate date;
  private final List<Loggable> entries = new ArrayList<>();

  public DailyLog(LocalDate date) {
    if (date == null) throw new IllegalArgumentException("date cannot be null");
    this.date = date;
  }
  public LocalDate getDate() {
    return date;
  }

  public List<Loggable> getEntries() {
    return Collections.unmodifiableList(entries);
  }

  public void addEntry(Loggable item) {
    if (item == null) throw new IllegalArgumentException("item cannot be null");
    entries.add(item);
  }

  public boolean removeEntry(Loggable item) {
    return entries.remove(item);
  }

  public void clear() {
    entries.clear();
  }

  public int getTotalCalories() {
    int sum = 0;
    for (Loggable item : entries) {
      sum += item.getCalories();
    }
    return sum;
  }

  public int size() {
    return entries.size();
  }

  public boolean isEmpty() {
    return entries.isEmpty();
  }
}