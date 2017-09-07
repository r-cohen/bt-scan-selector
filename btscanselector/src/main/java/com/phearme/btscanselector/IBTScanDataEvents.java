package com.phearme.btscanselector;


interface IBTScanDataEvents {
    void onDataChange();
    void onScanToggled(boolean scanning);
}
