
// Import statements to access classes from the main application source folders
import GragasApp.controller.*;
import GragasApp.model.*;
import GragasApp.view.AllLogsView;
import GragasApp.view.DashboardView;
import GragasApp.view.MainView;
import GragasApp.view.UserSelectionView;

// JUnit 4 and standard Java imports
import org.junit.Before;
import org.junit.Test;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

/**
 * JUnit 4 tests for the AppController class.
 * This version does not use Mockito. Instead, it uses hand-written stub classes
 * (defined as inner classes) to simulate the behavior of the View and Model components
 * for isolated testing of the controller's logic.
 */
public class AppControllerTestJunit4 {

    // Stubs for the views and data handler
    private MainViewStub mainView;
    private CSVHandlerStub csvHandler;
    private AppController appController;

    private UserProfile testUser;

    /**
     * Sets up the test environment before each test case.
     * This method initializes the controller with our stub implementations, which
     * automatically attaches the controller's private methods as listeners.
     */
    @Before
    public void setUp() {
        mainView = new MainViewStub();
        csvHandler = new CSVHandlerStub();
        // The AppController constructor will call attachListeners, setting up our stubs.
        appController = new AppController(mainView, csvHandler);

        // Create a sample user for testing
        testUser = new UserProfile("TestUser", 30, 180, ActivityLevel.MODERATE, Sex.MALE, 80, 75);
    }

    /**
     * Tests the successful creation of a new user profile using metric units.
     */
    @Test
    public void testHandleCreateProfile_Metric_Success() {
        // 1. Arrange: Configure the stubs to return test data
        UserSelectionViewStub userSelectionView = mainView.getUserSelectionView();
        userSelectionView.setNewUserName("NewUser");
        userSelectionView.setAge("25");
        userSelectionView.setUserHeight("170.0");
        userSelectionView.setWeight("70.0");
        userSelectionView.setTargetWeight("68.0");
        userSelectionView.setSex(Sex.FEMALE);
        userSelectionView.setActivityLevel(ActivityLevel.LIGHT);
        userSelectionView.setImperial(false); // Metric selected

        // 2. Act: Simulate the user clicking the "Create Profile" button
        userSelectionView.clickCreateProfileButton();

        // 3. Assert: Check the state of our stubs to verify the outcome
        assertNotNull("CSVHandler should have saved a user", csvHandler.savedUser);
        assertEquals("NewUser", csvHandler.savedUser.getName());
        assertEquals(170, csvHandler.savedUser.getHeightCm());
        assertEquals(70.0, csvHandler.savedUser.getWeightKg(), 0.01);

        assertTrue("showDashboard should have been called on the main view", mainView.showDashboardCalled);
        assertTrue("A user should have been added to the user list", userSelectionView.addUserToListCalled);
        assertTrue("Creation fields should have been cleared", userSelectionView.clearCreationFieldsCalled);
    }

    /**
     * Tests the successful loading of an existing user profile.
     */
    @Test
    public void testHandleLoadProfile_Success() {
        // Arrange
        csvHandler.profiles.add(testUser);
        mainView.getUserSelectionView().setSelectedUser("TestUser");

        // Act: Simulate the user clicking the "Load Profile" button
        mainView.getUserSelectionView().clickLoadProfileButton();

        // Assert
        assertTrue("showDashboard should have been called", mainView.showDashboardCalled);
        assertTrue("setProfileInfo should have been called on the dashboard", mainView.getDashboardView().setProfileInfoCalled);
        assertEquals("The dashboard should be populated with the correct user's name", "TestUser", mainView.getDashboardView().profileName);
    }

    /**
     * Tests that an error message is shown when trying to load a profile
     * without making a selection.
     */
    @Test
    public void testHandleLoadProfile_NoSelection_ShowsError() {
        // Arrange
        mainView.getUserSelectionView().setSelectedUser(null);

        // Act: Simulate the click
        mainView.getUserSelectionView().clickLoadProfileButton();

        // Assert
        assertTrue("showError should have been called", mainView.showErrorCalled);
        assertEquals("Error message was not the one expected", "Please select a profile to load.", mainView.errorMessage);
        assertFalse("showDashboard should not have been called", mainView.showDashboardCalled);
    }

    /**
     * Tests the "Save Changes" functionality.
     */
    @Test
    public void testHandleSaveChanges() {
        // Arrange: First, simulate loading a user to set the internal state
        csvHandler.profiles.add(testUser);
        mainView.getUserSelectionView().setSelectedUser("TestUser");
        mainView.getUserSelectionView().clickLoadProfileButton();

        // Act: Now, simulate clicking the "Save Changes" button
        mainView.getDashboardView().clickSaveChangesButton();

        // Assert
        assertTrue("updateUserProfileToCsv should have been called", csvHandler.updateCalled);
        assertEquals("The correct user profile should have been saved", testUser, csvHandler.savedUser);
        assertTrue("A success message should have been shown", mainView.showMessageCalled);
        assertEquals("Success message was not the one expected", "Profile saved successfully!", mainView.infoMessage);
    }

    /**
     * Tests navigation to the "All Logs" view.
     */
    @Test
    public void testHandleViewAllLogs() {
        // Arrange: Load a user first
        csvHandler.profiles.add(testUser);
        mainView.getUserSelectionView().setSelectedUser("TestUser");
        mainView.getUserSelectionView().clickLoadProfileButton();

        // Act: Simulate the click
        mainView.getDashboardView().clickAllLogsButton();

        // Assert
        assertTrue("populateLogDates should have been called", mainView.getAllLogsView().populateLogDatesCalled);
        assertTrue("showAllLogs should have been called", mainView.showAllLogsCalled);
    }

    /**
     * A stub for CSVHandler that uses an in-memory list instead of files.
     */
    private static class CSVHandlerStub extends CSVHandler {
        List<UserProfile> profiles = new ArrayList<>();
        UserProfile savedUser = null;
        boolean updateCalled = false;

        @Override
        public void saveUserProfileToCsv(UserProfile user) {
            this.savedUser = user;
            profiles.removeIf(p -> p.getName().equals(user.getName()));
            profiles.add(user);
        }
        
        @Override
        public void updateUserProfileToCsv(UserProfile user) {
            this.savedUser = user;
            this.updateCalled = true;
        }

        @Override
        public void loadUserProfilesFromCsvs() { /* Do nothing */ }

        @Override
        public List<UserProfile> getUserProfiles() {
            return profiles;
        }
    }

    /**
     * A stub for the MainView to track method calls and hold sub-view stubs.
     */
    private static class MainViewStub extends MainView {
        boolean showDashboardCalled, showUserSelectionCalled, showAllLogsCalled, showErrorCalled, showMessageCalled;
        String errorMessage, infoMessage;

        private final UserSelectionViewStub userSelectionViewStub = new UserSelectionViewStub();
        private final DashboardViewStub dashboardViewStub = new DashboardViewStub();
        private final AllLogsViewStub allLogsViewStub = new AllLogsViewStub();

        @Override public UserSelectionViewStub getUserSelectionView() { return userSelectionViewStub; }
        @Override public DashboardViewStub getDashboardView() { return dashboardViewStub; }
        @Override public AllLogsViewStub getAllLogsView() { return allLogsViewStub; }

        @Override public void showDashboard() { this.showDashboardCalled = true; }
        @Override public void showUserSelection() { this.showUserSelectionCalled = true; }
        @Override public void showAllLogs() { this.showAllLogsCalled = true; }
        @Override public void showError(String message) { this.showErrorCalled = true; this.errorMessage = message; }
        @Override public void showMessage(String message) { this.showMessageCalled = true; this.infoMessage = message; }
    }

    /**
     * A stub for UserSelectionView that captures listeners and simulates clicks.
     */
    private static class UserSelectionViewStub extends UserSelectionView {
        String newUserName, age, userHeight, weight, targetWeight, selectedUser;
        Sex sex;
        ActivityLevel activityLevel;
        boolean imperial, addUserToListCalled, clearCreationFieldsCalled;
        
        private ActionListener createProfileListener, loadProfileListener;

        @Override public void addCreateProfileListener(ActionListener listener) { this.createProfileListener = listener; }
        @Override public void addLoadProfileListener(ActionListener listener) { this.loadProfileListener = listener; }
        
        public void clickCreateProfileButton() { createProfileListener.actionPerformed(new ActionEvent(this, 1, "")); }
        public void clickLoadProfileButton() { loadProfileListener.actionPerformed(new ActionEvent(this, 1, "")); }

        public void setNewUserName(String n) { this.newUserName = n; }
        public void setAge(String a) { this.age = a; }
        public void setUserHeight(String h) { this.userHeight = h; }
        public void setWeight(String w) { this.weight = w; }
        public void setTargetWeight(String tw) { this.targetWeight = tw; }
        public void setSex(Sex s) { this.sex = s; }
        public void setActivityLevel(ActivityLevel al) { this.activityLevel = al; }
        public void setImperial(boolean i) { this.imperial = i; }
        public void setSelectedUser(String u) { this.selectedUser = u; }

        @Override public String getNewUserName() { return newUserName; }
        @Override public int getAge() { return Integer.parseInt(age); }
        @Override public double getUserHeight() { return Double.parseDouble(userHeight); }
        @Override public double getWeight() { return Double.parseDouble(weight); }
        @Override public double getTargetWeight() { return Double.parseDouble(targetWeight); }
        @Override public Sex getSex() { return sex; }
        @Override public ActivityLevel getActivityLevel() { return activityLevel; }
        @Override public boolean isImperial() { return imperial; }
        @Override public String getSelectedUser() { return selectedUser; }

        @Override public void addUserToList(UserProfile profile) { this.addUserToListCalled = true; }
        @Override public void clearCreationFields() { this.clearCreationFieldsCalled = true; }
    }

    /**
     * A stub for DashboardView that captures listeners and simulates clicks.
     */
    private static class DashboardViewStub extends DashboardView {
        boolean setProfileInfoCalled;
        String profileName;
        private ActionListener saveChangesListener, viewAllLogsListener;

        @Override public void addSaveChangesListener(ActionListener listener) { this.saveChangesListener = listener; }
        @Override public void addViewAllLogsListener(ActionListener listener) { this.viewAllLogsListener = listener; }

        public void clickSaveChangesButton() { saveChangesListener.actionPerformed(new ActionEvent(this, 1, "")); }
        public void clickAllLogsButton() { viewAllLogsListener.actionPerformed(new ActionEvent(this, 1, "")); }

        @Override
        public void setProfileInfo(String name, String age, String weight, String height, String targetWeight) {
            this.setProfileInfoCalled = true;
            this.profileName = name;
        }
    }

    /**
     * A stub for AllLogsView to track calls.
     */
    private static class AllLogsViewStub extends AllLogsView {
        boolean populateLogDatesCalled;
        @Override public void populateLogDates(List<DailyLog> logs) { this.populateLogDatesCalled = true; }
    }
}

