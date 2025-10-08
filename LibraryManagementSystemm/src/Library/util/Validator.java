package Library.util;

public class Validator {
    public static boolean isEmpty(String... fields) {
        for (String field : fields) {
            if (field == null || field.trim().isEmpty()) return true;
        }
        return false;
    }

    public static boolean isValidId(String id) {
        return id != null && id.matches("[A-Za-z0-9]+");
    }
}
