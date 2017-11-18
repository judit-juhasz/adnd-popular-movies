package name.juhasz.judit.udacity.popularmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import name.juhasz.judit.udacity.popularmovies.data.MoviesContract;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static name.juhasz.judit.udacity.popularmovies.DateUtils.formatReleaseDate;

public class DetailsActivity extends AppCompatActivity implements TrailerAdapter.OnClickListener {

    private final static String LOG_TAG = DetailsActivity.class.getSimpleName();

    public static final String INTENT_DATA = Movie.class.getName();

    private RecyclerView mReviewsRecyclerView;
    private TextView mReviewLabelTextView;
    private ReviewAdapter mReviewAdapter;

    private RecyclerView mTrailersRecyclerView;
    private TrailerAdapter mTrailerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        final TextView movieTitleTextView = (TextView) findViewById(R.id.tv_movie_title);
        final TextView movieOriginalTitleTextView = (TextView) findViewById(R.id.tv_movie_original_title);
        final TextView movieReleaseDateTextView = (TextView) findViewById(R.id.tv_movie_release_date);
        final TextView movieVoteAverageTextView = (TextView) findViewById(R.id.tv_movie_vote_average);
        final TextView movieSynopsisTextView = (TextView) findViewById(R.id.tv_movie_synopsis);
        final ImageView moviePosterImageView = (ImageView) findViewById(R.id.iv_movie_poster);
        final Button favoriteMovieButton = (Button) findViewById(R.id.b_favorite_movie);
        mReviewLabelTextView = (TextView) findViewById(R.id.tv_review_label);
        mReviewsRecyclerView = (RecyclerView) findViewById(R.id.rv_movies_reviews);
        mTrailersRecyclerView = (RecyclerView) findViewById(R.id.rv_trailers);

        final Intent intent = getIntent();

        if (intent != null && intent.hasExtra(INTENT_DATA)) {
            final Movie movie = intent.getParcelableExtra(INTENT_DATA);

            movieTitleTextView.setText(movie.getTitle());
            movieOriginalTitleTextView.setText(getString(R.string.original_title_format,
                    movie.getOriginalTitle()));
            movieReleaseDateTextView.setText(getString(R.string.release_date_format,
                    formatReleaseDate(movie.getReleaseDate())));
            movieVoteAverageTextView.setText(getString(R.string.average_vote_format,
                    movie.getVoteAverage()));
            movieSynopsisTextView.setText(movie.getSynopsis());

            final String posterPath = movie.getPosterPath();
            Picasso.with(this).load(posterPath).into(moviePosterImageView);

            final String movieId = movie.getId();

            final Cursor cursor = getContentResolver().query(MoviesContract.MovieEntry.CONTENT_URI,
                    new String[]{MoviesContract.MovieEntry.COLUMN_ID},
                    MoviesContract.MovieEntry.COLUMN_ID + "=?",
                    new String[]{movieId},
                    null);

            if (null!= cursor && cursor.getCount() != 0) {
                favoriteMovieButton.setText(getString(R.string.button_remove_movie_from_favorites));
            } else {
                favoriteMovieButton.setText(getString(R.string.button_add_movie_to_favorites));
            }

            mReviewAdapter = new ReviewAdapter();
            mReviewsRecyclerView.setAdapter(mReviewAdapter);
            loadReview(movieId);

            mTrailerAdapter = new TrailerAdapter(this);
            mTrailersRecyclerView.setAdapter(mTrailerAdapter);
            loadTrailers(movie.getId());
        }
    }

    public void onClickFavoritesButton(View view) {
        Button favoritesButton = (Button) view;
        String favoritesButtonText = favoritesButton.getText().toString();
        Intent intent = getIntent();
        Movie movie = (Movie) intent.getParcelableExtra(INTENT_DATA);

        if (favoritesButtonText.equals(getString(R.string.button_add_movie_to_favorites))) {
            ContentValues values = new ContentValues();
            values.put(MoviesContract.MovieEntry.COLUMN_ID, movie.getId());
            values.put(MoviesContract.MovieEntry.COLUMN_TITLE, movie.getTitle());
            values.put(MoviesContract.MovieEntry.COLUMN_ORIGINAL_TITLE, movie.getOriginalTitle());
            values.put(MoviesContract.MovieEntry.COLUMN_SYNOPSIS, movie.getSynopsis());
            values.put(MoviesContract.MovieEntry.COLUMN_VOTE_AVERAGE, movie.getVoteAverage());
            String releaseDate = String.valueOf(movie.getReleaseDate().getTime());
            values.put(MoviesContract.MovieEntry.COLUMN_RELEASE_DATE, releaseDate);
            values.put(MoviesContract.MovieEntry.COLUMN_POSTER_PATH, movie.getPosterPath());
            getContentResolver().insert(MoviesContract.MovieEntry.CONTENT_URI, values);

            favoritesButton.setText(getString(R.string.button_remove_movie_from_favorites));
        } else {
            getContentResolver().delete(MoviesContract.MovieEntry.CONTENT_URI,
                    MoviesContract.MovieEntry.COLUMN_ID + "=?", new String[]{movie.getId()});

            favoritesButton.setText(getString(R.string.button_add_movie_to_favorites));
        }
    }

    private void loadReview(final String movieId) {

        final String apiKey = BuildConfig.THE_MOVIE_DB_API_KEY;

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(TheMovieDbService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final Class<TheMovieDbService> theMovieDbServiceDefinition = TheMovieDbService.class;
        final TheMovieDbService theMovieDbService = retrofit.create(theMovieDbServiceDefinition);
        final Call<ReviewListResponse> call = theMovieDbService.getReviewsForMovie(movieId, apiKey);
        call.enqueue(new Callback<ReviewListResponse>() {
                         @Override
                         public void onResponse(final Call<ReviewListResponse> call,
                                                final Response<ReviewListResponse> response) {
                             if (response.isSuccessful()) {
                                 final List<Review> reviews = response.body().getReviews();
                                 mReviewAdapter.setReviews(reviews.toArray(new Review[0]));
                             }
                         }

                         @Override
                         public void onFailure(final Call<ReviewListResponse> call,
                                               final Throwable t) {
                             mReviewLabelTextView.setVisibility(View.GONE);
                             mReviewsRecyclerView.setVisibility(View.GONE);
                         }
                     }
        );
    }

    private void loadTrailers(final String movieId) {

        final String apiKey = BuildConfig.THE_MOVIE_DB_API_KEY;

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(TheMovieDbService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final Class<TheMovieDbService> theMovieDbServiceDefinition = TheMovieDbService.class;
        final TheMovieDbService theMovieDbService = retrofit.create(theMovieDbServiceDefinition);

        final Call<TrailerListResponse> call = theMovieDbService.getTrailersForMovie(movieId, apiKey);
        call.enqueue(new Callback<TrailerListResponse>() {
                         @Override
                         public void onResponse(final Call<TrailerListResponse> call,
                                                final Response<TrailerListResponse> response) {
                             if (response.isSuccessful()) {
                                 final List<Trailer> trailers = response.body().getTrailers();
                                 mTrailerAdapter.setTrailers(trailers.toArray(new Trailer[0]));
                             }
                         }

                         @Override
                         public void onFailure(final Call<TrailerListResponse> call,
                                               final Throwable t) {

                         }
                     }
        );
    }

    @Override
    public void onTrailerItemClick(Trailer trailer) {
        final Uri trailerUri = Uri.parse(trailer.getLink());
        final Intent trailerIntent = new Intent(Intent.ACTION_VIEW, trailerUri);
        if (null != trailerIntent.resolveActivity(getPackageManager())) {
            startActivity(trailerIntent);
        } else {
            Log.w(LOG_TAG, "No video player app found on the device.");
            Toast.makeText(this, "Cannot find video player app.", Toast.LENGTH_SHORT).show();
        }
    }
}
