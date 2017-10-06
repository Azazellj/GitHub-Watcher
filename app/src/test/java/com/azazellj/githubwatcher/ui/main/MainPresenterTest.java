package com.azazellj.githubwatcher.ui.main;

import com.azazellj.githubwatcher.common.MockModelFabric;
import com.azazellj.githubwatcher.common.injection.rule.RxSchedulersOverrideRule;
import com.azazellj.githubwatcher.data.ApiRestService;
import com.azazellj.githubwatcher.data.response.RepositoryResponse;
import com.azazellj.githubwatcher.util.Constants;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import rx.Observable;

import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.doReturn;

/**
 * Created by azazellj on 10/5/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class MainPresenterTest {

    @Mock
    MainMvpView mView;
    @Mock
    ApiRestService mRestService;

    private MainPresenter mPresenter;

    @Rule
    public final RxSchedulersOverrideRule mOverrideSchedulersRule = new RxSchedulersOverrideRule();

    @Before
    public void setUp() {
        mPresenter = new MainPresenter(mRestService);
        mPresenter.attachView(mView);
    }

    @After
    public void tearDown() {
        mPresenter.detachView();
    }

    @Test
    public void loadRepositories_NotEmpty() {
        String repoName = MockModelFabric.randomString();
        RepositoryResponse response = MockModelFabric.randomRepositories(10);
        Integer nextPage = Constants.FIRST_PAGE + 1;
        stubRestGetRepositories(Observable.just(response), repoName, Constants.FIRST_PAGE);

        mPresenter.findRepo(repoName);

        verify(mView).showProgress();
        verify(mView).hideProgress();
        verify(mView).showRepositories(response.getItems(), true, nextPage);
    }

    private void stubRestGetRepositories(final Observable observable, String query, int page) {
        doReturn(observable)
                .when(mRestService)
                .findRepositories(query, page);
    }
}
