package GragasApp.view;

import GragasApp.model.*;
import GragasApp.controller.*;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class SummaryPanel extends JPanel {
  private final JLabel totalCaloriesToday = new JLabel("-");
  private final JLabel poundsToGoal = new JLabel("-");
  private final UserProfileManager userProfileManager;

  public SummaryPanel(UserProfileManager userProfileManager) {
    this.userProfileManager = userProfileManager;
    setLayout(new GridLayout(2, 2, 8, 8));
    setBorder(BorderFactory.createTitledBorder("Today's Summary"));
    add(new JLabel("Total calories today:"));
    add(totalCaloriesToday);
    add(new JLabel("Pounds to goal:"));
    add(poundsToGoal);
    
    refresh();
  }

  public void refresh() {
    UserProfile user = userProfileManager.getCurrentUserProfile();
    if (user != null) {
      // Find today's log to get total calories
      int cals = user.getLogs().stream()
          .filter(log -> log.getDate().equals(LocalDate.now()))
          .findFirst()
          .map(DailyLog::getTotalCalories)
          .orElse(0);
      
      setTotalCaloriesToday(cals);
      setPoundsToGoal(user.getWeightLbs() - user.getTargetWeightLbs());
    } else {
        setTotalCaloriesToday(0);
        setPoundsToGoal(0.0);
    }
  }
  
  public void setTotalCaloriesToday(int cals) {
    totalCaloriesToday.setText(String.valueOf(cals));
  }
  public void setPoundsToGoal(double lbs) {
    poundsToGoal.setText(String.format("%.1f", lbs));
  }
}
