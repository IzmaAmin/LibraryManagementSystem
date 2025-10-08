package Library.ui;

import Library.Main;
import Library.service.LibraryService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class RegistrationView {
    private final LibraryService libraryService = SharedService.getLibraryServiceInstance();
    private final Main mainApp;

    public RegistrationView(Main mainApp) {
        this.mainApp = mainApp;
    }

    @SuppressWarnings("exports")
	public GridPane getView() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(40, 40, 40, 40));
        grid.setStyle("-fx-background-color: #E6E6FA;");

        Text scenetitle = new Text("Register New Member");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);

        Label firstNameLabel = new Label("First Name:");
        grid.add(firstNameLabel, 0, 1);
        TextField firstNameField = new TextField();
        firstNameField.setPromptText("Enter first name");
        grid.add(firstNameField, 1, 1);

        Label lastNameLabel = new Label("Last Name:");
        grid.add(lastNameLabel, 0, 2);
        TextField lastNameField = new TextField();
        lastNameField.setPromptText("Enter last name");
        grid.add(lastNameField, 1, 2);

        Label emailLabel = new Label("Email:");
        grid.add(emailLabel, 0, 3);
        TextField emailField = new TextField();
        emailField.setPromptText("Enter your email (e.g., user@gmail.com)");
        grid.add(emailField, 1, 3);

        Label passwordLabel = new Label("Password:");
        grid.add(passwordLabel, 0, 4);
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter password");
        grid.add(passwordField, 1, 4);

        Label confirmPasswordLabel = new Label("Confirm Password:");
        grid.add(confirmPasswordLabel, 0, 5);
        PasswordField confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Confirm password");
        grid.add(confirmPasswordField, 1, 5);

        Button registerButton = new Button("Register");
        Hyperlink loginLink = new Hyperlink("Already a member? Login here");
        loginLink.setOnAction(e -> mainApp.showLoginScreen());
        
        VBox buttonBox = new VBox(10, registerButton, loginLink);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        grid.add(buttonBox, 1, 6);


        registerButton.setOnAction(e -> {
            String firstName = firstNameField.getText().trim();
            String lastName = lastNameField.getText().trim();
            String email = emailField.getText().trim();
            String password = passwordField.getText();
            String confirmPassword = confirmPasswordField.getText();

            if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Registration Failed", "All fields are required.");
                return;
            }
            if (!password.equals(confirmPassword)) {
                showAlert(Alert.AlertType.ERROR, "Registration Failed", "Passwords do not match.");
                return;
            }
            // Basic email validation
            if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                showAlert(Alert.AlertType.ERROR, "Registration Failed", "Please enter a valid email address.");
                return;
            }

            String result = libraryService.registerNewMember(firstName, lastName, email, password);
            if (result.startsWith("Member registered successfully")) {
                showAlert(Alert.AlertType.INFORMATION, "Registration Successful", result + "\nPlease login.");
                mainApp.showLoginScreen();
            } else {
                showAlert(Alert.AlertType.ERROR, "Registration Failed", result);
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