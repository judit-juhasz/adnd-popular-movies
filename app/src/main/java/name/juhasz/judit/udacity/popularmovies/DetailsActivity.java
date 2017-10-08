package name.juhasz.judit.udacity.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

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
        }
    }
}
