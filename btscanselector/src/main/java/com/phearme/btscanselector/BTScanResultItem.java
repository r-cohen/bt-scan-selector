package com.phearme.btscanselector;


import android.bluetooth.BluetoothDevice;

public class BTScanResultItem {
    private BluetoothDevice bluetoothDevice;
    private int rssi;
    private int iconResourceId;

    BTScanResultItem(BluetoothDevice bluetoothDevice, int rssi) {
        this.bluetoothDevice = bluetoothDevice;
        setRssi(rssi);
        updateIconResId();
    }

    public BluetoothDevice getBluetoothDevice() {
        return bluetoothDevice;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
        updateIconResId();
    }

    private void updateIconResId() {
        if (rssi > -50) {
            iconResourceId = R.drawable.network_high;
            return;
        }
        if (rssi <= -50 && rssi > -67) {
            iconResourceId = R.drawable.network_medium;
            return;
        }
        if (rssi <= -67 && rssi > -70) {
            iconResourceId = R.drawable.network_low;
            return;
        }
        if (rssi <= -70) {
            iconResourceId = R.drawable.network_no_signal;
            return;
        }
        iconResourceId = R.drawable.network_null;
    }

    public int getIconResourceId() {
        return iconResourceId;
    }
}
