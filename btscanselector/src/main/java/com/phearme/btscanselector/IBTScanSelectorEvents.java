package com.phearme.btscanselector;


import android.bluetooth.BluetoothDevice;

public interface IBTScanSelectorEvents {
    void onDeviceSelected(BluetoothDevice device);
}
