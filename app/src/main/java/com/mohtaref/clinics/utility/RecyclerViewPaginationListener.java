package com.mohtaref.clinics.utility;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import androidx.annotation.NonNull;

public abstract class RecyclerViewPaginationListener extends RecyclerView.OnScrollListener {
    public static final int PAGE_START = 1;
    @NonNull
    private LinearLayoutManager layoutManager;
    /**
     * Set scrolling threshold here (for now i'm assuming 10 item in one page)
     */
    private static final int PAGE_SIZE = 10;
    /**
     * Supporting only LinearLayoutManager for now.
     */
    public RecyclerViewPaginationListener(@NonNull LinearLayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }
    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        int visibleItemCount = layoutManager.getChildCount();
        int totalItemCount = layoutManager.getItemCount();
        int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
        if (!isLoading() && !isLastPage()) {
            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                    && firstVisibleItemPosition >= 0
                    && totalItemCount >= PAGE_SIZE) {
                loadMoreItems();
            }
        }
    }
    protected abstract void loadMoreItems();
    public abstract boolean isLastPage();
    public abstract boolean isLoading();
}