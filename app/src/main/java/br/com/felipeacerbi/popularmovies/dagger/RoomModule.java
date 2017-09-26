package br.com.felipeacerbi.popularmovies.dagger;

import android.arch.persistence.room.Room;
import android.content.Context;

import javax.inject.Singleton;

import br.com.felipeacerbi.popularmovies.R;
import br.com.felipeacerbi.popularmovies.room.FavoritesDatabase;
import dagger.Module;
import dagger.Provides;

// Not used for now
@Module
class RoomModule {

    @Provides
    @Singleton
    FavoritesDatabase providesFavoritesDatabase(Context context) {
        return Room.databaseBuilder(
                context,
                FavoritesDatabase.class,
                context.getString(R.string.favorites_db_name))
                .build();
    }

}
