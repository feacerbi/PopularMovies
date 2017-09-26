package br.com.felipeacerbi.popularmovies.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import br.com.felipeacerbi.popularmovies.sqlite.FavoriteMoviesContract.*;

public class MoviesDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "movies.db";
    private static final int DATABASE_VERSION = 1;

    public MoviesDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_FAVORITE_MOVIES_TABLE = "CREATE TABLE " + MovieEntry.TABLE_NAME + " (" +
                MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                MovieEntry.COLUMN_ID + " INTEGER NOT NULL, " +
                MovieEntry.COLUMN_TITLE + " STRING NOT NULL, " +
                MovieEntry.COLUMN_ORIGINAL_TITLE + " STRING NOT NULL, " +
                MovieEntry.COLUMN_THUMB_PATH + " STRING NOT NULL, " +
                MovieEntry.COLUMN_OVERVIEW + " STRING NOT NULL, " +
                MovieEntry.COLUMN_RELEASE_DATE + " STRING NOT NULL, " +
                MovieEntry.COLUMN_POPULARITY + " FLOAT NOT NULL, " +
                MovieEntry.COLUMN_RATING + " FLOAT NOT NULL, " +
                MovieEntry.COLUMN_VOTE_COUNT + " STRING NOT NULL, " +
                MovieEntry.COLUMN_FAVORITE + " BOOL NOT NULL " +
                "); ";

        sqLiteDatabase.execSQL(SQL_CREATE_FAVORITE_MOVIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
