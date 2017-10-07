package name.juhasz.judit.udacity.popularmovies;

import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * Created by Judit on 10/6/2017.
 */

public final class DateUtils {
    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("dd. MM. yyyy");

    public static String formatReleaseDate(final Date date) {
        return DATE_FORMATTER.format(date);
    }
}
