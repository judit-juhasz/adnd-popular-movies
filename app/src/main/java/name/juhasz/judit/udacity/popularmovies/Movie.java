package name.juhasz.judit.udacity.popularmovies;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Judit on 10/2/2017.
 */

public class Movie {

    private static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w185/";

    @SerializedName("title")
    private String mTitle;

    @SerializedName("poster_path")
    private String mPosterPath;

    public Movie(String title, String posterPath) {
        mTitle = title;
        mPosterPath = posterPath;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getPosterPath() { return IMAGE_BASE_URL + mPosterPath; }
}
