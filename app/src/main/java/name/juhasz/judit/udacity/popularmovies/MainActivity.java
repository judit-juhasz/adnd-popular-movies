package name.juhasz.judit.udacity.popularmovies;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Date;

import name.juhasz.judit.udacity.popularmovies.data.MoviesContract;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieOnClickListener,
        FetchMoviesTask.Listener, LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private static final int ID_LOADER_FAVORITE_MOVIES = 1;

    private RecyclerView mMoviesRecyclerView;
    private MovieAdapter mAdapter;
    private ProgressBar mLoadProgressBar;
    private TextView mMessageTextView;

    private int mSelectedMovieList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLoadProgressBar = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        mMessageTextView = (TextView) findViewById(R.id.tv_message_display);
        mMoviesRecyclerView = (RecyclerView) findViewById(R.id.rv_movies);

        final MovieAdapter.MovieOnClickListener listener = this;

        mAdapter = new MovieAdapter(listener);
        mMoviesRecyclerView.setAdapter(mAdapter);

        final int spanCount = 2;
        GridLayoutManager layoutManager = new GridLayoutManager(this, spanCount);
        mMoviesRecyclerView.setLayoutManager(layoutManager);

        showLoadProgressBar();
        loadMovieList(FetchMoviesTask.MOVIE_LIST_POPULAR);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu_item_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int itemId = item.getItemId();

        switch (itemId) {
            case R.id.action_switch_most_popular:
                mSelectedMovieList = itemId;
                loadMovieList(FetchMoviesTask.MOVIE_LIST_POPULAR);
                break;
            case R.id.action_switch_highest_rated:
                mSelectedMovieList = itemId;
                loadMovieList(FetchMoviesTask.MOVIE_LIST_TOP_RATED);
                break;
            case R.id.action_switch_favorites:
                mSelectedMovieList = itemId;
                loadFavoriteMovieList();
                break;
            default:
                Log.w(LOG_TAG, "Menu selection is not handled. ItemId: " + itemId);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(Movie movie) {
        Intent intentToStartDetailsActivity = new Intent(this, DetailsActivity.class);
        intentToStartDetailsActivity.putExtra(DetailsActivity.INTENT_DATA, movie);
        startActivity(intentToStartDetailsActivity);
    }

    @Override
    public void onFetchFinished(Movie[] movies) {
        final boolean moviesDisplayed = (0 != mAdapter.getItemCount());
        final boolean newMovieListAvailable = (null != movies);

        if (newMovieListAvailable) {
            showMoviesList();
            mAdapter.setMoviesData(movies);
        } else {
            if (moviesDisplayed) {
                Log.w(LOG_TAG, "No new data available to refresh the movies list.");
            } else {
                showMessage(R.string.error_no_internet);
            }
        }
    }

    public void loadMovieList(int listType) {
        showLoadProgressBar();
        final FetchMoviesTask.Listener listener = this;
        new FetchMoviesTask(listener, listType).execute();
    }

    public void loadFavoriteMovieList() {
        showLoadProgressBar();
        final Cursor cursor = getContentResolver().query(MoviesContract.MovieEntry.CONTENT_URI,
                null,
                null,
                null,
                null);
        if (null != cursor && cursor.getCount() != 0) {
            final Movie[] movies = new Movie[cursor.getCount()];
            int currentMovieIndex = 0;
            while (cursor.moveToNext()) {
                final Movie movie = new Movie();

                movie.setId(cursor.getString(cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_ID)));
                movie.setTitle(cursor.getString(cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_TITLE)));
                movie.setOriginalTitle(cursor.getString(cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_ORIGINAL_TITLE)));
                movie.setPosterPath(cursor.getString(cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_POSTER_PATH)));
                movie.setSynopsis(cursor.getString(cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_SYNOPSIS)));
                movie.setVoteAverage(cursor.getString(cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_VOTE_AVERAGE)));
                movie.setReleaseDate(new Date(cursor.getInt(cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_RELEASE_DATE))));

                movies[currentMovieIndex++] = movie;
            }
            mAdapter.setMoviesData(movies);
            showMoviesList();
        } else {
            showMessage(R.string.error_no_movie);
        }
    }


    private void showLoadProgressBar() {
        mMoviesRecyclerView.setVisibility(View.INVISIBLE);
        mMessageTextView.setVisibility(View.INVISIBLE);
        mLoadProgressBar.setVisibility(View.VISIBLE);
    }

    private void showMoviesList() {
        mMessageTextView.setVisibility(View.INVISIBLE);
        mLoadProgressBar.setVisibility(View.INVISIBLE);
        mMoviesRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showMessage(int messageStringResourceId) {
        mLoadProgressBar.setVisibility(View.INVISIBLE);
        mMoviesRecyclerView.setVisibility(View.INVISIBLE);

        String message = getString(messageStringResourceId);
        mMessageTextView.setText(message);
        mMessageTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Context context = MainActivity.this;
        switch (id) {
            case ID_LOADER_FAVORITE_MOVIES:
                try {
                    return new CursorLoader(context,
                            MoviesContract.MovieEntry.CONTENT_URI,
                            null,
                            null,
                            null,
                            null);
                } catch (Exception e) {
                    Log.e(LOG_TAG, "Failed to asynchronously load data.");
                    e.printStackTrace();
                    return null;
                }
            default:
                throw new RuntimeException("Loader not implemented: " + id);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (R.id.action_switch_favorites == mSelectedMovieList) {
            mAdapter.setMoviesData(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if (R.id.action_switch_favorites == mSelectedMovieList) {
            mAdapter.setMoviesData((Cursor) null);
        }
    }
}
