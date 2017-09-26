package br.com.felipeacerbi.popularmovies.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import br.com.felipeacerbi.popularmovies.models.Movie;

// Not used for now
@Database(entities = {Movie.class}, version = 1, exportSchema = false)
public abstract class FavoritesDatabase extends RoomDatabase {

    public abstract FavoriteDAO favoriteDAO();

}
