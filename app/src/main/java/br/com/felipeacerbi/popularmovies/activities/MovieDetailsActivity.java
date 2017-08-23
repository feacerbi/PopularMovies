package br.com.felipeacerbi.popularmovies.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import br.com.felipeacerbi.popularmovies.R;
import br.com.felipeacerbi.popularmovies.models.Movie;
import butterknife.BindView;
import butterknife.ButterKnife;
import icepick.Icepick;
import icepick.State;

public class MovieDetailsActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.toolbar_layout) CollapsingToolbarLayout toolbarLayout;
    @BindView(R.id.movie_poster) ImageView moviePosterView;
    @BindView(R.id.title) TextView titleView;
    @BindView(R.id.release) TextView releaseView;
    @BindView(R.id.rating_bar) RatingBar ratingBar;
    @BindView(R.id.synopsis) TextView synopsisView;

    @State(Movie.class) Movie movie = null;

    public static final String MOVIE_EXTRA = "movie";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        getWindow().setEnterTransition(new Fade());

        Icepick.restoreInstanceState(this, savedInstanceState);

        if(movie == null) {
            handleIntent(getIntent());
        }

        setUpUI();
    }

    private void handleIntent(Intent intent) {
        if(intent != null && intent.hasExtra(MOVIE_EXTRA)) {
            movie = Parcels.unwrap(intent.getParcelableExtra(MOVIE_EXTRA));
        }
    }

    private void setUpUI() {
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        setTitle(null);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                supportFinishAfterTransition();
            }
        });

        if(movie != null) {
            titleView.setText(movie.getTitle());
            releaseView.setText(movie.getReleaseDate());
            ratingBar.setRating(movie.getRating() / 2);
            synopsisView.setText(movie.getOverview());

            Picasso.with(this)
                    .load(getString(R.string.api_images_base_path) + movie.getThumbPath())
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .fit()
                    .centerCrop()
                    .into(moviePosterView);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        supportFinishAfterTransition();
    }
}
