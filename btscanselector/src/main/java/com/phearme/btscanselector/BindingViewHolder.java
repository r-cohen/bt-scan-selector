package com.phearme.btscanselector;

import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;


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
