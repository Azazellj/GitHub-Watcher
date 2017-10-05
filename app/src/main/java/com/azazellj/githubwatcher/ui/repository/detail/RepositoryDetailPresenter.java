package com.azazellj.githubwatcher.ui.repository.detail;

import com.azazellj.githubwatcher.data.ApiRestService;
import com.azazellj.githubwatcher.data.model.User;
import com.azazellj.githubwatcher.ui.base.BasePresenter;
import com.azazellj.githubwatcher.ui.base.ProgressPresenter;
import com.azazellj.githubwatcher.util.Constants;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by azazellj on 10/5/17.
 */

public class RepositoryDetailPresenter
        extends BasePresenter<RepoDetailMvpView>
        implements ProgressPresenter {

    private ApiRestService apiRestService;

    @Inject
    public RepositoryDetailPresenter(ApiRestService restService) {
        this.apiRestService = restService;
    }

    public void getSubscriptions(String repoName, int page) {
        Observable.just(apiRestService)
                .subscribeOn(Schedulers.newThread())
                .flatMap(restService -> restService.getSubscribers(repoName, page))
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(() -> showProgressOnScreen())
                .doOnCompleted(() -> hideProgressOnScreen())
                .doOnError(error -> hideProgressOnScreen())
                .subscribe(
                        response -> showSubscriptions(response, page),
                        error -> analyzeGetSubscribersError("getSubscriptions", error)
                );
    }

    private void showSubscriptions(List<User> response, int page) {
        boolean endOfResults = response.isEmpty() && page != Constants.FIRST_PAGE;
        Integer nextPage = endOfResults ? null : ++page;

        getMvpView().showSubscribers(response, nextPage);
    }

    private void analyzeGetSubscribersError(String funcName, Throwable error) {
        getMvpView().onGetSubscribersError();
    }

    @Override
    public void showProgressOnScreen() {
        getMvpView().showProgress();
    }

    @Override
    public void hideProgressOnScreen() {
        getMvpView().hideProgress();
    }
}
