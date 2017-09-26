package br.com.felipeacerbi.popularmovies.room;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import br.com.felipeacerbi.popularmovies.models.Movie;

// Not used for now
@Dao
public interface FavoriteDAO {

    @Query("SELECT * FROM " + Movie.TABLE_NAME)
    LiveData<List<Movie>> getFavorites();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long addFavorite(Movie movie);

    @Delete
    int removeFavorite(Movie movie);

}
