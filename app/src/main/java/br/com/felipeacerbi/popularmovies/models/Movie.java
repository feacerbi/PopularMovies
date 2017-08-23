package br.com.felipeacerbi.popularmovies.models;

import android.os.Bundle;

import org.parceler.Parcel;
import org.parceler.Parcels;

import icepick.Bundler;

@Parcel
@SuppressWarnings({"UnusedDeclaration"})
public class Movie implements Bundler<Movie> {

    int id;
    String title;
    String thumbPath;
    String overview;
    String releaseDate;
    float popularity;
    float rating;

    public Movie() {
    }

    public Movie(int id, String title, String thumbPath, String overview, String releaseDate, float popularity, float rating) {
        this.id = id;
        this.title = title;
        this.thumbPath = thumbPath;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.popularity = popularity;
        this.rating = rating;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getPopularity() {
        return popularity;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    @Override
    public void put(String key, Movie movie, Bundle bundle) {
        bundle.putParcelable(key, Parcels.wrap(movie));
    }

    @Override
    public Movie get(String key, Bundle bundle) {
        return Parcels.unwrap(bundle.getParcelable(key));
    }
}
