package br.com.felipeacerbi.popularmovies.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;

import org.parceler.Parcel;
import org.parceler.Parcels;

import br.com.felipeacerbi.popularmovies.sqlite.FavoriteMoviesContract;
import icepick.Bundler;

@Parcel
@Entity(tableName = Movie.TABLE_NAME)
@SuppressWarnings({"UnusedDeclaration"})
public class Movie implements Bundler<Movie> {

    public static final String TABLE_NAME = "favorite_movies";
    public static final String COLUMN_ID = "id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_ORIGINAL_TITLE = "original_title";
    private static final String COLUMN_THUMB_PATH = "thumb_path";
    private static final String COLUMN_OVERVIEW = "overview";
    private static final String COLUMN_RELEASE_DATE = "release_date";
    private static final String COLUMN_POPULARITY = "popularity";
    private static final String COLUMN_RATING = "rating";
    private static final String COLUMN_VOTE_COUNT = "vote_count";
    private static final String COLUMN_FAVORITE = "favorite";


    @PrimaryKey
    @ColumnInfo(index = true, name = COLUMN_ID)
    int id;
    @ColumnInfo(name = COLUMN_TITLE)
    String title;
    @ColumnInfo(name = COLUMN_ORIGINAL_TITLE)
    String originalTitle;
    @ColumnInfo(name = COLUMN_THUMB_PATH)
    String thumbPath;
    @ColumnInfo(name = COLUMN_OVERVIEW)
    String overview;
    @ColumnInfo(name = COLUMN_RELEASE_DATE)
    String releaseDate;
    @ColumnInfo(name = COLUMN_POPULARITY)
    float popularity;
    @ColumnInfo(name = COLUMN_RATING)
    float rating;
    @ColumnInfo(name = COLUMN_VOTE_COUNT)
    int voteCount;
    @ColumnInfo(name = COLUMN_FAVORITE)
    boolean favorite;

    @Ignore
    public Movie() {
    }

    public Movie(int id, String title, String originalTitle, String thumbPath, String overview, String releaseDate, float popularity, float rating, int voteCount, boolean favorite) {
        this.id = id;
        this.title = title;
        this.originalTitle = originalTitle;
        this.thumbPath = thumbPath;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.popularity = popularity;
        this.rating = rating;
        this.voteCount = voteCount;
        this.favorite = favorite;
    }

    public static Movie fromContentValues(ContentValues values) {
        Movie movie = new Movie();

        movie.setId(values.getAsInteger(COLUMN_ID));
        movie.setTitle(values.getAsString(COLUMN_TITLE));

        if(values.containsKey(COLUMN_ORIGINAL_TITLE)) {
            movie.setOriginalTitle(values.getAsString(COLUMN_ORIGINAL_TITLE));
        }
        if(values.containsKey(COLUMN_THUMB_PATH)) {
            movie.setThumbPath(values.getAsString(COLUMN_THUMB_PATH));
        }
        if(values.containsKey(COLUMN_OVERVIEW)) {
            movie.setOverview(values.getAsString(COLUMN_OVERVIEW));
        }
        if(values.containsKey(COLUMN_RELEASE_DATE)) {
            movie.setReleaseDate(values.getAsString(COLUMN_RELEASE_DATE));
        }
        if(values.containsKey(COLUMN_POPULARITY)) {
            movie.setPopularity(values.getAsInteger(COLUMN_POPULARITY));
        }
        if(values.containsKey(COLUMN_RATING)) {
            movie.setRating(values.getAsInteger(COLUMN_RATING));
        }
        if(values.containsKey(COLUMN_VOTE_COUNT)) {
            movie.setVoteCount(values.getAsInteger(COLUMN_VOTE_COUNT));
        }
        if(values.containsKey(COLUMN_FAVORITE)) {
            movie.setFavorite(values.getAsBoolean(COLUMN_FAVORITE));
        }

        return movie;
    }

    public static Movie fromCursor(Cursor cursor) {
        return new Movie(
                cursor.getInt(cursor.getColumnIndex(FavoriteMoviesContract.MovieEntry.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(FavoriteMoviesContract.MovieEntry.COLUMN_TITLE)),
                cursor.getString(cursor.getColumnIndex(FavoriteMoviesContract.MovieEntry.COLUMN_ORIGINAL_TITLE)),
                cursor.getString(cursor.getColumnIndex(FavoriteMoviesContract.MovieEntry.COLUMN_THUMB_PATH)),
                cursor.getString(cursor.getColumnIndex(FavoriteMoviesContract.MovieEntry.COLUMN_OVERVIEW)),
                cursor.getString(cursor.getColumnIndex(FavoriteMoviesContract.MovieEntry.COLUMN_RELEASE_DATE)),
                cursor.getFloat(cursor.getColumnIndex(FavoriteMoviesContract.MovieEntry.COLUMN_POPULARITY)),
                cursor.getFloat(cursor.getColumnIndex(FavoriteMoviesContract.MovieEntry.COLUMN_RATING)),
                cursor.getInt(cursor.getColumnIndex(FavoriteMoviesContract.MovieEntry.COLUMN_VOTE_COUNT)),
                (cursor.getInt(cursor.getColumnIndex(FavoriteMoviesContract.MovieEntry.COLUMN_FAVORITE)) == 1)
        );
    }

    public ContentValues toContentValues() {
        ContentValues contentValues = new ContentValues();

        contentValues.put(FavoriteMoviesContract.MovieEntry.COLUMN_ID, getId());
        contentValues.put(FavoriteMoviesContract.MovieEntry.COLUMN_TITLE, getTitle());
        contentValues.put(FavoriteMoviesContract.MovieEntry.COLUMN_ORIGINAL_TITLE, getOriginalTitle());
        contentValues.put(FavoriteMoviesContract.MovieEntry.COLUMN_THUMB_PATH, getThumbPath());
        contentValues.put(FavoriteMoviesContract.MovieEntry.COLUMN_OVERVIEW, getOverview());
        contentValues.put(FavoriteMoviesContract.MovieEntry.COLUMN_RELEASE_DATE, getReleaseDate());
        contentValues.put(FavoriteMoviesContract.MovieEntry.COLUMN_POPULARITY, getPopularity());
        contentValues.put(FavoriteMoviesContract.MovieEntry.COLUMN_RATING, getRating());
        contentValues.put(FavoriteMoviesContract.MovieEntry.COLUMN_VOTE_COUNT, getVoteCount());
        contentValues.put(FavoriteMoviesContract.MovieEntry.COLUMN_FAVORITE, isFavorite() ? 1 : 0);

        return contentValues;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getThumbPath() {
        return thumbPath;
    }

    public void setThumbPath(String thumbPath) {
        this.thumbPath = thumbPath;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public float getPopularity() {
        return popularity;
    }

    public void setPopularity(float popularity) {
        this.popularity = popularity;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public float getStarRating() {
        return getRating() / 2;
    }

    @Override
    public void put(String key, Movie movie, Bundle bundle) {
        bundle.putParcelable(key, Parcels.wrap(movie));
    }

    @Override
    public Movie get(String key, Bundle bundle) {
        return Parcels.unwrap(bundle.getParcelable(key));
    }

    @Override
    public boolean equals(Object movie) {
        return movie instanceof Movie && getId() == ((Movie) movie).getId();
    }
}
