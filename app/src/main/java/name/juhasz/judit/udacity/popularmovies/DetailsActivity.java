package name.juhasz.judit.udacity.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;


public class DetailsActivity extends AppCompatActivity {

    private TextView mTitleTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        mTitleTextView = (TextView) findViewById(R.id.tv_movie_title);
        Intent intent = getIntent();

        if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
            mTitleTextView.setText(intent.getStringExtra(Intent.EXTRA_TEXT));
        }
    }
}
