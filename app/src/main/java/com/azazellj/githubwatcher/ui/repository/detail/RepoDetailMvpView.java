package com.azazellj.githubwatcher.ui.repository.detail;

import android.support.annotation.Nullable;

import com.azazellj.githubwatcher.data.model.User;
import com.azazellj.githubwatcher.ui.base.ProgressMvpView;

import java.util.List;

/**
 * Created by azazellj on 10/4/17.
 */

public interface RepoDetailMvpView
        extends ProgressMvpView {

    void showSubscribers(List<User> subscribers, @Nullable Integer nextPage);

    void onGetSubscribersError();
}
