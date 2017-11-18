package name.juhasz.judit.udacity.popularmovies;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {

    private Trailer[] mTrailers;

    public TrailerAdapter() {
        mTrailers = null;
    }

    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final Context context = parent.getContext();

        final LayoutInflater inflater = LayoutInflater.from(context);

        final boolean shouldAttachToParentImmediately = false;
        final int layoutIdForRecyclerViewItem = R.layout.item_trailer;

        final View view = inflater.inflate(layoutIdForRecyclerViewItem, parent,
                shouldAttachToParentImmediately);

        final TrailerViewHolder viewHolder = new TrailerViewHolder(context, view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TrailerViewHolder holder, int position) {
        final Trailer trailer = mTrailers[position];
        holder.bind(trailer);
    }

    @Override
    public int getItemCount() {
        return (null != mTrailers) ? mTrailers.length : 0;
    }

    public class TrailerViewHolder extends RecyclerView.ViewHolder {

        private final Context mContext;

        private ImageView mTrailerThumbnail;

        public TrailerViewHolder(Context context, View itemView) {
            super(itemView);

            mContext = context;
            mTrailerThumbnail =
                    (ImageView) itemView.findViewById(R.id.iv_trailer_thumbnail);
        }

        public void bind(Trailer trailer) {
            final String thumbnailLink = trailer.getThumbnailLink();
            Picasso.with(mContext).load(thumbnailLink).into(mTrailerThumbnail);
        }
    }

    public Trailer[] getTrailers() {
        return mTrailers;
    }

    public void setTrailers(final Trailer[] trailers) {
        this.mTrailers = trailers;
        notifyDataSetChanged();
    }

}
