package Library.ui;

import Library.Main;
import Library.model.Member; // Assuming this import is needed
import Library.service.LibraryService; // Assuming this import is needed
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class LoginView {
    private final LibraryService libraryService = SharedService.getLibraryServiceInstance();
    private final Main mainApp;

    public LoginView(Main mainApp) {
        this.mainApp = mainApp;
    }

    @SuppressWarnings("exports")
    public GridPane getView() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(40, 40, 40, 40));
        grid.setStyle("-fx-background-color: #F0F8FF;");

        Text scenetitle = new Text("Welcome to Online Book Bazar");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);

        Label userIdLabel = new Label("Member ID or Email:");
        grid.add(userIdLabel, 0, 1);
        TextField userIdField = new TextField();
        userIdField.setPromptText("Enter Member ID or Email");
        grid.add(userIdField, 1, 1);
        // userIdField.setText("M001"); // For quick testing

        Label pwLabel = new Label("Password:");
        grid.add(pwLabel, 0, 2);
        PasswordField pwBox = new PasswordField();
        pwBox.setPromptText("Enter password");
        grid.add(pwBox, 1, 2);
        // pwBox.setText("12345678"); // For quick testing

        Button loginButton = new Button("Sign in");
        loginButton.setDefaultButton(true);

        Hyperlink registerLink = new Hyperlink("Not a member? Register here");
        registerLink.setOnAction(e -> mainApp.showRegistrationScreen());

        VBox buttonBox = new VBox(10, loginButton, registerLink);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        grid.add(buttonBox, 1, 4);


        loginButton.setOnAction(e -> {
            String idOrEmail = userIdField.getText();
            String password = pwBox.getText();

            if (idOrEmail.isEmpty() || password.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Login Failed", "Member ID/Email and Password cannot be empty.");
                return;
            }

            Member member = libraryService.loginMember(idOrEmail, password);

            if (member != null) {
                SharedService.setLoggedInMember(member);
                showAlert(Alert.AlertType.INFORMATION, "Login Successful", "Welcome, " + member.getFirstName() + "!");
                // --- DEBUGGING STATEMENT ADDED HERE ---
                System.out.println("LoginView: Login successful for " + member.getMemberId() + ". Attempting to show BookDisplayScreen.");
                // --- END OF DEBUGGING STATEMENT ---
                mainApp.showBookDisplayScreen();
            } else {
                showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid Member ID/Email or Password.");
                SharedService.logoutMember();
            }
        });

        return grid;
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}