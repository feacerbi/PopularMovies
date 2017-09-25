package br.com.felipeacerbi.popularmovies.models;

import android.os.Bundle;
import android.support.annotation.NonNull;

import org.parceler.Parcel;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import icepick.Bundler;

@Parcel
public class MoviesArrayList extends ArrayList<Movie> implements Bundler<ArrayList<Movie>> {

    private int currentPage = 1;

    public MoviesArrayList() {
    }

    public MoviesArrayList(@NonNull Collection<? extends Movie> c) {
        super(c);
    }

    public void addItems(List<Movie> movies) {
        if(currentPage == 1) {
            clear();
        }

        addAll(movies);
    }

    public void nextPage() {
        currentPage++;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    @Override
    public void put(String key, ArrayList<Movie> movies, Bundle bundle) {
        bundle.putParcelable(key, Parcels.wrap(movies));
    }

    @Override
    public ArrayList<Movie> get(String key, Bundle bundle) {
        return Parcels.unwrap(bundle.getParcelable(key));
    }
}
