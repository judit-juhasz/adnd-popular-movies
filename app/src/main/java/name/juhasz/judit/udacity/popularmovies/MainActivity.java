package name.juhasz.judit.udacity.popularmovies;

import android.content.Intent;
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

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieOnClickListener, FetchMoviesTask.Listener {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private RecyclerView mMoviesRecycleView;
    private MovieAdapter mAdapter;
    private ProgressBar mLoadProgressBar;
    private TextView mMessageTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLoadProgressBar = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        mMessageTextView = (TextView) findViewById(R.id.tv_message_display);
        mMoviesRecycleView = (RecyclerView) findViewById(R.id.rv_movies);

        final MovieAdapter.MovieOnClickListener listener = this;

        mAdapter = new MovieAdapter(listener);
        mMoviesRecycleView.setAdapter(mAdapter);

        final int spanCount = 2;
        GridLayoutManager layoutManager = new GridLayoutManager(this, spanCount);
        mMoviesRecycleView.setLayoutManager(layoutManager);

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
                showLoadProgressBar();
                loadMovieList(FetchMoviesTask.MOVIE_LIST_POPULAR);
                break;
            case R.id.action_switch_highest_rated:
                showLoadProgressBar();
                loadMovieList(FetchMoviesTask.MOVIE_LIST_TOP_RATED);
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
        final FetchMoviesTask.Listener listener = this;
        new FetchMoviesTask(listener, listType).execute();
    }

    private void showLoadProgressBar() {
        mMoviesRecycleView.setVisibility(View.INVISIBLE);
        mMessageTextView.setVisibility(View.INVISIBLE);
        mLoadProgressBar.setVisibility(View.VISIBLE);
    }

    private void showMoviesList() {
        mMessageTextView.setVisibility(View.INVISIBLE);
        mLoadProgressBar.setVisibility(View.INVISIBLE);
        mMoviesRecycleView.setVisibility(View.VISIBLE);
    }

    private void showMessage(int messageStringResourceId) {
        mLoadProgressBar.setVisibility(View.INVISIBLE);
        mMoviesRecycleView.setVisibility(View.INVISIBLE);

        String message = getString(messageStringResourceId);
        mMessageTextView.setText(message);
        mMessageTextView.setVisibility(View.VISIBLE);
    }
}
