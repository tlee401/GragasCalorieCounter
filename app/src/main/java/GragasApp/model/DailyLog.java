package GragasApp.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A per-day container of {Loggable} entries (e.g., foods, activities).
 *
 * The log is identified by an immutable {LocalDate} and maintains an
 * internal, mutable list of entries. The list returned by {#getEntries()}
 * is read-only; modify the log via {#addEntry(Loggable)},
 * {#removeEntry(Loggable)}, or {#clear()}.
 *   {#getTotalCalories()} sums {getCalories()} across all entries
 *       using {int}. If your {Loggable} values are large or fractional,
 *       consider an alternative total method.
 * @see Loggable
 */
public class DailyLog {
  private final LocalDate date;
  private final List<Loggable> entries = new ArrayList<>();

  /**
   * Creates a log for the given calendar date.
   *
   * @param date the date this log represents (must not be {@code null})
   * @throws IllegalArgumentException if {@code date} is {@code null}
   */
  public DailyLog(LocalDate date) {
    if (date == null) throw new IllegalArgumentException("date cannot be null");
    this.date = date;
  }

  /**
   * Returns the date associated with this log.
   *
   * @return the log date
   */
  public LocalDate getDate() {
    return date;
  }

  /**
   * Returns an unmodifiable view of the entries in insertion order.
   *
   * @return read-only list of log entries
   */
  public List<Loggable> getEntries() {
    return Collections.unmodifiableList(entries);
  }

  /**
   * Adds a new {Loggable} item to the end of the log.
   *
   * @param item the entry to add (must not be {null})
   * @throws IllegalArgumentException if {item} is {null}
   */
  public void addEntry(Loggable item) {
    if (item == null) throw new IllegalArgumentException("item cannot be null");
    entries.add(item);
  }

  /**
   * Removes the first occurrence of the given {Loggable} from the log.
   *
   * @param item the entry to remove
   * @return {true} if an element was removed; {false} otherwise
   */
  public boolean removeEntry(Loggable item) {
    return entries.remove(item);
  }

  /**
   * Removes all entries from this log.
   */
  public void clear() {
    entries.clear();
  }

  /**
   * Computes the sum of calories across all entries.
   *
   * The meaning of the sign (e.g., intake vs. expenditure) depends on the
   * {Loggable} implementation.
   *
   * @return total calories as an {int}
   */
  public int getTotalCalories() {
    int sum = 0;
    for (Loggable item : entries) {
      sum += item.getCalories();
    }
    return sum;
  }

  /**
   * Returns the number of entries currently in the log.
   *
   * @return entry count
   */
  public int size() {
    return entries.size();
  }

  /**
   * Indicates whether the log contains no entries.
   *
   * @return {true} if there are no entries; {false} otherwise
   */
  public boolean isEmpty() {
    return entries.isEmpty();
  }
}