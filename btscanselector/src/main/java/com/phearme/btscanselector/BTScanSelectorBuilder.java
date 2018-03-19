package com.phearme.btscanselector;


import android.support.v4.app.FragmentActivity;

public class BTScanSelectorBuilder {
    public static void build(FragmentActivity activity, ABTScanSelectorEventsHandler handler) {
        build(activity, handler, null);
    }

    public static void build(FragmentActivity activity, ABTScanSelectorEventsHandler handler, String title) {
        BTScanSelectorDialog dialog = new BTScanSelectorDialog();
        dialog.setTitle(title);
        dialog.setEvents(handler);
        dialog.show(activity.getSupportFragmentManager(), "selectbtdevice");
    }
}
