package name.juhasz.judit.udacity.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

import com.google.gson.annotations.SerializedName;

public class Movie implements Parcelable {

    private static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w185/";

    @SerializedName("title")
    private String mTitle;

    @SerializedName("original_title")
    private String mOriginalTitle;

    @SerializedName("poster_path")
    private String mPosterPath;

    @SerializedName("overview")
    private String mSynopsis;

    @SerializedName("vote_average")
    private String mVoteAverage;

    @SerializedName("release_date")
    private Date mReleaseDate;

    public Movie() { }

    public String getTitle() {
        return mTitle;
    }

    public String getOriginalTitle() {
        return mOriginalTitle;
    }

    public String getPosterPath() {
        return IMAGE_BASE_URL + mPosterPath;
    }

    public String getSynopsis() {
        return mSynopsis;
    }

    public String getVoteAverage() {
        return mVoteAverage;
    }

    public Date getReleaseDate() {
        return mReleaseDate;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mTitle);
        dest.writeString(mOriginalTitle);
        dest.writeString(mPosterPath);
        dest.writeString(mSynopsis);
        dest.writeString(mVoteAverage);
        dest.writeLong(mReleaseDate.getTime());
    }

    public Movie(Parcel in) {
        mTitle = in.readString();
        mOriginalTitle = in.readString();
        mPosterPath = in.readString();
        mSynopsis = in.readString();
        mVoteAverage = in.readString();
        mReleaseDate = new Date(in.readLong());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
