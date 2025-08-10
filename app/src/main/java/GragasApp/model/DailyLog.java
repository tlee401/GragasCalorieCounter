package GragasApp.model;

import java.time.LocalDate;

//stub class need to add the loggable Entry list
public class DailyLog {
  private final LocalDate date;
  public DailyLog(LocalDate date) { this.date = date; }
  public LocalDate getDate() { return date; }
}