package com.phearme.btscanselector;

import static android.Manifest.permission.BLUETOOTH_CONNECT;
import static android.Manifest.permission.BLUETOOTH_SCAN;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

import android.Manifest;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Objects;


public class BTScanSelectorDialog extends DialogFragment {
    private static final int REQUEST_ENABLE_BT = 1;
    private static final int REQUEST_LOCATION_PERMISSIONS = 2;
    private static final int REQUEST_ANDROID_12_BLUETOOTH_PERMISSIONS = 3;
    BTScanSelectorAdapter mAdapter;
    RecyclerView recyclerView;
    ProgressBar progressBar;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.selector_dialog, null);

        recyclerView = view.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        progressBar = view.findViewById(R.id.progressBar);

        BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
        if (btAdapter != null && !btAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        } else {
            bindRecyclerView();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        String dialogTitle = BTScanSelectorBuilder.getTitle();
        builder.setView(view)
                .setCancelable(false)
                .setTitle(dialogTitle == null ? getString(R.string.nearbyDevices) : dialogTitle)
                .setNeutralButton(R.string.refresh, null)
                .setNegativeButton(R.string.close, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        BTScanSelectorDialog.this.getDialog().dismiss();
                    }
                });
        final AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mAdapter != null) {
                            mAdapter.refresh();
                        }
                    }
                });
            }
        });
        return dialog;
    }

    @Override
    public void onDestroy() {
        if (mAdapter != null && getActivity() != null) {
            mAdapter.terminate(getActivity());
        }
        super.onDestroy();
    }

    private boolean hasRequiredPermissions() {
        boolean hasLocationLocationPermissions = Objects.requireNonNull(getActivity()).checkCallingOrSelfPermission(
                Manifest.permission.ACCESS_FINE_LOCATION) == PERMISSION_GRANTED &&
                Objects.requireNonNull(getActivity()).checkCallingOrSelfPermission(
                        Manifest.permission.ACCESS_COARSE_LOCATION) == PERMISSION_GRANTED;
        boolean hasBluetoothPermissions = true;
        if (isAtLeastAndroid12()) {
            hasBluetoothPermissions = getActivity().checkCallingOrSelfPermission(BLUETOOTH_SCAN) == PERMISSION_GRANTED &&
                    getActivity().checkCallingOrSelfPermission(BLUETOOTH_CONNECT) == PERMISSION_GRANTED;
        }
        return hasLocationLocationPermissions && hasBluetoothPermissions;
    }

    private boolean isAtLeastAndroid12() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.S;
    }

    private void bindRecyclerView() {
        if (getActivity() == null) {
            return;
        }
        if (!hasRequiredPermissions()) {
            if (isAtLeastAndroid12()) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{BLUETOOTH_SCAN, BLUETOOTH_CONNECT}, REQUEST_ANDROID_12_BLUETOOTH_PERMISSIONS);
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSIONS);
            }
            return;
        }

        if (recyclerView != null) {
            mAdapter = new BTScanSelectorAdapter(getActivity(), new IBTScanSelectorEvents() {
                @Override
                public void onDeviceSelected(BluetoothDevice device) {
                    ABTScanSelectorEventsHandler events = BTScanSelectorBuilder.getHandler();
                    if (events != null) {
                        events.onDeviceSelected(device);
                    }
                    BTScanSelectorDialog.this.getDialog().cancel();
                }

                @Override
                public boolean onDeviceFound(BluetoothDevice device) {
                    ABTScanSelectorEventsHandler events = BTScanSelectorBuilder.getHandler();
                    return events == null || events.onDeviceFound(device);
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
            // wait a few seconds for the bt device to be ready
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    bindRecyclerView();
                }
            }, 2000);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_LOCATION_PERMISSIONS && grantResults.length > 0
                && grantResults[0] == PERMISSION_GRANTED) {
            bindRecyclerView();
        } else if (requestCode == REQUEST_ANDROID_12_BLUETOOTH_PERMISSIONS && grantResults.length > 0) {
            boolean allGranted = true;
            for (int grantResult : grantResults) {
                if (grantResult != PERMISSION_GRANTED) {
                    allGranted = false;
                    break;
                }
            }
            if (allGranted) {
                bindRecyclerView();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        try {
            FragmentTransaction ft = manager.beginTransaction();
            ft.add(this, tag);
            ft.commitAllowingStateLoss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
