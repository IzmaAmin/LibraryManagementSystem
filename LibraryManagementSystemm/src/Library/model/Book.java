package Library.model;

public class Book {
    // CRITICAL: This path now assumes your placeholder in src/images_resources/ is "placeholder.png"
	private static final String DEFAULT_PLACEHOLDER_IMAGE_PATH = "/images_resources/actual_placeholder.jpg";

    private String id;
    private String title;
    private String author;
    private String genre;
    private boolean isBorrowed;
    private String coverImagePath;

    public Book(String id, String title, String author, String genre, String coverImagePath) {
        if (id == null || id.trim().isEmpty()) {
            System.err.println("Book Constructor Warning: ID is null or empty for title: " + title);
            this.id = "UNKNOWN_ID";
        } else {
            this.id = id;
        }

        if (title == null || title.trim().isEmpty()) {
            System.err.println("Book Constructor Warning: Title is null or empty for ID: " + id);
            this.title = "Untitled Book";
        } else {
            this.title = title;
        }
        
        this.author = author;
        this.genre = genre;
        this.isBorrowed = false;

        if (coverImagePath == null || coverImagePath.trim().isEmpty()) {
            this.coverImagePath = DEFAULT_PLACEHOLDER_IMAGE_PATH;
        } else {
            this.coverImagePath = coverImagePath;
        }
    }

    public Book(String id, String title, String author, String genre) {
        this(id, title, author, genre, DEFAULT_PLACEHOLDER_IMAGE_PATH);
    }

    // Getters
    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getGenre() { return genre; }
    public boolean isBorrowed() { return isBorrowed; }
    public String getCoverImagePath() { return coverImagePath; }

    // Actions
    public void borrow() { isBorrowed = true; }
    public void returnBook() { isBorrowed = false; }

    @Override
    public String toString() {
        return (title != null ? title : "N/A Title") + 
               " by " + (author != null ? author : "N/A Author") + 
               " [" + (genre != null ? genre : "N/A Genre") + "] - " + 
               (isBorrowed ? "Borrowed" : "Available");
    }
}