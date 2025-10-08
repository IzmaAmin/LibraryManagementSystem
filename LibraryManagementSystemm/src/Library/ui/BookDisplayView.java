package Library.ui;

import Library.Main;
import Library.model.Book;
import Library.model.Member;
import Library.service.LibraryService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.List; // Ensure this is imported

public class BookDisplayView {
    private final LibraryService libraryService = SharedService.getLibraryServiceInstance();
    private final Main mainApp;
    private FlowPane booksFlowPane; // Initialized in getView()
    private BorderPane mainLayout;  // Initialized in getView()
    private Label welcomeLabel;     // Initialized in getView()

    public BookDisplayView(Main mainApp) {
        this.mainApp = mainApp;
        System.out.println("BookDisplayView: Constructor called."); // DEBUG
    }

    @SuppressWarnings("exports")
    public BorderPane getView() {
        // --- DEBUGGING STATEMENT ADDED HERE ---
        System.out.println("BookDisplayView.getView: Method started - creating UI components.");

        mainLayout = new BorderPane();
        mainLayout.setPadding(new Insets(15));
        mainLayout.setStyle("-fx-background-color: #FAF0E6;");

        Label titleLabel = new Label("Online Book Bazar");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));

        welcomeLabel = new Label(); // Initialized here
        // updateWelcomeMessage(); // Called by refreshBookDisplay

        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(e -> {
            SharedService.logoutMember();
            mainApp.showLoginScreen();
        });

        HBox rightHeaderBox = new HBox(15, welcomeLabel, logoutButton);
        rightHeaderBox.setAlignment(Pos.CENTER_RIGHT);

        HBox header = new HBox(titleLabel, rightHeaderBox);
        HBox.setHgrow(rightHeaderBox, Priority.ALWAYS);
        header.setPadding(new Insets(0, 0, 15, 0));
        header.setAlignment(Pos.CENTER_LEFT);

        mainLayout.setTop(header);

        booksFlowPane = new FlowPane(); // booksFlowPane is initialized HERE
        booksFlowPane.setPadding(new Insets(10));
        booksFlowPane.setVgap(20);
        booksFlowPane.setHgap(20);
        booksFlowPane.setAlignment(Pos.TOP_LEFT);

        ScrollPane scrollPane = new ScrollPane(booksFlowPane);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        mainLayout.setCenter(scrollPane);

        // --- DEBUGGING STATEMENT ADDED HERE ---
        System.out.println("BookDisplayView.getView: UI components created. booksFlowPane is " +
                           (booksFlowPane == null ? "NULL" : "NOT NULL") + ". Method finished.");
        return mainLayout;
    }

    private void updateWelcomeMessage() {
        // System.out.println("BookDisplayView.updateWelcomeMessage: Called."); // DEBUG if needed
        Member loggedInUser = SharedService.getLoggedInMember();
        if (loggedInUser != null && welcomeLabel != null) {
            welcomeLabel.setText("Welcome, " + loggedInUser.getFirstName() + "!");
        } else if (welcomeLabel != null) {
            welcomeLabel.setText("Welcome, Guest!");
        } else {
            // System.err.println("BookDisplayView.updateWelcomeMessage: welcomeLabel is null!"); // DEBUG if needed
        }
    }

    public void refreshBookDisplay() {
        System.out.println("BookDisplayView.refreshBookDisplay: Method started."); // DEBUG
        updateWelcomeMessage();

        if (booksFlowPane == null) {
            System.err.println("BookDisplayView.refreshBookDisplay: CRITICAL - booksFlowPane is NULL. Cannot refresh display. This should not happen if getView() was called first.");
            return;
        }
        booksFlowPane.getChildren().clear();
        System.out.println("BookDisplayView.refreshBookDisplay: booksFlowPane cleared."); // DEBUG

        if (!SharedService.isLoggedIn()) {
            Label loginPrompt = new Label("Please log in to view books.");
            loginPrompt.setFont(Font.font("Arial", 18));
            booksFlowPane.getChildren().add(loginPrompt);
            System.out.println("BookDisplayView.refreshBookDisplay: User not logged in. Showing login prompt.");
            return;
        }

        List<Book> allBooks = libraryService.getAllBooks(); // This calls LibraryService.getAllBooks()
        System.out.println("BookDisplayView.refreshBookDisplay: Number of books from service: " + (allBooks != null ? allBooks.size() : "null list"));


        if (allBooks == null || allBooks.isEmpty()) {
            Label noBooksLabel = new Label("No books available in the library currently.");
            noBooksLabel.setFont(Font.font("Arial", 16));
            booksFlowPane.getChildren().add(noBooksLabel);
            System.out.println("BookDisplayView.refreshBookDisplay: No books found or list is null. Added 'No books' label.");
        } else {
            System.out.println("BookDisplayView.refreshBookDisplay: Processing " + allBooks.size() + " books to create cards.");
            for (Book book : allBooks) {
                if (book == null) {
                    System.err.println("BookDisplayView.refreshBookDisplay: Encountered a NULL Book object in the list from LibraryService. Skipping card.");
                    continue;
                }

                // System.out.println("BookDisplayView.refreshBookDisplay: Preparing card for Book -> ID: " + book.getId() +
                //                    ", Title: '" + book.getTitle() +
                //                    "', Author: '" + book.getAuthor() +
                //                    "', CoverPath: '" + book.getCoverImagePath() + "'");

                if (book.getTitle() == null || book.getAuthor() == null) {
                    System.err.println("BookDisplayView.refreshBookDisplay: WARNING - Book with ID " + book.getId() +
                                       " has null Title or Author. Title: " + book.getTitle() +
                                       ", Author: " + book.getAuthor() + ". Card content will be affected.");
                }

                BookCard card = new BookCard(book, this); // This calls BookCard constructor
                booksFlowPane.getChildren().add(card);
            }
            System.out.println("BookDisplayView.refreshBookDisplay: Finished creating and adding " + booksFlowPane.getChildren().size() + " book cards.");
        }
        System.out.println("BookDisplayView.refreshBookDisplay: Method finished."); // DEBUG
    }
}