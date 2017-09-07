package com.phearme.btscanselector;

import android.Manifest;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;


public class BTScanSelectorDialog extends DialogFragment {
    private static int REQUEST_ENABLE_BT = 1;
    private static int REQUEST_LOCATION_PERMISSION = 2;
    private IBTScanSelectorEvents mEvents;
    BTScanSelectorAdapter mAdapter;
    RecyclerView recyclerView;
    ProgressBar progressBar;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.selector_dialog, null);

        recyclerView = (RecyclerView) view.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!btAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        } else {
            bindRecyclerView();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view)
                .setTitle(R.string.nearbyDevices)
                .setNegativeButton(R.string.close, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        BTScanSelectorDialog.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }

    public void setEvents(IBTScanSelectorEvents events) {
        this.mEvents = events;
    }

    @Override
    public void onDestroy() {
        if (mAdapter != null) {
            mAdapter.terminate(getActivity());
        }
        super.onDestroy();
    }

    private void bindRecyclerView() {
        if (getActivity().checkCallingOrSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
            return;
        }

        if (recyclerView != null) {
            mAdapter = new BTScanSelectorAdapter(getActivity(), new IBTScanSelectorEvents() {
                @Override
                public void onDeviceSelected(BluetoothDevice device) {
                    if (mEvents != null) {
                        mEvents.onDeviceSelected(device);
                    }
                    BTScanSelectorDialog.this.getDialog().cancel();
                }


            }, new IBTScanDataEvents() {
                @Override
                public void onDataChange() { }

                @Override
                public void onScanToggled(boolean scanning) {
                    if (progressBar != null) {
                        progressBar.setVisibility(scanning ? View.VISIBLE : View.INVISIBLE);
                    }
                }
            });
            recyclerView.setAdapter(mAdapter);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BT) {
            bindRecyclerView();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_LOCATION_PERMISSION && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            bindRecyclerView();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
