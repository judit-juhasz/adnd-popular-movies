package name.juhasz.judit.udacity.popularmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import name.juhasz.judit.udacity.popularmovies.data.MoviesContract;

import static name.juhasz.judit.udacity.popularmovies.DateUtils.formatReleaseDate;

public class DetailsActivity extends AppCompatActivity {

    public static final String INTENT_DATA = Movie.class.getName();

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
}
