package name.juhasz.judit.udacity.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mMoviesRecycleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMoviesRecycleView = (RecyclerView) findViewById(R.id.rv_movies);

        final MovieAdapter adapter = new MovieAdapter();
        mMoviesRecycleView.setAdapter(adapter);

        final int spanCount = 2;
        GridLayoutManager layoutManager = new GridLayoutManager(this, spanCount);
        mMoviesRecycleView.setLayoutManager(layoutManager);
    }
}
