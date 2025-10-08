package Library.ui;

import Library.model.Book;
import Library.model.Member;
import Library.service.LibraryService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

import java.io.InputStream;
import java.io.IOException;

public class BookCard extends VBox {
    private final Book book;
    private final LibraryService libraryService = SharedService.getLibraryServiceInstance();
    private Member currentUser;
    private final Label statusLabel;
    private final Button actionButton;
    private final BookDisplayView parentView;

    public BookCard(Book book, BookDisplayView parentView) {
        if (book == null) {
            System.err.println("BookCard Constructor: CRITICAL - Received NULL Book object! Creating a dummy card.");
            this.book = new Book("DUMMY_ID", "Error: Book Data Missing", "N/A", "N/A", "");
        } else {
            this.book = book;
        }
        this.parentView = parentView;
        this.currentUser = SharedService.getLoggedInMember();

        System.out.println("BookCard [" + this.book.getId() + "]: Constructor START. Title='" + this.book.getTitle() + "', Author='" + this.book.getAuthor() + "', CurrentUser: " + (currentUser != null ? currentUser.getMemberId() : "null"));

        this.setPrefWidth(180);
        this.setMinWidth(160);
        this.setSpacing(8);
        this.setPadding(new Insets(10));
        this.setAlignment(Pos.TOP_CENTER);
        // --- AGGRESSIVE STYLING FOR THE CARD ITSELF ---
        this.setStyle("-fx-border-color: LIMEGREEN; -fx-border-width: 3px; -fx-background-color: YELLOW; " +
                      "-fx-min-width: 150px; -fx-pref-width: 180px; -fx-min-height: 280px;");
        System.out.println("BookCard [" + this.book.getId() + "]: Applied aggressive VBox styling.");

        ImageView coverImageView = createCoverImageView();
        // --- AGGRESSIVE STYLING FOR IMAGEVIEW ---
        coverImageView.setStyle("-fx-border-color: BLUE; -fx-border-width: 2px;");
        System.out.println("BookCard [" + this.book.getId() + "]: coverImageView created. Image is " + (coverImageView.getImage() != null ? "LOADED" : "NULL or FAILED"));


        Label titleLabel = new Label(this.book.getTitle() != null ? this.book.getTitle() : "TITLE MISSING");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        titleLabel.setWrapText(true);
        titleLabel.setTextAlignment(TextAlignment.CENTER);
        titleLabel.setMinHeight(40);
        // --- AGGRESSIVE STYLING FOR TITLE LABEL ---
        titleLabel.setStyle("-fx-border-color: RED; -fx-border-width: 1px; -fx-text-fill: BLACK; -fx-background-color: LIGHTPINK; -fx-padding: 2px;");
        System.out.println("BookCard [" + this.book.getId() + "]: titleLabel created. Text='" + titleLabel.getText() + "'");


        Label authorLabel = new Label("By: " + (this.book.getAuthor() != null ? this.book.getAuthor() : "AUTHOR MISSING"));
        authorLabel.setFont(Font.font("Arial", 12));
        authorLabel.setWrapText(true);
        authorLabel.setTextAlignment(TextAlignment.CENTER);
        // --- AGGRESSIVE STYLING FOR AUTHOR LABEL ---
        authorLabel.setStyle("-fx-border-color: ORANGE; -fx-border-width: 1px; -fx-text-fill: BLACK; -fx-background-color: LIGHTCYAN; -fx-padding: 2px;");
        System.out.println("BookCard [" + this.book.getId() + "]: authorLabel created. Text='" + authorLabel.getText() + "'");


        statusLabel = new Label();
        statusLabel.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 12));
        // --- AGGRESSIVE STYLING FOR STATUS LABEL ---
        statusLabel.setStyle("-fx-border-color: PURPLE; -fx-border-width: 1px; -fx-text-fill: BLACK; -fx-background-color: LIGHTGREEN; -fx-padding: 2px;");


        actionButton = new Button();
        actionButton.setPrefWidth(100);
        // --- AGGRESSIVE STYLING FOR ACTION BUTTON ---
        actionButton.setStyle("-fx-border-color: BROWN; -fx-border-width: 1px;");


        updateStatusAndButton();
        System.out.println("BookCard [" + this.book.getId() + "]: after updateStatusAndButton. Status='" + statusLabel.getText() + "', ButtonText='" + actionButton.getText() + "'");


        this.getChildren().addAll(coverImageView, titleLabel, authorLabel, statusLabel, actionButton);
        System.out.println("BookCard [" + this.book.getId() + "]: All children added to VBox. Total children in card: " + this.getChildren().size());
        System.out.println("BookCard [" + this.book.getId() + "]: Constructor END. Preferred VBox Width: " + this.getPrefWidth() + ", Height: " + this.getPrefHeight());
    }

    private ImageView createCoverImageView() {
        ImageView imageView = new ImageView();
        imageView.setFitWidth(100);
        imageView.setFitHeight(150);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);

        String specificImagePath = book.getCoverImagePath();
        // CRITICAL: Ensure this path matches your actual placeholder image file in src/images_resources/
     // Example: if your file is actual_placeholder.jpeg
        String placeholderImagePath = "/images_resources/actual_placeholder.jpg"; // << MAKE SURE THIS IS YOUR ACTUAL PLACEHOLDER

        boolean imageLoaded = false;

        if (specificImagePath != null && !specificImagePath.trim().isEmpty() && !specificImagePath.equals(placeholderImagePath)) {
            imageLoaded = loadImageToView(imageView, specificImagePath, "Specific image for '" + book.getTitle() + "'");
        }

        if (!imageLoaded) {
            imageLoaded = loadImageToView(imageView, placeholderImagePath, "Placeholder image");
        }

        if (!imageLoaded) {
            System.err.println("BookCard [" + (book != null ? book.getId() : "UNKNOWN_BOOK") + "]: FINAL FALLBACK: No image could be loaded. Setting grey rectangle.");
            setFallbackRectangle(imageView);
        }
        return imageView;
    }

    private boolean loadImageToView(ImageView imageView, String imagePath, String imageDescription) {
        if (imagePath == null || imagePath.trim().isEmpty()) {
            // System.err.println("BookCard: loadImageToView - imagePath is null or empty for " + imageDescription); // Can be noisy
            return false;
        }
        InputStream imageStream = null;
        // System.out.println("BookCard [" + (book != null ? book.getId() : "UNKNOWN_BOOK") + "]: loadImageToView - Attempting: " + imageDescription + " from path: " + imagePath);
        try {
            imageStream = getClass().getResourceAsStream(imagePath);
            if (imageStream != null) {
                Image img = new Image(imageStream);
                if (!img.isError()) {
                    imageView.setImage(img);
                    System.out.println("BookCard [" + (book != null ? book.getId() : "UNKNOWN_BOOK") + "]: INFO: Successfully loaded " + imageDescription + " from " + imagePath);
                    return true;
                } else {
                    System.err.println("BookCard [" + (book != null ? book.getId() : "UNKNOWN_BOOK") + "]: ERROR: Image found at " + imagePath + " for " + imageDescription + " but it's invalid or has an error.");
                    if (img.getException() != null) {
                        // img.getException().printStackTrace();
                    }
                }
            } else {
                System.err.println("BookCard [" + (book != null ? book.getId() : "UNKNOWN_BOOK") + "]: ERROR: " + imageDescription + " NOT FOUND at classpath path: " + imagePath);
            }
        } catch (Exception e) {
            System.err.println("BookCard [" + (book != null ? book.getId() : "UNKNOWN_BOOK") + "]: EXCEPTION while loading " + imageDescription + " (path: " + imagePath + "): " + e.getMessage());
            // e.printStackTrace();
        } finally {
            if (imageStream != null) {
                try { imageStream.close(); } catch (IOException e) { System.err.println("IOException closing stream: " + e.getMessage()); }
            }
        }
        return false;
    }

    private void setFallbackRectangle(ImageView imageView) {
        Rectangle placeholderRect = new Rectangle(100, 150, Color.LIGHTSLATEGRAY);
        Image placeholderImageFromRect = placeholderRect.snapshot(null, null);
        imageView.setImage(placeholderImageFromRect);
    }

    public void updateStatusAndButton() {
        // this.currentUser = SharedService.getLoggedInMember(); // Already set in constructor

        if (currentUser == null) {
            statusLabel.setText("Error: No User");
            statusLabel.setTextFill(Color.RED);
            actionButton.setText("N/A");
            actionButton.setDisable(true);
            return;
        }
        boolean isBorrowedByCurrentUser = currentUser.getBorrowedBooks().stream().anyMatch(b -> b.getId().equals(book.getId()));

        if (book.isBorrowed()) {
            if (isBorrowedByCurrentUser) {
                statusLabel.setText("You Borrowed");
                statusLabel.setTextFill(Color.DARKGREEN);
                actionButton.setText("Return");
                actionButton.setStyle("-fx-background-color: #FFB6C1; -fx-text-fill: black; -fx-border-color: BROWN; -fx-border-width: 1px;");
                actionButton.setOnAction(e -> handleReturn());
                actionButton.setDisable(false);
            } else {
                statusLabel.setText("Borrowed");
                statusLabel.setTextFill(Color.ORANGERED);
                actionButton.setText("Unavailable");
                actionButton.setStyle("-fx-border-color: BROWN; -fx-border-width: 1px;"); // Keep border for visibility
                actionButton.setDisable(true);
            }
        } else {
            statusLabel.setText("Available");
            statusLabel.setTextFill(Color.FORESTGREEN);
            actionButton.setText("Borrow");
            actionButton.setStyle("-fx-background-color: #ADD8E6; -fx-text-fill: black; -fx-border-color: BROWN; -fx-border-width: 1px;");
            actionButton.setOnAction(e -> handleBorrow());
            actionButton.setDisable(false);
        }
    }

    // ... (handleBorrow, handleReturn, showAlert methods remain the same) ...
    private void handleBorrow() {
        if (currentUser == null) return;
        String result = libraryService.borrowBook(currentUser.getMemberId(), book.getId());
        showAlert(result.endsWith("successfully!") ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR, "Borrow Attempt", result);
        parentView.refreshBookDisplay();
    }

    private void handleReturn() {
        if (currentUser == null) return;
        String result = libraryService.returnBook(currentUser.getMemberId(), book.getId());
        showAlert(result.endsWith("successfully!") ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR, "Return Attempt", result);
        parentView.refreshBookDisplay();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}