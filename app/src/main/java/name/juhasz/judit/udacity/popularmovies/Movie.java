package name.juhasz.judit.udacity.popularmovies;

/**
 * Created by Judit on 10/2/2017.
 */

public class Movie {

    private String mTitle;
    private String mPosterPath;

    public Movie(String title, String posterPath) {
        mTitle = title;
        mPosterPath = posterPath;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getPosterPath() { return mPosterPath; }
}
