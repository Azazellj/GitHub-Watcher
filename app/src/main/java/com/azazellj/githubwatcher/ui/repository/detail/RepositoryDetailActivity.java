package com.azazellj.githubwatcher.ui.repository.detail;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.Toast;

import com.azazellj.githubwatcher.BaseApp;
import com.azazellj.githubwatcher.R;
import com.azazellj.githubwatcher.data.model.Repository;
import com.azazellj.githubwatcher.data.model.User;
import com.azazellj.githubwatcher.databinding.ActivityRepositoryDetailBinding;
import com.azazellj.githubwatcher.util.Constants;
import com.malinskiy.superrecyclerview.OnMoreListener;

import java.util.List;

import javax.inject.Inject;

public class RepositoryDetailActivity
        extends AppCompatActivity
        implements RepoDetailMvpView, OnMoreListener {

    @Inject
    RepositoryDetailPresenter presenter;
    @Inject
    SubscribersAdapter adapter;

    private ActivityRepositoryDetailBinding mView;

    private Repository mRepo;
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
        mView = DataBindingUtil.setContentView(this, R.layout.activity_repository_detail);

        setSupportActionBar(mView.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mView.subscribersRecyclerView.setupMoreListener(this, 1);
        mView.subscribersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mView.subscribersRecyclerView.setAdapter(adapter);

        mView.subscribersRecyclerView.showProgress();
    }

    private void initData() {
        if (!getIntent().hasExtra(Constants.ARGS_REPO)) {
            finish();
        } else {
            mRepo = (Repository) getIntent().getSerializableExtra(Constants.ARGS_REPO);
            getSupportActionBar().setTitle(mRepo.getFullName());
            presenter.getSubscriptions(mRepo.getFullName(), pageNumber);
        }
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }

    @Override
    public void onMoreAsked(final int numberOfItems, final int numberBeforeMore,
                            final int currentItemPos) {

        boolean noPosition = currentItemPos == RecyclerView.NO_POSITION;
        boolean emptyNextPage = pageNumber == null;

        if (noPosition || emptyNextPage) {
            hideProgress();
        } else {
            presenter.getSubscriptions(mRepo.getFullName(), pageNumber);
        }
    }

    @Override
    public void showSubscribers(List<User> subscribers, @Nullable Integer nextPage) {
        this.pageNumber = nextPage;
        this.adapter.addAll(subscribers);
    }

    @Override
    public void onGetSubscribersError() {
        Toast.makeText(this, "Oooppppssss...", Toast.LENGTH_SHORT).show();
        mView.subscribersRecyclerView.showRecycler();
    }

    @Override
    public void showProgress() {
        mView.subscribersRecyclerView.showMoreProgress();
    }

    @Override
    public void hideProgress() {
        mView.subscribersRecyclerView.hideMoreProgress();
        mView.subscribersRecyclerView.showRecycler();
    }
}
