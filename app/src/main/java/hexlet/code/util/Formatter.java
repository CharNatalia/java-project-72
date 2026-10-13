package hexlet.code.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Formatter {
    public static String formatedDate(LocalDateTime time) {
        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        return time.format(formatter);
    }

    public static String truncate(String text) {
        if (text == null) {
            return "";
        }
        return text.length() > 200
                ? text.substring(0, 200) + "..."
                : text;
    }
}
