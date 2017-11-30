package com.phearme.btscanselector;


import android.bluetooth.BluetoothDevice;

public abstract class ABTScanSelectorEventsHandler implements IBTScanSelectorEvents {
    @Override
    public boolean onDeviceFound(BluetoothDevice device) {
        return true;
    }
}
