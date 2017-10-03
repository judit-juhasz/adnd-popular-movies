package name.juhasz.judit.udacity.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.squareup.picasso.Picasso;

/**
 * Created by Judit on 10/2/2017.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private static final Movie[] sDummyData = {
            new Movie("Rogue One", "https://images-na.ssl-images-amazon.com/images/M/MV5BMjEwMzMxODIzOV5BMl5BanBnXkFtZTgwNzg3OTAzMDI@._V1_SY1000_SX675_AL_.jpg"),
            new Movie("The Dark Knight", "https://images-na.ssl-images-amazon.com/images/M/MV5BMTMxNTMwODM0NF5BMl5BanBnXkFtZTcwODAyMTk2Mw@@._V1_SY1000_CR0,0,675,1000_AL_.jpg"),
            new Movie("Pulp Fiction", "https://images-na.ssl-images-amazon.com/images/M/MV5BMTkxMTA5OTAzMl5BMl5BanBnXkFtZTgwNjA5MDc3NjE@._V1_SY1000_CR0,0,673,1000_AL_.jpg"),
            new Movie("Passangers", "https://images-na.ssl-images-amazon.com/images/M/MV5BMTk4MjU3MDIzOF5BMl5BanBnXkFtZTgwMjM2MzY2MDI@._V1_SY1000_CR0,0,675,1000_AL_.jpg"),
            new Movie("Forrest Gump", "https://images-na.ssl-images-amazon.com/images/M/MV5BYThjM2MwZGMtMzg3Ny00NGRkLWE4M2EtYTBiNWMzOTY0YTI4XkEyXkFqcGdeQXVyNDYyMDk5MTU@._V1_SY1000_CR0,0,757,1000_AL_.jpg"),
            new Movie("Moana", "https://images-na.ssl-images-amazon.com/images/M/MV5BMjI4MzU5NTExNF5BMl5BanBnXkFtZTgwNzY1MTEwMDI@._V1_SY1000_CR0,0,674,1000_AL_.jpg"),
            new Movie("Fight Club", "https://images-na.ssl-images-amazon.com/images/M/MV5BMzc1YmU2ZjEtYWIwMC00ZjM3LWI0NTctMDVlNGQ3YmYwMzE5XkEyXkFqcGdeQXVyMTQxNzMzNDI@._V1_SY999_CR0,0,704,999_AL_.jpg"),
            new Movie("Frozen", "https://images-na.ssl-images-amazon.com/images/M/MV5BMTQ1MjQwMTE5OF5BMl5BanBnXkFtZTgwNjk3MTcyMDE@._V1_SY1000_CR0,0,674,1000_AL_.jpg")
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
        MovieViewHolder viewHolder = new MovieViewHolder(context, view);

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

        private final Context mContext;

        public ImageView moviePoster;

        public MovieViewHolder(Context context, View itemView) {
            super(itemView);

            mContext = context;

            moviePoster = (ImageView) itemView.findViewById(R.id.tv_movie_poster);
            moviePoster.setOnClickListener(this);
        }

        void bind(int position) {
            final Movie movie = sDummyData[position];
            final String posterPath = movie.getPosterPath();
            Picasso.with(mContext).load(posterPath).into(moviePoster);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            Movie movie = sDummyData[adapterPosition];
            mListener.onItemClick(movie);
        }
    }
}
