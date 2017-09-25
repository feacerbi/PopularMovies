package br.com.felipeacerbi.popularmovies.adapters;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.felipeacerbi.popularmovies.R;
import br.com.felipeacerbi.popularmovies.models.Review;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ReviewsAdapter extends Adapter<RecyclerView.ViewHolder> {

    private List<Review> reviewsList = new ArrayList<>();

    public ReviewsAdapter(List<Review> reviewsList) {
        this.reviewsList.addAll(reviewsList);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_item, parent, false);
        return new ReviewViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Review review = reviewsList.get(position);
        final ReviewViewHolder reviewViewHolder = (ReviewViewHolder) holder;

        reviewViewHolder.name.setText(review.getAuthor());
        reviewViewHolder.commentText.setText(review.getContent());
    }

    public void setItems(List<Review> items) {
        reviewsList = items;
        notifyItemRangeInserted(0, reviewsList.size() - 1);
    }

    @Override
    public int getItemCount() {
        return reviewsList.size();
    }

    class ReviewViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.review_author) TextView name;
        @BindView(R.id.review_content) TextView commentText;

        ReviewViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
