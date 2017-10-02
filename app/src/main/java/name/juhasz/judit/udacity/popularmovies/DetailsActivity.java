package name.juhasz.judit.udacity.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;


public class DetailsActivity extends AppCompatActivity {

    private TextView mDisplayTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

       mDisplayTextView = (TextView) findViewById(R.id.tv_display_text);
    }
}
