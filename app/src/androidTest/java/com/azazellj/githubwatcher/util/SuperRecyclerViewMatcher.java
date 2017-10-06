package com.azazellj.githubwatcher.util;

import android.support.test.espresso.util.HumanReadables;
import android.view.View;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

/**
 * Created by azazellj on 10/5/17.
 */

public class SuperRecyclerViewMatcher extends TypeSafeMatcher<View> {

    private int superRecyclerViewID;
    private View mView;

    public SuperRecyclerViewMatcher(final int id) {
        this.superRecyclerViewID = id;
    }

    @Override
    protected boolean matchesSafely(final View view) {
        this.mView = view;

        ViewParent firstParent = view.getParent();
        if (firstParent == null) {
            return false;
        }

        ViewParent secondParent = firstParent.getParent();
        if (secondParent == null) {
            return false;
        }

        ViewParent lastParent = secondParent.getParent();

        return lastParent != null && ((View) lastParent).getId() == superRecyclerViewID;

    }

    @Override
    public void describeTo(final Description description) {
        description.appendText("with SuperRecyclerView ");

        if (mView != null) {
            description.appendValue(HumanReadables.describe(mView));
        } else {
            description.appendText("with ID: ");
            description.appendValue(superRecyclerViewID);
        }
    }
}