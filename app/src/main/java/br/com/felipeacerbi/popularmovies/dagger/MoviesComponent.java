package br.com.felipeacerbi.popularmovies.dagger;

import javax.inject.Singleton;

import br.com.felipeacerbi.popularmovies.activities.MainActivity;
import dagger.Component;

@Singleton
@Component(modules = {
        MoviesModule.class,
        RetrofitModule.class})

public interface MoviesComponent {
    void inject(MainActivity activity);
}
