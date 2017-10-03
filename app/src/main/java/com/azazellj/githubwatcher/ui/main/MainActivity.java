package com.azazellj.githubwatcher.ui.main;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.azazellj.githubwatcher.BaseApp;
import com.azazellj.githubwatcher.R;
import com.azazellj.githubwatcher.data.model.Repository;
import com.azazellj.githubwatcher.databinding.ActivityMainBinding;
import com.malinskiy.superrecyclerview.OnMoreListener;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

public class MainActivity
        extends AppCompatActivity
        implements MainMvpView, OnMoreListener {

    @Inject
    MainPresenter presenter;
    @Inject
    SearchAdapter searchAdapter;

    private static final long DELAY = 500; // milliseconds
    private Timer mTimer = new Timer();

    private ActivityMainBinding mView;
    private SearchView mSearch;

    private Integer pageNumber = MainPresenter.FIRST_PAGE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BaseApp.getAppComponent().inject(this);
        presenter.attachView(this);

        initViews();
        initData();
    }

    private void initViews() {
        mView = DataBindingUtil.setContentView(this, R.layout.activity_main);

        setSupportActionBar(mView.toolbar);

        mView.searchRecyclerView.setupMoreListener(this, 1);
        mView.searchRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mView.searchRecyclerView.setAdapter(searchAdapter);

        hideEmptyView();
    }

    private void initData() {

    }

    private void scheduleSearch(final String searchQuery) {
        mTimer.cancel();
        mTimer = new Timer();
        mTimer.schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        runOnUiThread(() -> {
                            searchAdapter.clear();
                            presenter.findRepo(searchQuery);
                            hideEmptyView();
                        });
                    }
                },
                DELAY
        );
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search, menu);

        MenuItem menuItem = menu.findItem(R.id.search);
//        MenuItemCompat.expandActionView(menuItem);

        mSearch = (SearchView) menuItem.getActionView();
        mSearch.setOnCloseListener(() -> {
            resetState();
            return false;
        });
        MenuItemCompat.setOnActionExpandListener(menuItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(final MenuItem item) {
//                showFastSearch();
                return false;
            }

            @Override
            public boolean onMenuItemActionCollapse(final MenuItem item) {
//                //clear items
//                if (!isFastSearchVisible() && !searchAdapter.isEmpty()) {
//                    searchAdapter.clear();
//                    return false;
//                }
//                //clean search query
//                if (!isFastSearchVisible() && !getSearchQuery().isEmpty()) {
//                    mSearch.setQuery("", false);
//                    return false;
//                }
//                //finish activity
//                if (getSearchQuery().isEmpty()) {
//                    finish();
//                    return false;
//                }
//                finish();

                return false;
            }
        });
        mSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String query) {
                searchAdapter.clear();
                presenter.findRepo(query);

                return false;
            }

            @Override
            public boolean onQueryTextChange(final String searchQuery) {
                if ("".equals(searchQuery)) {
                    resetState();
                }

                if (searchQuery.length() < MainPresenter.MIN_STR_SIZE) {
                    resetState();
                    return true;
                }

                searchAdapter.clear();

                scheduleSearch(searchQuery);

                return false;
            }
        });

        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mSearch != null) {
            mSearch.clearFocus();
        }
    }

    private void resetState() {
        searchAdapter.clear();
        pageNumber = MainPresenter.FIRST_PAGE;
    }

    @Override
    protected void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }

    @Override
    public void showProgress() {
        mView.searchRecyclerView.showMoreProgress();
    }

    @Override
    public void hideProgress() {
        mView.searchRecyclerView.hideMoreProgress();
    }

    @Override
    public void onMoreAsked(final int numberOfItems, final int numberBeforeMore,
                            final int currentItemPos) {
        if (currentItemPos == -1) {
            hideProgress();
            return;
        }

        if (pageNumber == null) {
            hideProgress();
            return;
        }

        String query = getSearchQuery();
        pageNumber++;

        presenter.findRepo(query, pageNumber);
    }


    @Override
    public void showRepositories(List<Repository> items, int totalCount, boolean endOfResults, boolean newLoad, Integer nextPage) {
        //no results
        if (items.isEmpty() && newLoad) {
            showEmptyView();
            return;
        } else {
            hideEmptyView();
        }

        this.pageNumber = nextPage;
        this.searchAdapter.addAll(items);


    }

    private String getSearchQuery() {
        return mSearch.getQuery().toString();
    }

    private void showEmptyView() {
        mView.searchRecyclerView.getEmptyView().setVisibility(View.VISIBLE);
    }

    private void hideEmptyView() {
        mView.searchRecyclerView.getEmptyView().setVisibility(View.GONE);
    }
}
