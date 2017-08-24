package br.com.felipeacerbi.popularmovies.dagger;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

@Module
class RetrofitModule {

    private static final String RETROFIT_BASE_URL = "https://api.themoviedb.org/3/";

    @Provides
    @Singleton
    Retrofit provideRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(RETROFIT_BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .build();
    }

}
