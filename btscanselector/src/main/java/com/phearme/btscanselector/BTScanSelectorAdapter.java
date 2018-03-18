package com.phearme.btscanselector;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;


class BTScanSelectorAdapter extends RecyclerView.Adapter<BindingViewHolder> {

    private BTScanSelectorViewModel viewModel;
    private IBTScanDataEvents scanEvents;

    BTScanSelectorAdapter(Context context, IBTScanSelectorEvents callbacks, IBTScanDataEvents scanEvents) {
        this.scanEvents = scanEvents;
        this.viewModel = new BTScanSelectorViewModel(callbacks, new IBTScanDataEvents() {
            @Override
            public void onDataChange() {
                notifyDataSetChanged();
                BTScanSelectorAdapter.this.scanEvents.onDataChange();
            }

            @Override
            public void onScanToggled(boolean scanning) {
                BTScanSelectorAdapter.this.scanEvents.onScanToggled(scanning);
            }
        }, context);
    }

    @Override
    public BindingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewDataBinding viewDataBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()), R.layout.selector_dialog_item, parent, false);
        return new BindingViewHolder(viewDataBinding);
    }

    @Override
    public void onBindViewHolder(BindingViewHolder holder, int position) {
        final BTScanResultItem device = viewModel.getDevices().get(position);
        holder.getViewDataBinding().setVariable(BR.device, device);
        holder.getViewDataBinding().setVariable(BR.viewModel, viewModel);
        holder.getViewDataBinding().executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return viewModel.getDevices().size();
    }

    void terminate(Context context) {
        if (viewModel != null) {
            viewModel.terminate(context);
        }
    }
}
