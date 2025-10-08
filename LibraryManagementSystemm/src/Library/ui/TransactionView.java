package Library.ui;

import Library.service.LibraryService;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class TransactionView {
    // Use the shared LibraryService instance
    private LibraryService service = SharedService.getLibraryServiceInstance();
    private VBox view;
    private TextArea displayArea; // To show borrowing history

    public TransactionView() {
        view = new VBox(10);
        view.setPadding(new Insets(10));
        view.setStyle("-fx-background-color: #F5F5DC;"); // Beige background

        Label title = new Label("Admin: Book Transactions & History");
        title.setFont(javafx.scene.text.Font.font("Arial", javafx.scene.text.FontWeight.BOLD, 18));

        TextField memberIdField = new TextField();
        memberIdField.setPromptText("Member ID for Transaction");
        TextField bookIdField = new TextField();
        bookIdField.setPromptText("Book ID for Transaction");

        Button borrowBtn = new Button("Borrow Book (Admin)");
        borrowBtn.setOnAction(e -> {
            String memberId = memberIdField.getText();
            String bookId = bookIdField.getText();
            if (memberId.isEmpty() || bookId.isEmpty()){
                showAlert(Alert.AlertType.WARNING, "Input Error", "Member ID and Book ID are required!");
                return;
            }
            String result = service.borrowBook(memberId, bookId);
            showAlert(Alert.AlertType.INFORMATION, "Borrow Attempt", result);
            refreshDisplay(); // Update history
        });

        Button returnBtn = new Button("Return Book (Admin)");
        returnBtn.setOnAction(e -> {
            String memberId = memberIdField.getText();
            String bookId = bookIdField.getText();
             if (memberId.isEmpty() || bookId.isEmpty()){
                showAlert(Alert.AlertType.WARNING, "Input Error", "Member ID and Book ID are required!");
                return;
            }
            String result = service.returnBook(memberId, bookId);
            showAlert(Alert.AlertType.INFORMATION, "Return Attempt", result);
            refreshDisplay(); // Update history
        });

        displayArea = new TextArea();
        displayArea.setEditable(false);
        displayArea.setPrefHeight(300);

        refreshDisplay(); // Initial load of history

        view.getChildren().addAll(title, memberIdField, bookIdField, borrowBtn, returnBtn, displayArea);
    }

    private void refreshDisplay() {
        displayArea.clear();
        // Display borrowing history from LibraryService
        displayArea.setText(service.getBorrowingHistory());
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @SuppressWarnings("exports")
	public VBox getView() {
        refreshDisplay(); // Ensure history is up-to-date when shown
        return view;
    }
}