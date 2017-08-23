package br.com.felipeacerbi.popularmovies.app;

import android.app.Application;

import br.com.felipeacerbi.popularmovies.dagger.DaggerMoviesComponent;
import br.com.felipeacerbi.popularmovies.dagger.MoviesComponent;

public class MoviesApplication extends Application {

    public MoviesComponent moviesComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        moviesComponent = DaggerMoviesComponent.builder().build();
    }

    public MoviesComponent getMoviesComponent() {
        return moviesComponent;
    }
}
