package com.azazellj.githubwatcher.ui.main;

import com.azazellj.githubwatcher.data.model.Repository;
import com.azazellj.githubwatcher.ui.base.ProgressMvpView;

import java.util.List;

/**
 * Created by azazellj on 10/3/17.
 */

public interface MainMvpView
        extends ProgressMvpView {

    void showRepositories(List<Repository> items, int totalCount,
                          boolean endOfResults, boolean newLoad,
                          Integer nextPage);
}
