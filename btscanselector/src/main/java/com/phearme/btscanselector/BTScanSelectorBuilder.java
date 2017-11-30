package com.phearme.btscanselector;


import android.support.v4.app.FragmentActivity;

public class BTScanSelectorBuilder {
    public static void build(FragmentActivity activity, ABTScanSelectorEventsHandler handler) {
        BTScanSelectorDialog dialog = new BTScanSelectorDialog();
        dialog.setCancelable(true);
        dialog.setEvents(handler);
        dialog.show(activity.getSupportFragmentManager(), "selectbtdevice");
    }

}
