package Library.ui;

import Library.model.Member;
import Library.service.LibraryService;
import Library.util.SampleData;

public class SharedService {
    private static final LibraryService libraryServiceInstance = new LibraryService();
    private static Member loggedInMember = null;

    static {
        // Populate sample data when this class is loaded
        SampleData.populate(libraryServiceInstance);
    }

    public static LibraryService getLibraryServiceInstance() {
        return libraryServiceInstance;
    }

    public static Member getLoggedInMember() {
        return loggedInMember;
    }

    public static void setLoggedInMember(Member member) {
        loggedInMember = member;
    }

    public static void logoutMember() {
        loggedInMember = null;
    }

    public static boolean isLoggedIn() {
        return loggedInMember != null;
    }
}