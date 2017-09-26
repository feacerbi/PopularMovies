package br.com.felipeacerbi.popularmovies.repository;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;

import br.com.felipeacerbi.popularmovies.models.Movie;
import br.com.felipeacerbi.popularmovies.sqlite.FavoriteMoviesContract;
import br.com.felipeacerbi.popularmovies.sqlite.FavoriteMoviesContract.MovieEntry;
import br.com.felipeacerbi.popularmovies.sqlite.MoviesDBHelper;

public class ContentProviderDataSource extends ContentProvider {

    private static final int FAVORITES = 100;
    private static final int FAVORITE_WITH_ID = 101;

    private static final UriMatcher matcher = createUriMatcher();

    private MoviesDBHelper moviesDBHelper;

    @Override
    public boolean onCreate() {

        moviesDBHelper = new MoviesDBHelper(getContext());

        return true;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        int match = matcher.match(uri);

        SQLiteDatabase database = moviesDBHelper.getWritableDatabase();

        int deletes;

        switch (match) {
            case FAVORITE_WITH_ID:
                deletes = database.delete(MovieEntry.TABLE_NAME, MovieEntry.COLUMN_ID + "=?", new String[]{String.valueOf(ContentUris.parseId(uri))});
                break;
            default:
                throw new UnsupportedOperationException("Not supported Uri: " + uri);
        }

        Context context = getContext();

        if(deletes > 0 && context != null) {
            context.getContentResolver().notifyChange(uri, null);
        }

        return deletes;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        int match = matcher.match(uri);

        switch (match) {
            case FAVORITES:
                return "vnd.android.cursor.dir/" + FavoriteMoviesContract.AUTHORITY + "." + MovieEntry.TABLE_NAME;
            case FAVORITE_WITH_ID:
                return "vnd.android.cursor.item/" + FavoriteMoviesContract.AUTHORITY + "." + MovieEntry.TABLE_NAME;
            default:
                throw new UnsupportedOperationException("Not supported Uri: " + uri);
        }
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        int match = matcher.match(uri);
        Uri returnUri;

        SQLiteDatabase database = moviesDBHelper.getWritableDatabase();

        switch (match) {
            case FAVORITES:
                long id = database.insert(MovieEntry.TABLE_NAME, null, values);

                if(id > 0) {
                    returnUri = ContentUris.withAppendedId(MovieEntry.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Not supported Uri: " + uri);
        }

        Context context = getContext();

        if(context != null) {
            context.getContentResolver().notifyChange(uri, null);
        }

        return returnUri;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        int match = matcher.match(uri);

        SQLiteDatabase database = moviesDBHelper.getReadableDatabase();

        Cursor cursor;

        switch (match) {
            case FAVORITES:
                cursor = database.query(
                        MovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Not supported Uri: " + uri);
        }

        Context context = getContext();

        if(context != null) {
            cursor.setNotificationUri(context.getContentResolver(), uri);
        }

        return cursor;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int match = matcher.match(uri);

        SQLiteDatabase database = moviesDBHelper.getWritableDatabase();

        int updates;

        switch (match) {
            case FAVORITE_WITH_ID:
                updates = database.update(MovieEntry.TABLE_NAME, values, MovieEntry.COLUMN_ID + "=?", new String[]{String.valueOf(ContentUris.parseId(uri))});
                break;
            default:
                throw new UnsupportedOperationException("Not supported Uri: " + uri);
        }

        Context context = getContext();

        if(updates > 0 && context != null) {
            context.getContentResolver().notifyChange(uri, null);
        }

        return updates;
    }

    private static UriMatcher createUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(FavoriteMoviesContract.AUTHORITY, Movie.TABLE_NAME, FAVORITES);
        uriMatcher.addURI(FavoriteMoviesContract.AUTHORITY, Movie.TABLE_NAME + "/*", FAVORITE_WITH_ID);

        return uriMatcher;
    }
}
