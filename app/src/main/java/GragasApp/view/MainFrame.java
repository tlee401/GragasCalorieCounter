package GragasApp.view;
import GragasApp.controller.*;
import GragasApp.model.*;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;


public class MainFrame extends JFrame {
  // Root cards
  private final CardLayout rootCards = new CardLayout();
  private final JPanel root = new JPanel(rootCards);

  private final JTabbedPane tabs = new JTabbedPane();
  private final ProfilePanel profileView;
  private final CalculatorPanel calculatorView;
  private final LogPanel logView;
  private final SummaryPanel summaryView;

  public MainFrame(ProfileController profileController,
      CalculatorController calculatorController,
      LogController logController,
      CalorieLookupService calorieLookupService) {
    super("Gragas Calorie Counter");
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setSize(900, 600);
    setLocationRelativeTo(null);

    // Build the login card
    LoginPanel login = new LoginPanel(profileController, new LoginPanel.Listener() {
      @Override
      public void onOpenExisting(String name) {
        // Load model, then show tabs at Log
        profileController.selectProfileByName(name);
        showApp();
        tabs.setSelectedComponent(logView);
      }
      @Override
      public void onCreateNew() {
        // Show tabs at Profile, empty fields ready for creation
        showApp();
        tabs.setSelectedComponent(profileView);
        profileView.setNameText("");
        profileView.focusNameField();
      }
    });

    // Panels
    profileView = new ProfilePanel(profileController);
    calculatorView = new CalculatorPanel(calculatorController);
    logView = new LogPanel(logController, calorieLookupService);
    summaryView = new SummaryPanel();

    tabs.addTab("Profile", profileView);
    tabs.addTab("Calculator", calculatorView);
    tabs.addTab("Log", logView);
    tabs.addTab("Summary", summaryView);

    // Add cards to the root panel
    root.add(login, "LOGIN");
    root.add(tabs,  "APP");

    // Add the root panel to the MainFrame's content pane
    getContentPane().add(root, BorderLayout.CENTER);
    showLogin();
  }

  public void showLogin() {
    rootCards.show(root, "LOGIN");
  }

  public void showApp() {
    rootCards.show(root, "APP");
  }
}

