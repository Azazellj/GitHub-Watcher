package com.azazellj.githubwatcher.util;

import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.malinskiy.superrecyclerview.SuperRecyclerView;

import org.hamcrest.Description;

/**
 * Created by azazellj on 4/4/17.
 */

public class NoProgressMatcher extends BoundedMatcher<View, View> {

    public NoProgressMatcher() {
        super(View.class);
    }

    @Override
    public void describeTo(final Description description) {
        description.appendText("no progress views are visible now");
    }

    @Override
    protected boolean matchesSafely(final View view) {
        if (!(view instanceof SuperRecyclerView)) {
            return false;
        }

        SuperRecyclerView recyclerView = (SuperRecyclerView) view;
        View progress = recyclerView.getProgressView();
        View more = recyclerView.getMoreProgressView();
        SwipeRefreshLayout swipeToRefresh = recyclerView.getSwipeToRefresh();

        if (progress != null && progress.getVisibility() == View.VISIBLE) {
            return false;
        }
        if (more != null && more.getVisibility() == View.VISIBLE) {
            return false;
        }
        if (swipeToRefresh != null && swipeToRefresh.isRefreshing()) {
            return false;
        }

        return true;
    }
}