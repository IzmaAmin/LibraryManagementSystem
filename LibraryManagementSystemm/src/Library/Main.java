package Library;

import Library.ui.BookDisplayView;
import Library.ui.LoginView;
import Library.ui.RegistrationView;
import Library.ui.SharedService;
import Library.ui.BookView; // Admin view
import Library.ui.MemberView; // Admin view
import Library.ui.TransactionView; // Admin view
import Library.model.Member; // Added for getting user name

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Alert; // Added for error dialog

public class Main extends Application {
    private Stage primaryStage;

    private LoginView loginView;
    private RegistrationView registrationView;
    private BookDisplayView bookDisplayView;

    private BookView adminBookView;
    private MemberView adminMemberView;
    private TransactionView adminTransactionView;


    @Override
    @SuppressWarnings("exports")
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        System.out.println("Main.start: Initializing views..."); // DEBUG
        loginView = new LoginView(this);
        registrationView = new RegistrationView(this);
        bookDisplayView = new BookDisplayView(this);

        adminBookView = new BookView();
        adminMemberView = new MemberView();
        adminTransactionView = new TransactionView();
        System.out.println("Main.start: Views initialized."); // DEBUG


        this.primaryStage.setTitle("Online Book Bazar & Admin");
        System.out.println("Main.start: Showing login screen."); // DEBUG
        showLoginScreen();
        this.primaryStage.show();
        System.out.println("Main.start: Primary stage shown."); // DEBUG
    }

    public void showLoginScreen() {
        System.out.println("Main.showLoginScreen: Method started."); // DEBUG
        Scene scene = new Scene(loginView.getView(), 600, 550);
        this.primaryStage.setScene(scene);
        this.primaryStage.setTitle("Login - Online Book Bazar");
        System.out.println("Main.showLoginScreen: Login scene set."); // DEBUG
    }

    public void showRegistrationScreen() {
        System.out.println("Main.showRegistrationScreen: Method started."); // DEBUG
        Scene scene = new Scene(registrationView.getView(), 550, 500);
        this.primaryStage.setScene(scene);
        this.primaryStage.setTitle("Register - Online Book Bazar");
        System.out.println("Main.showRegistrationScreen: Registration scene set."); // DEBUG
    }

    public void showBookDisplayScreen() {
        // --- DEBUGGING AND ERROR HANDLING ADDED HERE ---
        System.out.println("Main.showBookDisplayScreen: Method started.");

        if (!SharedService.isLoggedIn()) {
            System.out.println("Main.showBookDisplayScreen: User not logged in! Showing login screen instead.");
            showLoginScreen();
            return;
        }
        System.out.println("Main.showBookDisplayScreen: User is logged in. Proceeding.");

        Scene scene = null;
        try {
            System.out.println("Main.showBookDisplayScreen: Attempting to get BookDisplayView.getView().");
            scene = new Scene(bookDisplayView.getView(), 950, 750); // This calls BookDisplayView.getView()
            System.out.println("Main.showBookDisplayScreen: Scene created successfully with BookDisplayView.");
        } catch (Exception e) {
            System.err.println("Main.showBookDisplayScreen: CRITICAL ERROR creating scene with BookDisplayView.getView(): " + e.getMessage());
            e.printStackTrace(); // PRINT THE FULL STACK TRACE
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Application Error");
            alert.setHeaderText("Failed to load book display screen.");
            alert.setContentText("An unexpected error occurred while preparing the view: " + e.getMessage());
            alert.showAndWait();
            return; // Stop further execution if scene creation fails
        }

        System.out.println("Main.showBookDisplayScreen: Attempting to call bookDisplayView.refreshBookDisplay().");
        try {
            bookDisplayView.refreshBookDisplay(); // This calls BookDisplayView.refreshBookDisplay()
            System.out.println("Main.showBookDisplayScreen: bookDisplayView.refreshBookDisplay() completed.");
        } catch (Exception e) {
            System.err.println("Main.showBookDisplayScreen: CRITICAL ERROR during bookDisplayView.refreshBookDisplay(): " + e.getMessage());
            e.printStackTrace(); // PRINT THE FULL STACK TRACE
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Application Error");
            alert.setHeaderText("Failed to load book content.");
            alert.setContentText("An unexpected error occurred while refreshing book data: " + e.getMessage());
            alert.showAndWait();
            // Continue to set the scene, but content might be missing/problematic
        }

        this.primaryStage.setScene(scene);
        Member loggedInMember = SharedService.getLoggedInMember(); // Fetch once
        String userName = loggedInMember != null ? loggedInMember.getFirstName() : "User";
        this.primaryStage.setTitle("Book Collection - Welcome, " + userName);
        System.out.println("Main.showBookDisplayScreen: Set scene and title. Method finished.");
        // --- END OF DEBUGGING AND ERROR HANDLING ---
    }

    // --- Methods to show admin views ---
    public void showAdminBookManagement() {
        System.out.println("Main.showAdminBookManagement: Method started."); // DEBUG
        Scene scene = new Scene(adminBookView.getView(), 700, 600);
        this.primaryStage.setScene(scene);
        this.primaryStage.setTitle("Admin: Manage Books");
        System.out.println("Main.showAdminBookManagement: Admin Book Management scene set."); // DEBUG
    }

    public void showAdminMemberManagement() {
        System.out.println("Main.showAdminMemberManagement: Method started."); // DEBUG
        Scene scene = new Scene(adminMemberView.getView(), 700, 600);
        this.primaryStage.setScene(scene);
        this.primaryStage.setTitle("Admin: Manage Members");
        System.out.println("Main.showAdminMemberManagement: Admin Member Management scene set."); // DEBUG
    }

    public void showAdminTransactionManagement() {
        System.out.println("Main.showAdminTransactionManagement: Method started."); // DEBUG
        Scene scene = new Scene(adminTransactionView.getView(), 700, 600);
        this.primaryStage.setScene(scene);
        this.primaryStage.setTitle("Admin: Manage Transactions & History");
        System.out.println("Main.showAdminTransactionManagement: Admin Transaction Management scene set."); // DEBUG
    }

    public static void main(String[] args) {
        launch(args);
    }}
