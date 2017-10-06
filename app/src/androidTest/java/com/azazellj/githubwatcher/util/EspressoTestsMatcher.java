package com.azazellj.githubwatcher.util;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.azazellj.githubwatcher.R;

import org.hamcrest.Matcher;

import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;

/**
 * Created by azazellj on 10/5/17.
 */

public class EspressoTestsMatcher {

    public static final int RECYCLER_MAIN_SEARCH = R.id.search_recycler_view;

    public static final int NO_VALUE = -1;

    public static Matcher<View> noSuperRecyclerViewProgress() {
        return new NoProgressMatcher();
    }

    private static Matcher<View> withSRW(final int id) {
        return new SuperRecyclerViewMatcher(id);
    }

    public static Matcher<View> withSuperRecyclerViewID(final int parentSuperRecyclerViewID) {
        return allOf(withClassName(equalTo(RecyclerView.class.getName())), withSRW(parentSuperRecyclerViewID));
    }
}
