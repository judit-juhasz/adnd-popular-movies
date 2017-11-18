package name.juhasz.judit.udacity.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {

    private final static String LOG_TAG = TrailerAdapter.class.getSimpleName();

    private Trailer[] mTrailers;

    private TrailerAdapter.OnClickListener mListener;

    public interface OnClickListener {
        void onTrailerItemClick(Trailer trailer);
    }

    public TrailerAdapter(TrailerAdapter.OnClickListener listener) {
        mTrailers = null;
        mListener = listener;
    }

    @Override
    public TrailerViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
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
    public void onBindViewHolder(final TrailerViewHolder holder, final int position) {
        final Trailer trailer = mTrailers[position];
        holder.bind(trailer);
    }

    @Override
    public int getItemCount() {
        return (null != mTrailers) ? mTrailers.length : 0;
    }

    public class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final Context mContext;

        private ImageView mTrailerThumbnail;

        public TrailerViewHolder(final Context context, final View itemView) {
            super(itemView);

            mContext = context;
            mTrailerThumbnail =
                    (ImageView) itemView.findViewById(R.id.iv_trailer_thumbnail);
            mTrailerThumbnail.setOnClickListener(this);
        }

        public void bind(final Trailer trailer) {
            final String thumbnailLink = trailer.getThumbnailLink();
            Picasso.with(mContext).load(thumbnailLink).into(mTrailerThumbnail);
        }

        @Override
        public void onClick(final View view) {
            if (null != mTrailers) {
                final int position = getAdapterPosition();
                final Trailer trailer = mTrailers[position];
                mListener.onTrailerItemClick(trailer);
            } else {
                Log.wtf(LOG_TAG, "OnClick handler call with empty trailers list.");
            }
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
