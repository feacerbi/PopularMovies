package br.com.felipeacerbi.popularmovies.activities;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import org.parceler.Parcels;

import java.util.List;

import javax.inject.Inject;

import br.com.felipeacerbi.popularmovies.R;
import br.com.felipeacerbi.popularmovies.activities.base.RxBaseActivity;
import br.com.felipeacerbi.popularmovies.adapters.MoviesAdapter;
import br.com.felipeacerbi.popularmovies.adapters.listeners.IMovieClickListener;
import br.com.felipeacerbi.popularmovies.app.MoviesApplication;
import br.com.felipeacerbi.popularmovies.models.Movie;
import br.com.felipeacerbi.popularmovies.models.MoviesArrayList;
import br.com.felipeacerbi.popularmovies.utils.MoviesManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import icepick.Icepick;
import icepick.State;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends RxBaseActivity implements IMovieClickListener, OnRefreshListener {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.list) RecyclerView moviesList;
    @BindView(R.id.swipe_refresh) SwipeRefreshLayout swipeRefresh;

    @Inject MoviesManager moviesManager;

    @State int currentFilter = MoviesManager.MOST_POPULAR;
    @State(MoviesArrayList.class) MoviesArrayList loadedMovies;
    MoviesAdapter adapter;

    public static final String POSTER_TRANSITION = "poster";
    private int tempFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Icepick.restoreInstanceState(this, savedInstanceState);

        ((MoviesApplication) getApplication()).getMoviesComponent().inject(this);
        setUpUI();
    }

    private void setUpUI() {
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        swipeRefresh.setOnRefreshListener(this);

        if(loadedMovies != null) {
            setUpAdapter(loadedMovies);
        } else {
            swipeRefresh.setRefreshing(true);
            requestMovies(currentFilter);
        }
    }

    private void requestMovies(int filter) {
        Observable<List<Movie>> subscription = moviesManager.getMovies(this, filter)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        subscription.subscribe(createMoviesObserver());
    }

    private Observer<List<Movie>> createMoviesObserver() {
        return new Observer<List<Movie>>() {
            @Override
            public void onSubscribe(Disposable d) {}

            @Override
            public void onNext(List<Movie> movies) {
                setUpAdapter(movies);
            }

            @Override
            public void onError(Throwable e) { swipeRefresh.setRefreshing(false); }

            @Override
            public void onComplete() {}
        };
    }

    private void setUpAdapter(List<Movie> movies) {
        adapter = new MoviesAdapter(this, movies);
        moviesList.setLayoutManager(new GridLayoutManager(this, 2));
        moviesList.setAdapter(adapter);
        loadedMovies = new MoviesArrayList(movies);

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
            case R.id.action_settings:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showFilterDialog() {
        tempFilter = currentFilter;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.movies_list_filter_title)
                .setSingleChoiceItems(
                        R.array.movies_list_filter,
                        currentFilter,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int position) {
                                tempFilter = (position == 0) ?
                                        MoviesManager.MOST_POPULAR : MoviesManager.TOP_RATED ;
                            }
                        })
                .setPositiveButton(R.string.apply_button_title, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        currentFilter = tempFilter;
                        swipeRefresh.setRefreshing(true);
                        requestMovies(currentFilter);
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

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void onItemClick(Movie movie, ImageView posterView) {
        Intent intent = new Intent(this, MovieDetailsActivity.class);
        intent.putExtra(MovieDetailsActivity.MOVIE_EXTRA, Parcels.wrap(movie));

        ActivityOptions options = ActivityOptions
                .makeSceneTransitionAnimation(this, posterView, POSTER_TRANSITION);

        startActivity(intent, options.toBundle());
    }

    @Override
    public void onRefresh() {
        requestMovies(currentFilter);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }
}
