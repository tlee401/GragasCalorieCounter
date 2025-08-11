package GragasApp.view;
import GragasApp.model.*;
import GragasApp.controller.*;

import javax.swing.*;
import java.awt.*;

public class CalculatorPanel extends JPanel {
  private final JLabel bmrValue  = new JLabel("-");
  private final JLabel tdeeValue = new JLabel("-");

  public CalculatorPanel(CalculatorController controller) {
    setLayout(new BorderLayout());

    JPanel grid = new JPanel(new GridLayout(2, 2, 8, 8));
    grid.add(new JLabel("BMR (kcal/day):"));
    grid.add(bmrValue);
    grid.add(new JLabel("TDEE (kcal/day):"));
    grid.add(tdeeValue);

    JButton calc = new JButton("Calculate");
    calc.addActionListener(e -> {
      CalcResult res = controller.calculateBmrAndTdee();
      bmrValue.setText(String.format("%.0f", res.getBmr()));
      tdeeValue.setText(String.format("%.0f", res.getTdee()));
    });

    add(grid, BorderLayout.NORTH);
    add(calc, BorderLayout.SOUTH);
  }
}
