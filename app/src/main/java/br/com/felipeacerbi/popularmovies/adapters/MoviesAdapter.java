package br.com.felipeacerbi.popularmovies.adapters;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import br.com.felipeacerbi.popularmovies.R;
import br.com.felipeacerbi.popularmovies.adapters.listeners.IMovieClickListener;
import br.com.felipeacerbi.popularmovies.models.Movie;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MoviesAdapter extends Adapter<RecyclerView.ViewHolder> {

    private IMovieClickListener listener;
    private List<Movie> moviesList = new ArrayList<>();

    public MoviesAdapter(IMovieClickListener listener, List<Movie> moviesList) {
        this.listener = listener;
        this.moviesList.addAll(moviesList);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_grid_item, parent, false);
        return new MovieViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Movie movie = moviesList.get(position);
        final MovieViewHolder movieViewHolder = (MovieViewHolder) holder;

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDetails(movie, movieViewHolder.thumb);
            }
        });

        movieViewHolder.icon.setVisibility((movie.isFavorite()) ? View.VISIBLE : View.INVISIBLE);

        Picasso.with(listener.getContext())
                .load(listener.getContext().getString(R.string.api_images_base_path) + movie.getThumbPath())
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .fit()
                .centerCrop()
                .into(movieViewHolder.thumb);
    }

    private void openDetails(Movie movie, ImageView posterView) {
        listener.onItemClick(movie, posterView);
    }

    public void setItems(List<Movie> items) {
        moviesList = items;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.movie_thumb) ImageView thumb;
        @BindView(R.id.favorite_icon) ConstraintLayout icon;

        MovieViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
