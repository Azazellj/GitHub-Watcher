package com.azazellj.githubwatcher.ui.base;

/**
 * Created by azazellj on 10/3/17.
 */

public interface Presenter<V extends MvpView> {

    void attachView(V mvpView);

    void detachView();
}