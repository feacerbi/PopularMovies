package br.com.felipeacerbi.popularmovies.retrofit;

import javax.inject.Inject;

import br.com.felipeacerbi.popularmovies.BuildConfig;
import br.com.felipeacerbi.popularmovies.retrofit.models.MovieRequestResponse;
import br.com.felipeacerbi.popularmovies.retrofit.models.ReviewRequestResponse;
import br.com.felipeacerbi.popularmovies.retrofit.models.VideoRequestResponse;
import retrofit2.Call;

public class RestAPI {

    private static final String API_KEY = BuildConfig.API_KEY;

    @Inject
    MoviesApiService moviesApiService;

    @Inject
    RestAPI() {}

    public Call<MovieRequestResponse> getMostPopularMovies(int page) {
        return moviesApiService.getPopularMovies(API_KEY, page);
    }

    public Call<MovieRequestResponse> getTopRatedMovies(int page) {
        return moviesApiService.getTopRatedMovies(API_KEY, page);
    }

    public Call<VideoRequestResponse> getMovieTrailers(int movieId) {
        return moviesApiService.getMovieTrailers(movieId, API_KEY);
    }

    public Call<ReviewRequestResponse> getMovieReviews(int movieId) {
        return moviesApiService.getMovieReviews(movieId, API_KEY);
    }

}
