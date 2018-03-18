package com.phearme.btscanselector;


import android.bluetooth.BluetoothDevice;

public class BTScanResultItem {
    private BluetoothDevice bluetoothDevice;
    private int rssi;

    public BTScanResultItem(BluetoothDevice bluetoothDevice, int rssi) {
        this.bluetoothDevice = bluetoothDevice;
        this.rssi = rssi;
    }

    public BluetoothDevice getBluetoothDevice() {
        return bluetoothDevice;
    }

    public int getRssi() {
        return rssi;
    }
}
