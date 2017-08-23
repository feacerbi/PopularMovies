package br.com.felipeacerbi.popularmovies.utils;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import br.com.felipeacerbi.popularmovies.models.Movie;
import br.com.felipeacerbi.popularmovies.retrofit.models.MovieRequestResponse;
import br.com.felipeacerbi.popularmovies.retrofit.models.MovieResult;
import br.com.felipeacerbi.popularmovies.retrofit.models.RestAPI;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Singleton
public class MoviesManager {

    @Inject
    RestAPI restAPI;

    @Inject
    public MoviesManager() {}

    public static final int MOST_POPULAR = 0;
    public static final int TOP_RATED = 1;

    public Observable<List<Movie>> getMovies(final Context context, final int filter) {
        return Observable.create(new ObservableOnSubscribe<List<Movie>>() {
            @Override
            public void subscribe(final ObservableEmitter<List<Movie>> subscriber) throws Exception {
                Call<MovieRequestResponse> movies = null;
                switch (filter) {
                    case MOST_POPULAR:
                        movies = restAPI.getMostPopularMovies(context);
                        break;
                    case TOP_RATED:
                        movies = restAPI.getTopRatedMovies(context);
                        break;
                    default:
                        subscriber.onError(new Throwable("Wrong filter"));
                }

                if (movies != null) {
                    movies.enqueue(new Callback<MovieRequestResponse>() {
                        @Override
                        public void onResponse(Call<MovieRequestResponse> call, Response<MovieRequestResponse> response) {
                            List<Movie> returnList = new ArrayList<>();

                            if(response.isSuccessful()) {
                                MovieRequestResponse movieResponse = response.body();

                                for (MovieResult movieResult : movieResponse.getResults()) {
                                    Movie movie = new Movie(
                                            movieResult.getId(),
                                            movieResult.getTitle(),
                                            movieResult.getPoster_path(),
                                            movieResult.getOverview(),
                                            movieResult.getRelease_date(),
                                            movieResult.getPopularity(),
                                            movieResult.getVote_average());
                                    returnList.add(movie);
                                }
                                subscriber.onNext(returnList);
                                subscriber.onComplete();
                            }
                        }

                        @Override
                        public void onFailure(Call<MovieRequestResponse> call, Throwable t) {
                            subscriber.onError(t);
                        }
                    });
                }
            }
        });
    }
}
