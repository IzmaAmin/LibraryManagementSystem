package Library.model;

import java.util.ArrayList;
import java.util.Objects;

public class Member extends Person {
    private String memberId;
    private String email;
    private String password; // In a real app, this should be hashed!
    private ArrayList<Book> borrowedBooks;

    // Primary constructor
    public Member(String memberId, String firstName, String lastName, String email, String password) {
        super(firstName, lastName);
        this.memberId = memberId;
        this.email = email;
        this.password = password;
        this.borrowedBooks = new ArrayList<>();
    }

    // Optional: A secondary constructor if you sometimes get full name as one string
    // public Member(String memberId, String fullName, String email, String password) {
    //    super(parseFirstName(fullName), parseLastName(fullName)); // You'd need parse methods
    //    this.memberId = memberId;
    //    this.email = email;
    //    this.password = password;
    //    this.borrowedBooks = new ArrayList<>();
    // }

    public String getMemberId() { return memberId; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }

    public boolean borrowBook(Book book) {
        if (!book.isBorrowed() && borrowedBooks.size() < 5) { // Max 5 books
            borrowedBooks.add(book);
            book.borrow();
            return true;
        }
        return false;
    }

    public boolean returnBook(Book book) {
        if (borrowedBooks.contains(book)) {
            borrowedBooks.remove(book);
            book.returnBook();
            return true;
        }
        return false;
    }

    public ArrayList<Book> getBorrowedBooks() {
        return new ArrayList<>(borrowedBooks); // Return a copy
    }

    // Override Person's getName if you want a specific format,
    // otherwise, the Person's getName() will be inherited.
    // The one in Person.java is already good.
    // @Override
    // public String getName() {
    //     return super.getName(); // or custom logic if needed
    // }

    // It's good practice to override equals and hashCode if members are stored in Sets or as keys in Maps
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Member member = (Member) o;
        return Objects.equals(memberId, member.memberId); // memberId should be unique
    }

    @Override
    public int hashCode() {
        return Objects.hash(memberId);
    }
}