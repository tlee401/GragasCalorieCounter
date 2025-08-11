package GragasApp.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

//stub class need to add the loggable Entry list
public class DailyLog {
  private final LocalDate date;
  private final List<Loggable> entries = new ArrayList<>();

  public DailyLog(LocalDate date) {
    this.date = date;
  }
  public LocalDate getDate() {
    return date;
  }

}