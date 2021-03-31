package common;

import spark.Request;

import java.time.Duration;
import java.time.LocalDate;

public class Utils {
    public static String queryParams(Request req, String name) {
        String value = req.queryParams(name);
        if (value == null) {
            throw new IllegalArgumentException("Missing required query parameter: '%s'".formatted(name));
        }
        return value;
    }

    public static String formatDuration(Duration duration) {
        return duration.toString()
                .substring(2)
                .replaceAll("(\\d[HMS])(?!$)", "$1 ")
                .toLowerCase();
    }
}
