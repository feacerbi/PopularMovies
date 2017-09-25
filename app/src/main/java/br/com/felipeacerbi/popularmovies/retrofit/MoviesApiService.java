package br.com.felipeacerbi.popularmovies.retrofit;

import br.com.felipeacerbi.popularmovies.retrofit.models.MovieRequestResponse;
import br.com.felipeacerbi.popularmovies.retrofit.models.ReviewRequestResponse;
import br.com.felipeacerbi.popularmovies.retrofit.models.VideoRequestResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MoviesApiService {

    @GET("movie/popular")
    Call<MovieRequestResponse> getPopularMovies(@Query("api_key") String apiKey, @Query("page") int page);

    @GET("movie/top_rated")
    Call<MovieRequestResponse> getTopRatedMovies(@Query("api_key") String apiKey, @Query("page") int page);

    @GET("movie/{movie_id}/videos")
    Call<VideoRequestResponse> getMovieTrailers(@Path("movie_id") int id, @Query("api_key") String apiKey);

    @GET("movie/{movie_id}/reviews")
    Call<ReviewRequestResponse> getMovieReviews(@Path("movie_id") int id, @Query("api_key") String apiKey);

}
