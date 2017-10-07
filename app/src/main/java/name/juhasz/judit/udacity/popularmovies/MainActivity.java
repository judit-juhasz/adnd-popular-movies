package name.juhasz.judit.udacity.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieOnClickListener, FetchMoviesTask.Listener {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private RecyclerView mMoviesRecycleView;
    private MovieAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMoviesRecycleView = (RecyclerView) findViewById(R.id.rv_movies);
        final MovieAdapter.MovieOnClickListener listener = this;

        mAdapter = new MovieAdapter(listener);
        mMoviesRecycleView.setAdapter(mAdapter);

        final int spanCount = 2;
        GridLayoutManager layoutManager = new GridLayoutManager(this, spanCount);
        mMoviesRecycleView.setLayoutManager(layoutManager);

        new FetchMoviesTask(this).execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu_item_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Context context = MainActivity.this;
        final int itemId = item.getItemId();

        switch (itemId) {
            case R.id.action_switch_most_popular:
                Toast.makeText(context, "Most popular selected", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_switch_highest_rated:
                Toast.makeText(context, "Highest rated selected.", Toast.LENGTH_SHORT).show();
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
        mAdapter.setMoviesData(movies);
    }
}
