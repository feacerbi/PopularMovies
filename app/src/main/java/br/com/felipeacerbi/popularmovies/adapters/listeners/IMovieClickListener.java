package br.com.felipeacerbi.popularmovies.adapters.listeners;

import android.content.Context;
import android.widget.ImageView;

import br.com.felipeacerbi.popularmovies.models.Movie;

public interface IMovieClickListener {
    Context getContext();
    void onItemClick(Movie movie, ImageView posterView);
}
