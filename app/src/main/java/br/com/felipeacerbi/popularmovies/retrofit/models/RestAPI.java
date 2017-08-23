package br.com.felipeacerbi.popularmovies.retrofit.models;

import android.content.Context;

import javax.inject.Inject;

import br.com.felipeacerbi.popularmovies.R;
import br.com.felipeacerbi.popularmovies.retrofit.MoviesApiService;
import retrofit2.Call;

public class RestAPI {

    @Inject
    MoviesApiService moviesApiService;

    @Inject
    RestAPI() {}

    public Call<MovieRequestResponse> getMostPopularMovies(Context context) {
        return moviesApiService.getPopularMovies(context.getString(R.string.movies_api_key));
    }

    public Call<MovieRequestResponse> getTopRatedMovies(Context context) {
        return moviesApiService.getTopRatedMovies(context.getString(R.string.movies_api_key));
    }
}
