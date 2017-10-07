package name.juhasz.judit.udacity.popularmovies;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by Judit on 10/4/2017.
 */

public class FetchMoviesTask extends AsyncTask <Void, Void, Movie[]> {


    private static final String LOG_TAG = FetchMoviesTask.class.getSimpleName();
    private static final String THE_MOVIE_DB_BASE_URL = "http://api.themoviedb.org/3/";

    public static final int MOVIE_LIST_POPULAR = 1;
    public static final int MOVIE_LIST_TOP_RATED = 2;

    private Listener mListener;
    private TheMovieDbService mTheMovieDbService;
    private int mListType;

    interface Listener {
        void onFetchFinished(Movie[] movies);
    }

    public FetchMoviesTask(Listener listener, int listType) {

        if (null == listener) {
            final String errorMessage = "The listener cannot be null.";
            Log.e(LOG_TAG, errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }

        mListener = listener;

        if (listType == MOVIE_LIST_POPULAR) {
            mListType = listType;
        } else if (listType == MOVIE_LIST_TOP_RATED) {
            mListType = listType;
        } else {
            final String errorMessage = "Unknown list type for FetchMoviesTask. List type: "
                    + listType;
            Log.e(LOG_TAG, errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(THE_MOVIE_DB_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final Class<TheMovieDbService> theMovieDbServiceDefinition = TheMovieDbService.class;

        mTheMovieDbService = retrofit.create(theMovieDbServiceDefinition);
    }

    @Override
    protected Movie[] doInBackground(Void... params) {

        final Call<MovieListResponse> moviesCall = getMovieListCall(mListType);
        if (null == moviesCall) {
            return null;
        }

        try {
            final Response<MovieListResponse> response = moviesCall.execute();
            if (response.isSuccessful()) {
                final List<Movie> movies = response.body().getMovies();

                return movies.toArray(new Movie[0]);
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "The Movie DB service connection error: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Movie[] movies) {
        if (null == mListener) {
            Log.w(LOG_TAG, "Nobody is listening for FetchMoviesTask.");
        } else {
            mListener.onFetchFinished(movies);
        }
    }

    private Call<MovieListResponse> getMovieListCall(int listType) {
        final String apiKey = BuildConfig.THE_MOVIE_DB_API_KEY;

        Call<MovieListResponse> movieCall = null;
        switch (listType) {
            case MOVIE_LIST_POPULAR:
                movieCall = mTheMovieDbService.getPopularMovies(apiKey);
                break;
            case MOVIE_LIST_TOP_RATED:
                movieCall =  mTheMovieDbService.getTopRatedMovies(apiKey);
                break;
            default:
                Log.wtf(LOG_TAG, "Unknown list type for FetchMoviesTask.");
        }

        return movieCall;
    }
}
