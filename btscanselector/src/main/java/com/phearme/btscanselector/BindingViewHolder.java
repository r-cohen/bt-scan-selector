package com.phearme.btscanselector;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;


class BindingViewHolder extends RecyclerView.ViewHolder {
    private ViewDataBinding viewDataBinding;

    BindingViewHolder(ViewDataBinding itemView) {
        super(itemView.getRoot());
        viewDataBinding = itemView;
    }

    ViewDataBinding getViewDataBinding() {
        return viewDataBinding;
    }
}
