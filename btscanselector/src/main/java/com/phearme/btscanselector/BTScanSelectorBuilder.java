package com.phearme.btscanselector;


import android.support.v4.app.FragmentActivity;

public class BTScanSelectorBuilder {
    public static void build(FragmentActivity activity, IBTScanSelectorEvents events) {
        BTScanSelectorDialog dialog = new BTScanSelectorDialog();
        dialog.setCancelable(true);
        dialog.setEvents(events);
        dialog.show(activity.getSupportFragmentManager(), "selectbtdevice");
    }
}
