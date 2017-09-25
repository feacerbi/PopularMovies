package br.com.felipeacerbi.popularmovies.utils;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

public class InfiniteScrollListener extends RecyclerView.OnScrollListener {

    private ScrollLoadingCallback scrollLoadingCallback;
    private GridLayoutManager layoutManager;

    private boolean loading = true;
    private int previousTotal = 0;
    private int visibleThreshold = 6;
    private int firstVisibleItem = 0;
    private int visibleItemCount = 0;
    private int totalItemCount = 0;

    public InfiniteScrollListener(ScrollLoadingCallback scrollLoadingCallback, GridLayoutManager layoutManager) {
        this.scrollLoadingCallback = scrollLoadingCallback;
        this.layoutManager = layoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        if(dy > 0) {
            visibleItemCount = recyclerView.getChildCount();
            totalItemCount = layoutManager.getItemCount();
            firstVisibleItem = layoutManager.findFirstVisibleItemPosition();

            if(loading) {
                if(totalItemCount > previousTotal) {
                    loading = false;
                    previousTotal = totalItemCount;
                }
            }

            if(!loading
                    && (totalItemCount - visibleItemCount)
                    <= (firstVisibleItem + visibleThreshold)) {
                scrollLoadingCallback.onLoading();
                loading = true;
            }
        }
    }

    public void reset() {
        loading = false;
        previousTotal = 0;
        visibleThreshold = 6;
        firstVisibleItem = 0;
        visibleItemCount = 0;
        totalItemCount = 0;
    }
}
