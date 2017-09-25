package br.com.felipeacerbi.popularmovies.app;

import android.app.Application;

import br.com.felipeacerbi.popularmovies.dagger.AppModule;
import br.com.felipeacerbi.popularmovies.dagger.DaggerMoviesComponent;
import br.com.felipeacerbi.popularmovies.dagger.MoviesComponent;

public class MoviesApplication extends Application {

    private MoviesComponent moviesComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        moviesComponent = DaggerMoviesComponent
                .builder()
                .appModule(new AppModule(this))
                .build();
    }

    public MoviesComponent getMoviesComponent() {
        return moviesComponent;
    }
}
