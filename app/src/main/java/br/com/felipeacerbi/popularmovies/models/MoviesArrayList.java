package br.com.felipeacerbi.popularmovies.models;

import android.os.Bundle;
import android.support.annotation.NonNull;

import org.parceler.Parcel;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Collection;

import icepick.Bundler;

@Parcel
public class MoviesArrayList extends ArrayList<Movie> implements Bundler<ArrayList<Movie>> {

    public MoviesArrayList() {
    }

    public MoviesArrayList(@NonNull Collection<? extends Movie> c) {
        super(c);
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
