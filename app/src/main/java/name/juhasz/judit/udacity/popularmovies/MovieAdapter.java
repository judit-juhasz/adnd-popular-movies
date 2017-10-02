package name.juhasz.judit.udacity.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Judit on 10/2/2017.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private static final Movie[] sDummyData = {
            new Movie("Me before you"),
            new Movie("Avatar"),
            new Movie("A beautiful mind"),
            new Movie("Black Hawk Down"),
            new Movie("Tangled"),
            new Movie("Pearl Harbour"),
            new Movie("Saving private Ran"),
            new Movie("Son of Saul"),
    };

    private MovieOnClickListener mListener;

    public interface MovieOnClickListener {
        void onItemClick(Movie movie);
    }

    public MovieAdapter(MovieOnClickListener listener) {
        mListener = listener;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();

        int layoutIdForListItem = R.layout.item_movie;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        MovieViewHolder viewHolder = new MovieViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return sDummyData.length;
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView movieTitleTextView;

        public MovieViewHolder(View itemView) {
            super(itemView);

            movieTitleTextView = (TextView) itemView.findViewById(R.id.tv_movie_title);
            movieTitleTextView.setOnClickListener(this);
        }

        void bind(int position) {
            final Movie movie = sDummyData[position];
            String titleOfMovie = movie.getTitle();
            movieTitleTextView.setText(titleOfMovie);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            Movie movie = sDummyData[adapterPosition];
            mListener.onItemClick(movie);
        }
    }
}
