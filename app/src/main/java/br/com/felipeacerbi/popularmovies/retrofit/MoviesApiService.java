package br.com.felipeacerbi.popularmovies.retrofit;

import br.com.felipeacerbi.popularmovies.retrofit.models.MovieRequestResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MoviesApiService {

    @GET("movie/popular")
    Call<MovieRequestResponse> getPopularMovies(@Query("api_key") String apiKey);

    @GET("movie/top_rated")
    Call<MovieRequestResponse> getTopRatedMovies(@Query("api_key") String apiKey);

}
