package com.azazellj.githubwatcher.ui.main;

import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.azazellj.githubwatcher.BR;
import com.azazellj.githubwatcher.R;
import com.azazellj.githubwatcher.data.model.Repository;
import com.azazellj.githubwatcher.databinding.ItemRepositoryBinding;
import com.azazellj.githubwatcher.ui.base.OnItemClickListener;
import com.azazellj.recyclerview.adapter.BaseAdapter;
import com.azazellj.recyclerview.adapter.binding.BindingViewHolder;

import javax.inject.Inject;

/**
 * Created by azazellj on 10/3/17.
 */

public class SearchAdapter
        extends BaseAdapter<Repository, BindingViewHolder> {

    private OnItemClickListener mListener;

    @Inject
    public SearchAdapter() {
    }

    @Override
    public BindingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemRepositoryBinding binding = DataBindingUtil
                .inflate(inflater, R.layout.item_repository, parent, false);

        return new BindingViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(BindingViewHolder holder, int position) {
        int holderPosition = holder.getAdapterPosition();
        holder.getView().setVariable(BR.repo, getItem(holderPosition));

        holder.getView().getRoot().setOnClickListener(v -> performClick(holderPosition));
    }

    private void performClick(int position) {
        if (mListener != null) {
            mListener.onItemClick(position);
        }
    }

    public void setOnRepoClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }
}
