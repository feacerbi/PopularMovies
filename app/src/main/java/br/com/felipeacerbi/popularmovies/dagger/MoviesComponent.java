package br.com.felipeacerbi.popularmovies.dagger;

import javax.inject.Singleton;

import br.com.felipeacerbi.popularmovies.activities.MainActivity;
import br.com.felipeacerbi.popularmovies.activities.MovieDetailsActivity;
import dagger.Component;

@Singleton
@Component(modules = {
        AppModule.class,
        MoviesModule.class,
        RetrofitModule.class,
        RoomModule.class})

public interface MoviesComponent {
    void injectMain(MainActivity activity);
    void injectDetails(MovieDetailsActivity activity);
}
