package br.com.felipeacerbi.popularmovies.room;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.database.Cursor;

import java.util.List;

import br.com.felipeacerbi.popularmovies.models.Movie;

@Dao
public interface FavoriteDAO {

    @Query("SELECT * FROM " + Movie.TABLE_NAME)
    LiveData<List<Movie>> getFavorites();

    @Query("SELECT * FROM " + Movie.TABLE_NAME)
    Cursor getProviderFavorites();

    @Query("SELECT * FROM " + Movie.TABLE_NAME + " WHERE " + Movie.COLUMN_ID + " = :id")
    Cursor getProviderFavorite(int id);

    @Query("DELETE FROM " + Movie.TABLE_NAME + " WHERE " + Movie.COLUMN_ID + " = :id")
    int removeProviderFavorite(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long addFavorite(Movie movie);

    @Delete
    int removeFavorite(Movie movie);

    @Update
    int updateFavorite(Movie movie);

}
