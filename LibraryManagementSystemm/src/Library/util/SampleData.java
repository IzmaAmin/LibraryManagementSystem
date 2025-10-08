package Library.util;

import Library.model.Book;
import Library.model.Member;
import Library.service.LibraryService;

public class SampleData {
    public static void populate(LibraryService service) {
        System.out.println("SampleData.populate: Starting to populate data...");

        // CRITICAL: Ensure these filenames EXACTLY MATCH the files you placed and renamed
        // in your project's "src/images_resources/" folder.
        // And that these files are .jpg or .jpeg or .png (not .webp unless your JavaFX supports it)

        service.addBook(new Book("B001", "The Cottage in the Highlands", "Julie Shackman", "Romance", "/cottage_highlands.jpg"));
        service.addBook(new Book("B002", "Francesco Clemente: nostalgia, utopia", "Peter Lamborn Wilson", "Art", "/francesco_clemente.jpg"));
        service.addBook(new Book("B003", "Citysketch New York", "Melissa Wood", "Art/Travel", "/citysketch_ny.jpg"));
        service.addBook(new Book("B004", "Atomic Habits", "James Clear", "Self-Help", "/atomic_habits.jpg"));
        service.addBook(new Book("B005", "Effective Java", "Joshua Bloch", "Education", "/effective_java.jpg"));
        service.addBook(new Book("B006", "The Alchemist", "Paulo Coelho", "Fiction", "/sapiens_cover.jpg"));
        service.addBook(new Book("B007", "Becoming", "Michelle Obama", "Biography", "/becoming_cover.jpg"));
        service.addBook(new Book("B009", "The Power of Now", "Eckhart Tolle", "Self-Help", "/sapiens_cover.jpg"));  // <-- Rename image to match this

        System.out.println("SampleData.populate: Finished adding books. Current book count in service (via getAllBooks call): " + service.getAllBooks().size());

        service.registerMemberForSeeding(new Member("M001", "Alice", "Wonder", "alice@example.com", "12345678"));
        service.registerMemberForSeeding(new Member("M002", "Bob", "Builder", "bob@example.com", "password123"));
        service.registerMemberForSeeding(new Member("M003", "Charlie", "Brown", "charlie@example.com", "12345678"));
        service.registerMemberForSeeding(new Member("M004", "Diana", "Prince", "diana@example.com", "securepass"));
        service.registerMemberForSeeding(new Member("M005", "Ethan", "Hunt", "ethan@example.com", "mission"));
        System.out.println("SampleData.populate: Finished adding members.");

        System.out.println("SampleData.populate: Attempting sample borrowings...");
        String borrowResult1 = service.borrowBook("M001", "B001");
        System.out.println("SampleData.populate: Borrow M001, B001 -> " + borrowResult1);
        String borrowResult2 = service.borrowBook("M002", "B002");
        System.out.println("SampleData.populate: Borrow M002, B002 -> " + borrowResult2);
        String borrowResult3 = service.borrowBook("M001", "B004");
        System.out.println("SampleData.populate: Borrow M001, B004 -> " + borrowResult3);

        System.out.println("SampleData.populate: Data population complete.");
    }
}