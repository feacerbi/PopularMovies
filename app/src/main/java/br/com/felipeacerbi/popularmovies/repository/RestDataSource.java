package br.com.felipeacerbi.popularmovies.repository;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import br.com.felipeacerbi.popularmovies.models.Movie;
import br.com.felipeacerbi.popularmovies.models.MoviesArrayList;
import br.com.felipeacerbi.popularmovies.models.Review;
import br.com.felipeacerbi.popularmovies.models.Video;
import br.com.felipeacerbi.popularmovies.retrofit.RestAPI;
import br.com.felipeacerbi.popularmovies.retrofit.models.MovieRequestResponse;
import br.com.felipeacerbi.popularmovies.retrofit.models.MovieResult;
import br.com.felipeacerbi.popularmovies.retrofit.models.MoviesFilter;
import br.com.felipeacerbi.popularmovies.retrofit.models.ReviewRequestResponse;
import br.com.felipeacerbi.popularmovies.retrofit.models.ReviewResult;
import br.com.felipeacerbi.popularmovies.retrofit.models.VideoRequestResponse;
import br.com.felipeacerbi.popularmovies.retrofit.models.VideoResult;
import br.com.felipeacerbi.popularmovies.utils.RequestCallback;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Singleton
class RestDataSource {

    @Inject
    RestAPI restAPI;

    @Inject
    RestDataSource() {}

    void getMovies(int filter, int page, final RequestCallback<List<Movie>> callback) {

        Call<MovieRequestResponse> movies;

        switch (filter) {
            case MoviesFilter.MOST_POPULAR:
                movies = restAPI.getMostPopularMovies(page);
                break;
            case MoviesFilter.TOP_RATED:
                movies = restAPI.getTopRatedMovies(page);
                break;
            default:
                callback.onError("Wrong filter");
                return;
        }

        if (movies != null) {
            movies.enqueue(new Callback<MovieRequestResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<MovieRequestResponse> call, @NonNull Response<MovieRequestResponse> response) {
                        if(response.isSuccessful()) {
                            MovieRequestResponse movieResponse = response.body();
                            callback.onSuccess(createMoviesListFromResponse(movieResponse));
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<MovieRequestResponse> call, @NonNull Throwable t) {
                        callback.onError(t.getMessage());
                    }
            });
        }
    }

    void getVideos(int movieId, final RequestCallback<List<Video>> callback) {

        Call<VideoRequestResponse> videos = restAPI.getMovieTrailers(movieId);

        if (videos != null) {
            videos.enqueue(new Callback<VideoRequestResponse>() {
                @Override
                public void onResponse(@NonNull Call<VideoRequestResponse> call, @NonNull Response<VideoRequestResponse> response) {
                    if(response.isSuccessful()) {
                        VideoRequestResponse videoResponse = response.body();
                        callback.onSuccess(createMovieTrailersListFromResponse(videoResponse));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<VideoRequestResponse> call, @NonNull Throwable t) {
                    callback.onError(t.getMessage());
                }
            });
        }
    }

    void getReviews(int movieId, final RequestCallback<List<Review>> callback) {

        Call<ReviewRequestResponse> reviews = restAPI.getMovieReviews(movieId);

        if (reviews != null) {
            reviews.enqueue(new Callback<ReviewRequestResponse>() {
                @Override
                public void onResponse(@NonNull Call<ReviewRequestResponse> call, @NonNull Response<ReviewRequestResponse> response) {
                    if(response.isSuccessful()) {
                        ReviewRequestResponse reviewResponse = response.body();
                        callback.onSuccess(createMovieReviewsListFromResponse(reviewResponse));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ReviewRequestResponse> call, @NonNull Throwable t) {
                    callback.onError(t.getMessage());
                }
            });
        }
    }

    private MoviesArrayList createMoviesListFromResponse(MovieRequestResponse movieResponse) {
        List<Movie> returnList = new ArrayList<>();

        for (MovieResult movieResult : movieResponse.getResults()) {
            Movie movie = new Movie(
                    movieResult.getId(),
                    movieResult.getTitle(),
                    movieResult.getOriginal_title(),
                    movieResult.getPoster_path(),
                    movieResult.getOverview(),
                    movieResult.getRelease_date(),
                    movieResult.getPopularity(),
                    movieResult.getVote_average(),
                    movieResult.getVote_count(),
                    false);
            returnList.add(movie);
        }

        return new MoviesArrayList(returnList);
    }

    private List<Video> createMovieTrailersListFromResponse(VideoRequestResponse videoResponse) {
        List<Video> returnList = new ArrayList<>();

        for (VideoResult videoResult : videoResponse.getResults()) {
            Video video = new Video(
                    videoResult.getKey(),
                    videoResult.getName(),
                    videoResult.getSite(),
                    videoResult.getSize(),
                    videoResult.getType());
            returnList.add(video);
        }

        return returnList;
    }

    private List<Review> createMovieReviewsListFromResponse(ReviewRequestResponse reviewResponse) {
        List<Review> returnList = new ArrayList<>();

        for (ReviewResult reviewResult : reviewResponse.getResults()) {
            Review video = new Review(
                    reviewResult.getAuthor(),
                    reviewResult.getContent());
            returnList.add(video);
        }

        return returnList;
    }
}
