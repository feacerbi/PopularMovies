package br.com.felipeacerbi.popularmovies.sqlite;

import android.net.Uri;
import android.provider.BaseColumns;

import br.com.felipeacerbi.popularmovies.models.Movie;

public final class FavoriteMoviesContract {

    public static final String AUTHORITY = "br.com.felipeacerbi.popularmovies";
    private static final String SCHEME = "content://";
    private static final Uri BASE_CONTENT_URI = Uri.parse(SCHEME + AUTHORITY);

    private static final String PATH_FAVORITE_MOVIES = Movie.TABLE_NAME;

    public FavoriteMoviesContract() {
    }

    public static class MovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITE_MOVIES).build();

        public static final String TABLE_NAME = "favorite_movies";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_ORIGINAL_TITLE = "original_title";
        public static final String COLUMN_THUMB_PATH = "thumb_path";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_POPULARITY = "popularity";
        public static final String COLUMN_RATING = "rating";
        public static final String COLUMN_VOTE_COUNT = "vote_count";
        public static final String COLUMN_FAVORITE = "favorite";
    }

}
