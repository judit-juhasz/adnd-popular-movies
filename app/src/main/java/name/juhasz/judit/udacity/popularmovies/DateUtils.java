package name.juhasz.judit.udacity.popularmovies;

import java.text.SimpleDateFormat;
import java.util.Date;

public final class DateUtils {
    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("dd. MM. yyyy");

    public static String formatReleaseDate(final Date date) {
        return DATE_FORMATTER.format(date);
    }
}
