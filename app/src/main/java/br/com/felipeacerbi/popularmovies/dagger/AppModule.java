package br.com.felipeacerbi.popularmovies.dagger;

import android.content.Context;

import javax.inject.Singleton;

import br.com.felipeacerbi.popularmovies.app.MoviesApplication;
import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    private MoviesApplication application;

    public AppModule(MoviesApplication application) {
        this.application = application;
    }

    @Provides
    @Singleton
    Context provideContext() {
        return application;
    }

}
