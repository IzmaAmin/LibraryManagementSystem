package Library.service;

import java.util.ArrayList;
import java.util.List;
import Library.model.Book;
import Library.model.Member;

public class LibraryService {
    private List<Book> books = new ArrayList<>();
    private List<Member> members = new ArrayList<>();

    public void addBook(Book b) {
        if (b == null) {
            System.err.println("LibraryService.addBook: Attempted to add a null Book object. Skipping.");
            return;
        }
        if (b.getId() == null || b.getTitle() == null) {
            System.err.println("LibraryService.addBook: Attempted to add Book with null ID or Title. ID: " + b.getId() + ", Title: " + b.getTitle() + ". Skipping.");
            return;
        }
        if (getBookById(b.getId()) == null) {
            books.add(b);
            System.out.println("LibraryService.addBook: Successfully added Book -> ID: " + b.getId() + ", Title: '" + b.getTitle() + "'");
        } else {
            System.out.println("LibraryService.addBook: Book with ID " + b.getId() + " already exists. Not adding '" + b.getTitle() + "'.");
        }
    }

    public List<Book> getAllBooks() {
        System.out.println("LibraryService.getAllBooks: Returning " + (books != null ? books.size() : "null list") + " books.");
        if (books == null) return new ArrayList<>(); // Should not happen with current init
        return new ArrayList<>(books); // Return a copy
    }

    // ... (rest of LibraryService methods: registerMemberForSeeding, etc. can also have prints if needed) ...

    public void registerMemberForSeeding(Member m) { // For SampleData
        if (m == null) {
            System.err.println("LibraryService.registerMemberForSeeding: Attempted to add a null Member object. Skipping.");
            return;
        }
        if (getMemberById(m.getMemberId()) == null && getMemberByEmail(m.getEmail()) == null) {
            members.add(m);
             System.out.println("LibraryService.registerMemberForSeeding: Added Member ID: " + m.getMemberId());
        } else {
            System.out.println("SeedData: Member with ID " + m.getMemberId() + " or email " + m.getEmail() + " already exists. Skipping.");
        }
    }

    public String registerNewMember(String firstName, String lastName, String email, String password) { // For User RegistrationView
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            return "All fields are required.";
        }
        if (getMemberByEmail(email) != null) {
            return "Email already registered.";
        }
        String newMemberId = "M" + String.format("%03d", members.size() + 1);
        // Ensure newMemberId is unique, simple increment might not be robust if members are removed, but okay for this scale
        int attempts = 0;
        while (getMemberById(newMemberId) != null && attempts < 100) { // Safety break
            newMemberId = "M" + String.format("%03d", members.size() + 1 + attempts + 1); // Try next available
            attempts++;
        }
        if (getMemberById(newMemberId) != null) {
            return "Failed to generate a unique Member ID after several attempts."; // Should be very rare
        }

        Member newMember = new Member(newMemberId, firstName, lastName, email, password);
        members.add(newMember);
        System.out.println("LibraryService.registerNewMember: Registered Member ID: " + newMemberId);
        return "Member registered successfully with ID: " + newMemberId;
    }
     public Member loginMember(String identifier, String password) {
        if (identifier == null || identifier.trim().isEmpty() || password == null || password.isEmpty()) {
            System.out.println("LibraryService.loginMember: Identifier or password empty.");
            return null;
        }
        System.out.println("LibraryService.loginMember: Attempting login for identifier: " + identifier);
        for (Member member : members) {
            if ((member.getMemberId().equalsIgnoreCase(identifier.trim()) || member.getEmail().equalsIgnoreCase(identifier.trim()))
                    && member.getPassword().equals(password)) {
                System.out.println("LibraryService.loginMember: Login successful for " + member.getMemberId());
                return member;
            }
        }
        System.out.println("LibraryService.loginMember: Login failed for " + identifier);
        return null;
    }

    public List<Member> getAllMembers() { return new ArrayList<>(members); }

    public Book getBookById(String id) {
        if (id == null) return null;
        for (Book b : books) if (b.getId().equalsIgnoreCase(id)) return b;
        return null;
    }

    public Member getMemberById(String id) {
        if (id == null) return null;
        for (Member m : members) if (m.getMemberId().equalsIgnoreCase(id)) return m;
        return null;
    }

    public Member getMemberByEmail(String email) {
        if (email == null) return null;
        for (Member m : members) if (m.getEmail().equalsIgnoreCase(email)) return m;
        return null;
    }
    public String borrowBook(String memberId, String bookId) {
        Member member = getMemberById(memberId);
        Book book = getBookById(bookId);
        if (member == null) return "Member not found.";
        if (book == null) return "Book not found.";
        if (member.getBorrowedBooks().contains(book)) return "You have already borrowed this book.";
        if (book.isBorrowed()) return "Book is already borrowed by another member.";
        if (member.getBorrowedBooks().size() >= 5) return "Borrowing limit reached (5 books).";
        if (member.borrowBook(book)) return "Book '" + book.getTitle() + "' borrowed successfully!";
        return "Failed to borrow book.";
    }

    public String returnBook(String memberId, String bookId) {
        Member member = getMemberById(memberId);
        Book book = getBookById(bookId);
        if (member == null) return "Member not found.";
        if (book == null) return "Book not found.";
        if (!member.getBorrowedBooks().contains(book)) return "This book was not borrowed by you.";
        if (member.returnBook(book)) return "Book '" + book.getTitle() + "' returned successfully!";
        return "Failed to return book.";
    }
    public String getBorrowingHistory() {
        StringBuilder sb = new StringBuilder();
        boolean foundAnyBorrowed = false;
        for (Member m : members) {
            ArrayList<Book> borrowed = m.getBorrowedBooks(); 
            if (!borrowed.isEmpty()) {
                if (!foundAnyBorrowed) {
                    sb.append("Current Borrowing Status:\n");
                    sb.append("-------------------------\n");
                    foundAnyBorrowed = true;
                }
                sb.append("Member: ").append(m.getName()).append(" (ID: ").append(m.getMemberId()).append(")\n");
                for (Book b : borrowed) {
                    sb.append("  - ").append(b.getTitle()).append(" (Book ID: ").append(b.getId()).append(")\n");
                }
                sb.append("\n"); 
            }
        }

        if (!foundAnyBorrowed) {
            sb.append("No books are currently borrowed by any member.");
        }
        return sb.toString();
    }
}