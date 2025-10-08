package Library.ui;

import Library.model.Book;
import Library.service.LibraryService;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class BookView {
    // Use the shared LibraryService instance
    private LibraryService service = SharedService.getLibraryServiceInstance();
    private VBox view;
    private TextArea displayArea;

    public BookView() {
        view = new VBox(10);
        view.setPadding(new Insets(10));
        view.setStyle("-fx-background-color: #E0FFFF;"); // Light cyan background

        Label title = new Label("Admin: Manage Books");
        title.setFont(javafx.scene.text.Font.font("Arial", javafx.scene.text.FontWeight.BOLD, 18));


        TextField idField = new TextField();
        idField.setPromptText("Book ID (e.g., B101)");
        TextField titleField = new TextField();
        titleField.setPromptText("Title");
        TextField authorField = new TextField();
        authorField.setPromptText("Author");
        TextField genreField = new TextField();
        genreField.setPromptText("Genre");

        Button addButton = new Button("Add Book");
        addButton.setOnAction(e -> {
            if (idField.getText().isEmpty() || titleField.getText().isEmpty() ||
                authorField.getText().isEmpty() || genreField.getText().isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Input Error", "All fields are required to add a book!");
                return;
            }
            if (service.getBookById(idField.getText()) != null) {
                 showAlert(Alert.AlertType.ERROR, "Add Error", "Book with ID " + idField.getText() + " already exists!");
                return;
            }

            Book book = new Book(idField.getText(), titleField.getText(), authorField.getText(), genreField.getText(), null);
            service.addBook(book);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Book Added: " + book.getTitle());
            clearFields(idField, titleField, authorField, genreField);
            refreshDisplay();
        });

        displayArea = new TextArea();
        displayArea.setEditable(false);
        displayArea.setPrefHeight(300);

        refreshDisplay(); // Initial load

        view.getChildren().addAll(title, idField, titleField, authorField, genreField, addButton, displayArea);
    }

    private void refreshDisplay() {
        displayArea.clear();
        if (service.getAllBooks().isEmpty()) {
            displayArea.setText("No books in the library.");
        } else {
            for (Book b : service.getAllBooks()) {
                displayArea.appendText(b.toString() + "\n");
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