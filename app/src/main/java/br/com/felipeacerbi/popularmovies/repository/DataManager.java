package br.com.felipeacerbi.popularmovies.repository;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.support.annotation.Nullable;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import br.com.felipeacerbi.popularmovies.models.Movie;
import br.com.felipeacerbi.popularmovies.models.Review;
import br.com.felipeacerbi.popularmovies.models.Video;
import br.com.felipeacerbi.popularmovies.room.FavoritesDatabase;
import br.com.felipeacerbi.popularmovies.utils.RequestCallback;
import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

@Singleton
public class DataManager {

    @Inject
    RestDataSource restDataSource;

    @Inject
    FavoritesDatabase favoritesDatabase;

    @Inject
    DataManager() {}

    public void requestAddFavorite(Movie movie, final RequestCallback<String> addFavoriteRequest) {
        addFavorite(movie)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(createCompletableObserver(addFavoriteRequest));
    }

    public void requestRemoveFavorite(Movie movie, final RequestCallback<String> removeFavoriteRequest) {
        removeFavorite(movie)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(createCompletableObserver(removeFavoriteRequest));
    }

    public void requestFavorites(LifecycleOwner owner, final RequestCallback<List<Movie>> favoritesRequest) {
        favoritesDatabase.favoriteDAO().getFavorites().observe(owner, new android.arch.lifecycle.Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> favorites) {
                    favoritesRequest.onSuccess(favorites);
            }
        });
    }

    public void requestMovies(int filter, int page, final RequestCallback<List<Movie>> moviesRequest) {
            getMovies(filter, page)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(createObserver(moviesRequest));
    }

    public void requestTrailers(int movieId, RequestCallback<List<Video>> trailersRequest) {
        getTrailers(movieId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(createObserver(trailersRequest));
    }

    public void requestReviews(int movieId, RequestCallback<List<Review>> reviewsRequest) {
        getReviews(movieId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(createObserver(reviewsRequest));
    }

    private <T> Observer<T> createObserver(final RequestCallback<T> request) {
        return new Observer<T>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {}

            @Override
            public void onNext(@NonNull T object) { request.onSuccess(object); }

            @Override
            public void onError(@NonNull Throwable e) { request.onError(e.getMessage()); }

            @Override
            public void onComplete() {}
        };
    }

    private CompletableObserver createCompletableObserver(final RequestCallback<String> request) {
        return new CompletableObserver() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {}

            @Override
            public void onError(@NonNull Throwable e) { request.onError(e.getMessage()); }

            @Override
            public void onComplete() { request.onSuccess("success"); }
        };
    }

    private Completable addFavorite(final Movie movie) {
        return Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                favoritesDatabase.favoriteDAO().addFavorite(movie);
            }
        });
    }

    private Completable removeFavorite(final Movie movie) {
        return Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                favoritesDatabase.favoriteDAO().removeFavorite(movie);
            }
        });
    }

    private Observable<List<Movie>> getMovies(final int filter, final int page) {
        return Observable.create(new ObservableOnSubscribe<List<Movie>>() {
            @Override
            public void subscribe(final ObservableEmitter<List<Movie>> subscriber) throws Exception {
                restDataSource.getMovies(filter, page, new RequestCallback<List<Movie>>() {
                    @Override
                    public void onSuccess(List<Movie> movies) {
                        subscriber.onNext(movies);
                        subscriber.onComplete();
                    }

                    @Override
                    public void onError(String error) {
                        subscriber.onError(new Throwable(error));
                    }
                });
            }
        });
    }

    private Observable<List<Video>> getTrailers(final int movieId) {
        return Observable.create(new ObservableOnSubscribe<List<Video>>() {
            @Override
            public void subscribe(final ObservableEmitter<List<Video>> subscriber) throws Exception {
                restDataSource.getVideos(movieId, new RequestCallback<List<Video>>() {
                    @Override
                    public void onSuccess(List<Video> videos) {
                        subscriber.onNext(videos);
                        subscriber.onComplete();
                    }

                    @Override
                    public void onError(String error) {
                        subscriber.onError(new Throwable(error));
                    }
                });
            }
        });
    }

    private Observable<List<Review>> getReviews(final int movieId) {
        return Observable.create(new ObservableOnSubscribe<List<Review>>() {
            @Override
            public void subscribe(final ObservableEmitter<List<Review>> subscriber) throws Exception {
                restDataSource.getReviews(movieId, new RequestCallback<List<Review>>() {
                    @Override
                    public void onSuccess(List<Review> reviews) {
                        subscriber.onNext(reviews);
                        subscriber.onComplete();
                    }

                    @Override
                    public void onError(String error) {
                        subscriber.onError(new Throwable(error));
                    }
                });
            }
        });
    }
}
