package name.juhasz.judit.udacity.popularmovies;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

interface TheMovieDbService {

    @GET("movie/popular/")
    Call<MovieListResponse> getPopularMovies(@Query("api_key") String apiKey);

    @GET("movie/top_rated/")
    Call<MovieListResponse> getTopRatedMovies(@Query("api_key") String apiKey);

}