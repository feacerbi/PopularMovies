package br.com.felipeacerbi.popularmovies.dagger;

import javax.inject.Singleton;

import br.com.felipeacerbi.popularmovies.retrofit.MoviesApiService;
import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
class MoviesModule {

    @Provides
    @Singleton
    MoviesApiService provideMoviesApiService(Retrofit retrofit) {
        return retrofit.create(MoviesApiService.class);
    }

}
