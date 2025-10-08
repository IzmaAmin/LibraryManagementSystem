package Library.ui;

import Library.model.Member; // Uses the updated Member model
import Library.service.LibraryService;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class MemberView {
    // Use the shared LibraryService instance
    private LibraryService service = SharedService.getLibraryServiceInstance();
    private VBox view;
    private TextArea displayArea;

    public MemberView() {
        view = new VBox(10);
        view.setPadding(new Insets(10));
        view.setStyle("-fx-background-color: #FFF0F5;"); // Lavender blush background

        Label title = new Label("Admin: Manage Members");
        title.setFont(javafx.scene.text.Font.font("Arial", javafx.scene.text.FontWeight.BOLD, 18));

        TextField idField = new TextField();
        idField.setPromptText("Member ID (e.g., M101)");
        TextField firstNameField = new TextField();
        firstNameField.setPromptText("First Name");
        TextField lastNameField = new TextField();
        lastNameField.setPromptText("Last Name");
        TextField emailField = new TextField();
        emailField.setPromptText("Email (user@example.com)");
        PasswordField passwordField = new PasswordField(); // For admin to set initial password
        passwordField.setPromptText("Initial Password");


        Button registerBtn = new Button("Register Member (Admin)");
        registerBtn.setOnAction(e -> {
            String memberId = idField.getText().trim();
            String firstName = firstNameField.getText().trim();
            String lastName = lastNameField.getText().trim();
            String email = emailField.getText().trim();
            String password = passwordField.getText();

            if (memberId.isEmpty() || firstName.isEmpty() || email.isEmpty() || password.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Input Error", "Member ID, First Name, Email, and Password are required!");
                return;
            }
             if (service.getMemberById(memberId) != null) {
                showAlert(Alert.AlertType.ERROR, "Registration Error", "Member with ID " + memberId + " already exists!");
                return;
            }
            if (service.getMemberByEmail(email) != null) {
                showAlert(Alert.AlertType.ERROR, "Registration Error", "Member with email " + email + " already exists!");
                return;
            }


            // Use the constructor: Member(String memberId, String firstName, String lastName, String email, String password)
            Member member = new Member(memberId, firstName, lastName, email, password);
            service.registerMemberForSeeding(member); // Use a distinct method for admin registration
            showAlert(Alert.AlertType.INFORMATION, "Success", "Member Registered: " + member.getName());
            clearFields(idField, firstNameField, lastNameField, emailField, passwordField);
            refreshDisplay();
        });

        displayArea = new TextArea();
        displayArea.setEditable(false);
        displayArea.setPrefHeight(300);
        refreshDisplay();

        view.getChildren().addAll(title, idField, firstNameField, lastNameField, emailField, passwordField, registerBtn, displayArea);
    }

    private void refreshDisplay() {
        displayArea.clear();
        if (service.getAllMembers().isEmpty()) {
            displayArea.setText("No members registered.");
        } else {
            for (Member m : service.getAllMembers()) {
                displayArea.appendText(m.getMemberId() + " - " + m.getName() + " (" + m.getEmail() + ")\n");
            }
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void clearFields(TextField... fields) {
        for (TextField f : fields) {
            f.clear();
        }
    }

    @SuppressWarnings("exports")
	public VBox getView() {
        refreshDisplay(); // Ensure it's up-to-date when shown
        return view;
    }
}