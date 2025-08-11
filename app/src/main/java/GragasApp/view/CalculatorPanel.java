package GragasApp.view;
import GragasApp.controller.*;

import javax.swing.*;
import java.awt.*;

public class CalculatorPanel extends JPanel {
  private final JLabel bmrValue  = new JLabel(" - ");
  private final JLabel tdeeValue = new JLabel(" - ");
  private final CalculatorController controller;

  public CalculatorPanel(CalculatorController controller) {
    this.controller = controller;
    setLayout(new BorderLayout(10, 10));
    setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    JLabel title = new JLabel("BMR and TDEE Calculator", SwingConstants.CENTER);
    title.setFont(new Font("Serif", Font.BOLD, 24));
    add(title, BorderLayout.NORTH);

    JPanel grid = new JPanel(new GridLayout(2, 2, 8, 8));
    grid.add(new JLabel("BMR (kcal/day):"));
    grid.add(bmrValue);
    grid.add(new JLabel("TDEE (kcal/day):"));
    grid.add(tdeeValue);

    JButton calc = new JButton("Calculate");
    calc.addActionListener(e -> {
      CalcResult res = controller.calculateBmrAndTdee();
      if (res != null) {
        bmrValue.setText(String.format("%.0f", res.getBmr()));
        tdeeValue.setText(String.format("%.0f", res.getTdee()));
      }
    });

    JPanel centerPanel = new JPanel(new BorderLayout());
    centerPanel.add(grid, BorderLayout.NORTH);
    centerPanel.add(calc, BorderLayout.SOUTH);
    
    add(centerPanel, BorderLayout.CENTER);
  }
}
