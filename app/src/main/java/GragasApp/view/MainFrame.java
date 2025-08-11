package GragasApp.view;
import GragasApp.controller.*;
import GragasApp.model.*;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;


public class MainFrame extends JFrame {
  private final CardLayout rootCards = new CardLayout();
  private final JPanel root = new JPanel(rootCards);
  private final JTabbedPane tabs = new JTabbedPane();
  private final LoginPanel loginView;
  private final ProfilePanel profileView;
  private final HomePagePanel homeView;
  private final LogPanel logView;
  private final LogHistoryPanel historyView;
  private final CalculatorPanel calculatorView;
  private UserProfileManager userProfileManager;
  
  public MainFrame(UserProfileManager userProfileManager,
                   LogController logController,
                   CalorieLookupService calorieLookupService) {
    super("Gragas Calorie Counter");
    this.userProfileManager = userProfileManager;
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setSize(900, 600);
    setLocationRelativeTo(null);

    // Build the login card
    loginView = new LoginPanel(userProfileManager, new LoginPanel.Listener() {
        @Override
        public void onOpenExisting(String name) {
            userProfileManager.selectProfileByName(name);
            showApp();
        }
        @Override
        public void onCreateNew() {
            showProfileCreate();
        }
    });
    
    // Create controllers needed for views
    ProfileController profileController = new ProfileController(userProfileManager);
    CalculatorController calculatorController = new CalculatorController(userProfileManager);
    HistoryController historyController = new HistoryController(userProfileManager.getCurrentUserProfile());
    
    // Panels
    profileView = new ProfilePanel(profileController, e -> showLogin());
    logView = new LogPanel(logController, calorieLookupService);
    historyView = new LogHistoryPanel(historyController);
    calculatorView = new CalculatorPanel(calculatorController);
    homeView = new HomePagePanel(userProfileManager, e -> showLogin());

    tabs.addTab("Home", homeView);
    tabs.addTab("Log", logView);
    tabs.addTab("History", historyView);
    tabs.addTab("Profile", profileView);
    tabs.addTab("Calculator", calculatorView);

    // Add cards to the root panel
    root.add(loginView, "LOGIN");
    root.add(profileView, "PROFILE_CREATE");
    root.add(tabs,  "APP");
    
    getContentPane().add(root, BorderLayout.CENTER);
    showLogin();
  }

  public void showLogin() {
    userProfileManager.logout();
    loginView.refreshProfileList();
    rootCards.show(root, "LOGIN");
  }

  public void showApp() {
    // Refresh all panels with the new user's data
    homeView.updateWelcomeMessage(userProfileManager.getCurrentUserProfile().getName());
    logView.refreshEntries();
    historyView.refresh();
    
    rootCards.show(root, "APP");
    tabs.setSelectedComponent(homeView);
  }
  
  public void showProfileCreate() {
      profileView.clearFieldsForNewProfile();
      rootCards.show(root, "PROFILE_CREATE");
  }
}

