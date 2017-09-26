package br.com.felipeacerbi.popularmovies.activities;

import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import br.com.felipeacerbi.popularmovies.R;
import br.com.felipeacerbi.popularmovies.adapters.ReviewsAdapter;
import br.com.felipeacerbi.popularmovies.app.MoviesApplication;
import br.com.felipeacerbi.popularmovies.models.Movie;
import br.com.felipeacerbi.popularmovies.models.Review;
import br.com.felipeacerbi.popularmovies.models.Video;
import br.com.felipeacerbi.popularmovies.repository.DataManager;
import br.com.felipeacerbi.popularmovies.retrofit.models.MoviesFilter;
import br.com.felipeacerbi.popularmovies.utils.RequestCallback;
import butterknife.BindView;
import butterknife.ButterKnife;
import icepick.Icepick;
import icepick.State;

public class MovieDetailsActivity extends AppCompatActivity implements LifecycleRegistryOwner {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.fab) FloatingActionButton favoriteButton;
    @BindView(R.id.movie_poster) ImageView moviePosterView;
    @BindView(R.id.original) TextView originalTitle;
    @BindView(R.id.release) TextView releaseView;
    @BindView(R.id.rating_view) ConstraintLayout ratingView;
    @BindView(R.id.rating_bar) RatingBar ratingBar;
    @BindView(R.id.vote_count) TextView voteCount;
    @BindView(R.id.synopsis) TextView synopsisView;
    @BindView(R.id.trailers_field) LinearLayout trailers;
    @BindView(R.id.no_trailers) TextView noTrailers;

    @State(Movie.class) Movie movie;
    @State int filter;

    @Inject DataManager dataManager;

    private static final String TAG = MovieDetailsActivity.class.getName();
    public static final String MOVIE_EXTRA = "movie";
    public static final String FILTER_EXTRA = "filter";

    private final LifecycleRegistry registry = new LifecycleRegistry(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        Icepick.restoreInstanceState(this, savedInstanceState);

        getWindow().setEnterTransition(new Fade());

        ((MoviesApplication) getApplication()).getMoviesComponent().injectDetails(this);

        if(movie == null) {
            handleIntent(getIntent());
        }

        setUpUI();
    }

    private void handleIntent(Intent intent) {
        if(intent != null && intent.hasExtra(MOVIE_EXTRA) && intent.hasExtra(FILTER_EXTRA)) {
            movie = Parcels.unwrap(intent.getParcelableExtra(MOVIE_EXTRA));
            filter = intent.getIntExtra(FILTER_EXTRA, MoviesFilter.MOST_POPULAR);
        }
    }

    private void setUpUI() {
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        if(movie != null) {
            setTitle(movie.getTitle());
            originalTitle.setText(movie.getOriginalTitle());
            releaseView.setText(formatDate(movie.getReleaseDate()));
            ratingBar.setRating(movie.getStarRating());
            voteCount.setText(String.valueOf(movie.getVoteCount()));
            synopsisView.setText(movie.getOverview());

            refreshVideos();

            ratingView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showReviewsList();
                }
            });

            setFavoriteButtonResource();
            favoriteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    toggleFavorite();
                }
            });

            Picasso.with(this)
                    .load(getString(R.string.api_images_base_path) + movie.getThumbPath())
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .fit()
                    .centerCrop()
                    .into(moviePosterView);
        }
    }

    private void toggleFavorite() {
        if(movie.isFavorite()) {
            dataManager.requestRemoveFavorite(this, movie, new RequestCallback<String>() {
                @Override
                public void onSuccess(String message) {
                    Toast.makeText(MovieDetailsActivity.this, R.string.remove_success_message, Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "Remove from favorites " + message);
                }

                @Override
                public void onError(String error) {
                    Toast.makeText(MovieDetailsActivity.this, getString(R.string.remove_error_message) + error, Toast.LENGTH_SHORT).show();
                    Log.e(TAG, error);
                }
            });
        } else {
            dataManager.requestAddFavorite(this, movie, new RequestCallback<String>() {
                @Override
                public void onSuccess(String message) {
                    Toast.makeText(MovieDetailsActivity.this, R.string.add_success_message, Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "Add to favorites " + message);
                }

                @Override
                public void onError(String error) {
                    Toast.makeText(MovieDetailsActivity.this, R.string.add_error_message, Toast.LENGTH_SHORT).show();
                    Log.e(TAG, error);
                }
            });
        }
        movie.setFavorite(!movie.isFavorite());
        setFavoriteButtonResource();
    }

    private void setFavoriteButtonResource() {
        if(movie.isFavorite()) {
            favoriteButton.setImageResource(R.drawable.ic_favorite_white_24dp);
        } else {
            favoriteButton.setImageResource(R.drawable.ic_favorite_border_white_24dp);
        }
    }

    private void showReviewsList() {
        ConstraintLayout view = (ConstraintLayout) getLayoutInflater().inflate(R.layout.reviews_list, null);
        TextView titleView = (TextView) getLayoutInflater().inflate(R.layout.dialog_title, null);

        final RecyclerView recyclerView = ButterKnife.findById(view, R.id.list);
        final TextView textView = ButterKnife.findById(view, R.id.no_reviews);

        final ReviewsAdapter adapter = new ReviewsAdapter(new ArrayList<Review>());
        recyclerView.setAdapter(adapter);

        new AlertDialog.Builder(MovieDetailsActivity.this)
                .setCustomTitle(titleView)
                .setView(view)
                .setNegativeButton(R.string.close_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .show();

        dataManager.requestReviews(movie.getId(), new RequestCallback<List<Review>>() {
            @Override
            public void onSuccess(List<Review> reviews) {
                boolean hasItems = reviews.size() > 0;

                recyclerView.setVisibility((hasItems) ? View.VISIBLE : View.GONE);
                textView.setVisibility((hasItems) ? View.GONE : View.VISIBLE);

                adapter.setItems(reviews);
            }

            @Override
            public void onError(String error) {
                recyclerView.setVisibility(View.GONE);
                textView.setVisibility(View.VISIBLE);
            }
        });
    }

    private void refreshVideos() {
        dataManager.requestTrailers(movie.getId(), new RequestCallback<List<Video>>() {
            @Override
            public void onSuccess(List<Video> videos) {
                boolean hasItems = !videos.isEmpty();

                trailers.setVisibility((hasItems) ? View.VISIBLE : View.GONE);
                noTrailers.setVisibility((hasItems) ? View.GONE : View.VISIBLE);

                if(hasItems) {
                    setUpTrailers(videos);
                }
            }

            @Override
            public void onError(String error) {
                trailers.setVisibility(View.GONE);
                noTrailers.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setUpTrailers(List<Video> videos) {
        for (Video video : videos) {
            trailers.addView(createTrailerView(video));
        }
    }

    private View createTrailerView(final Video video) {
        ConstraintLayout view = (ConstraintLayout) getLayoutInflater().inflate(R.layout.video_item, trailers, false);

        ImageView trailerImage = ButterKnife.findById(view, R.id.trailer_image);
        TextView trailerTitle = ButterKnife.findById(view, R.id.title);
        TextView trailerType = ButterKnife.findById(view, R.id.type);
        TextView trailerSize = ButterKnife.findById(view, R.id.size);

        trailerTitle.setText(video.getName());
        trailerType.setText(video.getType());
        trailerSize.setText(String.format(Locale.getDefault(), "%dp", video.getSize()));

        Picasso.with(this)
                .load(video.getCoverLink())
                .placeholder(R.color.placeholderColor)
                .error(R.drawable.placeholder)
                .fit()
                .centerCrop()
                .into(trailerImage);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openVideoLink(video.getVideoLink());
            }
        });

        return view;
    }

    private void openVideoLink(String link) {
        Uri uri =  Uri.parse(link);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private String formatDate(String unformatted) {
        Calendar now = Calendar.getInstance(Locale.getDefault());
        String[] date = unformatted.split("-");

        if(date.length == 3) {
            now.set(Integer.parseInt(date[0]),
                    Integer.parseInt(date[1]),
                    Integer.parseInt(date[2]));
        }

        DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault());

        return df.format(now.getTime());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(filter == MoviesFilter.FAVORITES && !movie.isFavorite()) {
            finish();
        } else {
            supportFinishAfterTransition();
        }
    }

    @Override
    public LifecycleRegistry getLifecycle() {
        return registry;
    }
}
