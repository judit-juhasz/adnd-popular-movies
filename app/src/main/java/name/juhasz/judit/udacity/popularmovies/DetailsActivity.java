package name.juhasz.judit.udacity.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


public class DetailsActivity extends AppCompatActivity {

    public static final String INTENT_DATA = Movie.class.getName();

    private TextView mMovieTitleTextView;
    private TextView mMovieReleaseDateTextView;
    private TextView mMovieVoteAverageTextView;
    private TextView mMovieSynopsisTextView;
    private ImageView mMoviePosterImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        mMovieTitleTextView = (TextView) findViewById(R.id.tv_movie_title);
        mMovieReleaseDateTextView = (TextView) findViewById(R.id.tv_movie_release_date);
        mMovieVoteAverageTextView = (TextView) findViewById(R.id.tv_movie_vote_average);
        mMovieSynopsisTextView = (TextView) findViewById(R.id.tv_movie_synopsis);
        mMoviePosterImageView = (ImageView) findViewById(R.id.iv_movie_poster);

        final Intent intent = getIntent();

        if (intent != null && intent.hasExtra(INTENT_DATA)) {
            final Movie movie = intent.getParcelableExtra(INTENT_DATA);

            mMovieTitleTextView.setText(movie.getTitle());
            mMovieReleaseDateTextView.setText(movie.getReleaseDate().toString());
            mMovieVoteAverageTextView.setText(movie.getVoteAverage());
            mMovieSynopsisTextView.setText(movie.getSynopsis());

            final String posterPath = movie.getPosterPath();
            Picasso.with(this).load(posterPath).into(mMoviePosterImageView);
        }
    }
}
