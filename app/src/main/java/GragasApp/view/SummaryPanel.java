package GragasApp.view;

import GragasApp.model.*;
import GragasApp.controller.*;

import javax.swing.*;
import java.awt.*;

public class SummaryPanel extends JPanel {
  private final JLabel totalCaloriesToday = new JLabel("-");
  private final JLabel poundsToGoal = new JLabel("-");

  public SummaryPanel() {
    setLayout(new GridLayout(2, 2, 8, 8));
    add(new JLabel("Total calories today:"));
    add(totalCaloriesToday);
    add(new JLabel("Pounds to goal:"));
    add(poundsToGoal);
  }

  // TODO Call these from controller after model updates
  public void setTotalCaloriesToday(int cals) {
    totalCaloriesToday.setText(String.valueOf(cals));
  }
  public void setPoundsToGoal(double lbs) {
    poundsToGoal.setText(String.format("%.1f", lbs));
  }
}
