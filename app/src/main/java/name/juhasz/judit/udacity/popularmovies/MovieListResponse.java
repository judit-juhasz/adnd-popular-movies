package name.juhasz.judit.udacity.popularmovies;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Judit on 10/5/2017.
 */

public final class MovieListResponse {
    @SerializedName("results")
    private List<Movie> mMovies = new ArrayList<>();

    public List<Movie> getMovies() {
        return mMovies;
    }
}
