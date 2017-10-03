package com.azazellj.githubwatcher.ui.main;

import com.azazellj.githubwatcher.data.ApiRestService;
import com.azazellj.githubwatcher.data.model.Repository;
import com.azazellj.githubwatcher.data.response.RepositoryResponse;
import com.azazellj.githubwatcher.ui.base.BasePresenter;
import com.azazellj.githubwatcher.ui.base.ProgressPresenter;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by azazellj on 10/3/17.
 */

public class MainPresenter
        extends BasePresenter<MainMvpView>
        implements ProgressPresenter {

    public static final int FIRST_PAGE = 1;
    public static final boolean NEW_LOAD = true;
    public static final int MIN_STR_SIZE = 3;

    @Inject
    ApiRestService apiRestService;

    @Inject
    public MainPresenter(ApiRestService restService) {
        this.apiRestService = restService;
    }

    public void findRepo(final String searchStr) {
        findRepo(searchStr, FIRST_PAGE, true);
    }

    public void findRepo(final String searchStr, final int page) {
        findRepo(searchStr, page, false);
    }

    private void findRepo(final String searchStr, final int page, final boolean newLoad) {

        Observable.just(apiRestService)
                .subscribeOn(Schedulers.newThread())
                .map(restService -> restService.findRepositories(searchStr, page))
                .flatMap(result -> result)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(() -> showProgressOnScreen())
                .doOnCompleted(() -> hideProgressOnScreen())
                .doOnError(error -> hideProgressOnScreen())
                .subscribe(
                        response -> showRepositories(response, newLoad, page),
                        error -> analyzeGetRepoError("findRepo", error)
                );
    }

    private void showRepositories(RepositoryResponse response,
                                  boolean newLoad, int page) {
        List<Repository> items = response.getItems();
        int totalCount = response.getTotalCount();
        boolean endOfResults = items.isEmpty() && page != FIRST_PAGE;
        Integer nextPage = endOfResults ? null : ++page;

        getMvpView().showRepositories(items, totalCount, endOfResults, newLoad, nextPage);
    }

    private void analyzeGetRepoError(String funcName, Throwable error) {
        String s = "";
    }


    @Override
    public void hideProgressOnScreen() {
        getMvpView().hideProgress();
    }

    @Override
    public void showProgressOnScreen() {
        getMvpView().hideProgress();
    }
}
