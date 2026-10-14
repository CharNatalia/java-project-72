package hexlet.code.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Formatter {
    private static final int MAX_PREVIEW_LENGTH = 200;

    public static String formatedDate(LocalDateTime time) {
        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        return time.format(formatter);
    }

    public static String truncate(String text) {
        if (text == null) {
            return "";
        }
        return text.length() > MAX_PREVIEW_LENGTH
                ? text.substring(0, MAX_PREVIEW_LENGTH) + "..."
                : text;
    }
}
