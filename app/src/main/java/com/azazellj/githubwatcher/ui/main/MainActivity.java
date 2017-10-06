package com.azazellj.githubwatcher.ui.main;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.View;

import com.azazellj.githubwatcher.BaseApp;
import com.azazellj.githubwatcher.R;
import com.azazellj.githubwatcher.data.model.Repository;
import com.azazellj.githubwatcher.databinding.ActivityMainBinding;
import com.azazellj.githubwatcher.ui.base.OnItemClickListener;
import com.azazellj.githubwatcher.ui.repository.detail.RepositoryDetailActivity;
import com.azazellj.githubwatcher.util.Constants;
import com.malinskiy.superrecyclerview.OnMoreListener;

import java.util.List;

import javax.inject.Inject;

public class MainActivity
        extends AppCompatActivity
        implements MainMvpView, OnMoreListener,
        OnItemClickListener {

    @Inject
    MainPresenter presenter;
    @Inject
    SearchAdapter searchAdapter;

    private ActivityMainBinding mView;

    private Integer pageNumber = Constants.FIRST_PAGE;

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
        searchAdapter.setOnRepoClickListener(this);

        mView.search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String query) {
                searchAdapter.clear();
                presenter.findRepo(query);
                hideEmptyView();

                return false;
            }

            @Override
            public boolean onQueryTextChange(final String searchQuery) {
                return false;
            }
        });

        hideEmptyView();
    }

    private void initData() {

    }

    @Override
    public void onItemClick(int position) {
        Repository repository = searchAdapter.getItem(position);
        if (repository == null) {
            return;
        }

        Intent intent = new Intent(this, RepositoryDetailActivity.class);
        intent.putExtra(Constants.ARGS_REPO, repository);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mView.search != null) {
            mView.search.clearFocus();
        }
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

        boolean noPosition = currentItemPos == RecyclerView.NO_POSITION;
        boolean emptyNextPage = pageNumber == null;
        boolean emptyCurrentQuery = TextUtils.isEmpty(getSearchQuery());

        if (noPosition || emptyNextPage || emptyCurrentQuery) {
            hideProgress();
        } else {
            presenter.findRepo(getSearchQuery(), pageNumber);
        }
    }

    @Override
    public void showRepositories(List<Repository> items, boolean newLoad, Integer nextPage) {
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
        return mView.search.getQuery().toString();
    }

    private void showEmptyView() {
        mView.searchRecyclerView.getEmptyView().setVisibility(View.VISIBLE);
    }

    private void hideEmptyView() {
        mView.searchRecyclerView.getEmptyView().setVisibility(View.GONE);
    }

    public ActivityMainBinding getView() {
        return mView;
    }
}
