package br.com.felipeacerbi.popularmovies.activities;

import android.app.ActivityOptions;
import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import br.com.felipeacerbi.popularmovies.R;
import br.com.felipeacerbi.popularmovies.adapters.MoviesAdapter;
import br.com.felipeacerbi.popularmovies.adapters.listeners.IMovieClickListener;
import br.com.felipeacerbi.popularmovies.app.MoviesApplication;
import br.com.felipeacerbi.popularmovies.models.Movie;
import br.com.felipeacerbi.popularmovies.models.MoviesArrayList;
import br.com.felipeacerbi.popularmovies.repository.DataManager;
import br.com.felipeacerbi.popularmovies.retrofit.models.MoviesFilter;
import br.com.felipeacerbi.popularmovies.utils.InfiniteScrollListener;
import br.com.felipeacerbi.popularmovies.utils.RequestCallback;
import br.com.felipeacerbi.popularmovies.utils.ScrollLoadingCallback;
import butterknife.BindView;
import butterknife.ButterKnife;
import icepick.Icepick;
import icepick.State;

public class MainActivity extends AppCompatActivity implements LifecycleRegistryOwner, IMovieClickListener, OnRefreshListener {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.list) RecyclerView moviesList;
    @BindView(R.id.swipe_refresh) SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.no_movies) TextView noMovies;

    @Inject
    DataManager dataManager;

    @State() int currentFilter;
    @State(MoviesArrayList.class) MoviesArrayList loadedMovies;

    private static final String TAG = MainActivity.class.getName();
    private static final String POSTER_TRANSITION = "poster";
    private static final String FILTER_PREFERENCE = "filter";

    private final LifecycleRegistry registry = new LifecycleRegistry(this);
    private SharedPreferences preferences;

    private MoviesAdapter adapter;
    private InfiniteScrollListener infiniteScrollListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Icepick.restoreInstanceState(this, savedInstanceState);

        ((MoviesApplication) getApplication()).getMoviesComponent().injectMain(this);

        setUpUI();
    }

    private void setUpUI() {
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            swipeRefresh.setColorSchemeColors(getResources().getColor(R.color.colorPrimary, getTheme()));
        } else {
            swipeRefresh.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        }
        swipeRefresh.setOnRefreshListener(this);

        adapter = new MoviesAdapter(this, new ArrayList<Movie>());
        moviesList.setAdapter(adapter);

        infiniteScrollListener = createInfiniteScrollListener();
        moviesList.addOnScrollListener(infiniteScrollListener);

        preferences = getPreferences(MODE_PRIVATE);
        currentFilter = preferences.getInt(FILTER_PREFERENCE, MoviesFilter.MOST_POPULAR);

        if(loadedMovies != null) {
            setUpAdapter();
        } else {
            loadedMovies = new MoviesArrayList();
            refreshMovies();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(currentFilter == MoviesFilter.FAVORITES) {
            loadFavorites();
        } else {
            syncFavorites();
        }
    }

    private void refreshMovies() {
        if(currentFilter == MoviesFilter.FAVORITES) {
            loadFavorites();
        } else {
            loadMovies();
        }
    }

    private void loadMovies() {
        swipeRefresh.setRefreshing(true);
        dataManager.requestMovies(currentFilter, loadedMovies.getCurrentPage(),  new RequestCallback<List<Movie>>() {
            @Override
            public void onSuccess(List<Movie> movies) {
                loadedMovies.addItems(movies);
                syncFavorites();
            }

            @Override
            public void onError(String error) {
                Log.e(TAG, "Error loading movies " + error);
                onErrorUI();
            }
        });
    }

    private void loadFavorites() {
        swipeRefresh.setRefreshing(true);
        dataManager.requestFavorites(this, new RequestCallback<List<Movie>>() {
            @Override
            public void onSuccess(List<Movie> favorites) {
                loadedMovies.clear();
                loadedMovies.addItems(favorites);
                setUpAdapter();
            }

            @Override
            public void onError(String error) {
                Log.e(TAG, "Error accessing database " + error);
                onErrorUI();
            }
        });
    }

    private void onErrorUI() {
        swipeRefresh.setRefreshing(false);

        if(loadedMovies.isEmpty()) {
            noMovies.setVisibility(View.VISIBLE);
            moviesList.setVisibility(View.GONE);
        }

        Snackbar.make(moviesList, R.string.fail_movie_request_message, Snackbar.LENGTH_LONG)
                .setAction(R.string.snackbar_retry_action, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        refreshMovies();
                    }
                })
                .show();
    }

    private void syncFavorites() {
        dataManager.requestFavorites(this, new RequestCallback<List<Movie>>() {
            @Override
            public void onSuccess(List<Movie> favorites) {
                for (Movie movie : loadedMovies) {
                    movie.setFavorite(favorites.contains(movie));
                }
                setUpAdapter();
            }

            @Override
            public void onError(String error) {
                Log.e(TAG, "Error syncing favorites " + error);
                onErrorUI();
            }
        });
    }

    private void setUpAdapter() {
        adapter.setItems(loadedMovies);

        boolean hasItems = !loadedMovies.isEmpty();

        noMovies.setVisibility((hasItems) ? View.GONE : View.VISIBLE);
        moviesList.setVisibility((hasItems) ? View.VISIBLE : View.GONE);

        swipeRefresh.setRefreshing(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_sort:
                showFilterDialog();
                return true;
            case R.id.action_refresh:
                onRefresh();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showFilterDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.movies_list_filter_title)
                .setSingleChoiceItems(
                        R.array.movies_list_filter,
                        currentFilter,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int position) {
                                currentFilter = position;
                                preferences.edit().putInt(FILTER_PREFERENCE, currentFilter).apply();

                                loadedMovies.setCurrentPage(1);
                                infiniteScrollListener.reset();

                                refreshMovies();
                                dialogInterface.dismiss();
                            }
                        })
                .setNegativeButton(R.string.cancel_button_title, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .show();
    }

    private InfiniteScrollListener createInfiniteScrollListener() {
        return new InfiniteScrollListener(new ScrollLoadingCallback() {
            @Override
            public void onLoading() {
                loadedMovies.nextPage();
                refreshMovies();
            }
        }, (GridLayoutManager) moviesList.getLayoutManager());
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void onItemClick(Movie movie, ImageView posterView) {
        Intent intent = new Intent(this, MovieDetailsActivity.class);
        intent.putExtra(MovieDetailsActivity.MOVIE_EXTRA, Parcels.wrap(movie));
        intent.putExtra(MovieDetailsActivity.FILTER_EXTRA, currentFilter);

        ActivityOptions options = ActivityOptions
                .makeSceneTransitionAnimation(this, posterView, POSTER_TRANSITION);

        startActivity(intent, options.toBundle());
    }

    @Override
    public void onRefresh() {
        moviesList.smoothScrollToPosition(0);
        refreshMovies();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    @Override
    public LifecycleRegistry getLifecycle() {
        return registry;
    }
}
