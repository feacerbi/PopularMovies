package br.com.felipeacerbi.popularmovies.repository;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;

import javax.inject.Inject;

import br.com.felipeacerbi.popularmovies.models.Movie;
import br.com.felipeacerbi.popularmovies.room.FavoritesDatabase;

public class ContentProviderDataSource extends ContentProvider {

    private static final int FAVORITES = 100;
    private static final int FAVORITE_WITH_ID = 101;

    private static final String AUTHORITY = "br.com.felipeacerbi.popularmovies";
    public static final String SCHEME = "content://";
    public static final Uri BASE_CONTENT_URI = Uri.parse(SCHEME + AUTHORITY);

    public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(Movie.TABLE_NAME).build();

    private static final UriMatcher matcher = createUriMatcher();

    @Inject
    FavoritesDatabase favoritesDatabase;

    @Override
    public boolean onCreate() {
        return true;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        int match = matcher.match(uri);

        switch (match) {
            case FAVORITE_WITH_ID:
                return favoritesDatabase.favoriteDAO().removeProviderFavorite((int) ContentUris.parseId(uri));
            default:
                throw new UnsupportedOperationException("Not supported Uri: " + uri);
        }
    }

    @Override
    public String getType(@NonNull Uri uri) {
        int match = matcher.match(uri);

        switch (match) {
            case FAVORITES:
                return "vnd.android.cursor.dir/" + AUTHORITY + "." + Movie.TABLE_NAME;
            case FAVORITE_WITH_ID:
                return "vnd.android.cursor.item/" + AUTHORITY + "." + Movie.TABLE_NAME;
            default:
                throw new UnsupportedOperationException("Not supported Uri: " + uri);
        }
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        int match = matcher.match(uri);

        switch (match) {
            case FAVORITES:
                long id = favoritesDatabase.favoriteDAO().addFavorite(Movie.fromContentValues(values));
                return ContentUris.withAppendedId(uri, id);
            default:
                throw new UnsupportedOperationException("Not supported Uri: " + uri);
        }
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        int match = matcher.match(uri);

        Cursor cursor;

        switch (match) {
            case FAVORITES:
                cursor = favoritesDatabase.favoriteDAO().getProviderFavorites();
                break;
            case FAVORITE_WITH_ID:
                cursor = favoritesDatabase.favoriteDAO().getProviderFavorite((int) ContentUris.parseId(uri));
                break;
            default:
                throw new UnsupportedOperationException("Not supported Uri: " + uri);
        }

        Context context = getContext();

        if(context != null) {
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
            return cursor;
        } else {
            return null;
        }
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int match = matcher.match(uri);

        switch (match) {
            case FAVORITE_WITH_ID:
                return favoritesDatabase.favoriteDAO().updateFavorite(Movie.fromContentValues(values));
            default:
                throw new UnsupportedOperationException("Not supported Uri: " + uri);
        }
    }

    private static UriMatcher createUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(AUTHORITY, Movie.TABLE_NAME, FAVORITES);
        uriMatcher.addURI(AUTHORITY, Movie.TABLE_NAME + "/*", FAVORITE_WITH_ID);

        return uriMatcher;
    }
}
