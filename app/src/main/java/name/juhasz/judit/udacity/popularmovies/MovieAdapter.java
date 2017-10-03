package name.juhasz.judit.udacity.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.squareup.picasso.Picasso;

/**
 * Created by Judit on 10/2/2017.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private final static String LOG_TAG = MovieAdapter.class.getSimpleName();

    private Movie[] mMovies;

    public MovieAdapter(Movie[] movies) {
        mMovies = movies;
    }

    private MovieAdapter.MovieOnClickListener mListener;

    public interface MovieOnClickListener {
        void onItemClick(Movie movie);
    }

    public MovieAdapter(MovieAdapter.MovieOnClickListener listener) {
        mMovies = null;
        mListener = listener;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();

        int layoutIdForListItem = R.layout.item_movie;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        MovieViewHolder viewHolder = new MovieViewHolder(context, view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        final Movie movie = mMovies[position];
        holder.bind(movie);
    }

    @Override
    public int getItemCount() {
        if (mMovies == null) {
            return 0;
        } else {
            return mMovies.length;
        }
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private final Context mContext;

        private ImageView mMoviePoster;

        public MovieViewHolder(Context context, View itemView) {
            super(itemView);

            mContext = context;

            mMoviePoster = (ImageView) itemView.findViewById(R.id.tv_movie_poster);
            mMoviePoster.setOnClickListener(this);
        }

        void bind(Movie movie) {
            final String posterPath = movie.getPosterPath();
            Picasso.with(mContext).load(posterPath).into(mMoviePoster);
        }

        @Override
        public void onClick(View view) {
            if (null != mMovies) {
                int adapterPosition = getAdapterPosition();
                Movie movie = mMovies[adapterPosition];
                mListener.onItemClick(movie);
            } else {
                Log.wtf(LOG_TAG, "OnClick handler call with empty movie list.");
            }

        }
    }
}
