package GragasApp.view;
import GragasApp.controller.*;
import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;


public class MainFrame extends JFrame {
  private final JTabbedPane tabs = new JTabbedPane();

  public MainFrame(ProfileController profileController,
      CalculatorController calculatorController,
      LogController logController,
      CalorieLookupService calorieLookupService) {
    super("Gragas Calorie Counter");
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setSize(900, 600);
    setLocationRelativeTo(null);

    // Panels
    ProfilePanel profile = new ProfilePanel(profileController);
    CalculatorPanel calculator = new CalculatorPanel(calculatorController);
    LogPanel log = new LogPanel(logController, calorieLookupService);
    SummaryPanel summary = new SummaryPanel();

    tabs.addTab("Profile", profile);
    tabs.addTab("Calculator", calculator);
    tabs.addTab("Log", log);
    tabs.addTab("Summary", summary);

    getContentPane().add(tabs, BorderLayout.CENTER);
  }

}
