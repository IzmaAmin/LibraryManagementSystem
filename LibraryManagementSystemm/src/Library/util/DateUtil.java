package Library.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtil {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");

    public static String getTodayDate() {
        return LocalDate.now().format(formatter);
    }
}
