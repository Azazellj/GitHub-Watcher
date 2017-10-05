package com.azazellj.githubwatcher.ui.repository.detail;

import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.azazellj.githubwatcher.BR;
import com.azazellj.githubwatcher.R;
import com.azazellj.githubwatcher.data.model.User;
import com.azazellj.githubwatcher.databinding.ItemSubscriberBinding;
import com.azazellj.recyclerview.adapter.BaseAdapter;
import com.azazellj.recyclerview.adapter.binding.BindingViewHolder;

import javax.inject.Inject;

/**
 * Created by azazellj on 10/4/17.
 */

public class SubscribersAdapter
        extends BaseAdapter<User, BindingViewHolder> {

    @Inject
    public SubscribersAdapter() {
    }

    @Override
    public BindingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemSubscriberBinding binding = DataBindingUtil
                .inflate(inflater, R.layout.item_subscriber, parent, false);

        return new BindingViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(BindingViewHolder holder, int position) {
        holder.getView().setVariable(BR.user, getItem(holder.getAdapterPosition()));
    }
}
