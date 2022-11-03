package com.phearme.btscanselector;


import androidx.fragment.app.FragmentActivity;

public class BTScanSelectorBuilder {
    private static ABTScanSelectorEventsHandler mHandler;
    private static String mTitle;

    public static void build(FragmentActivity activity, ABTScanSelectorEventsHandler handler) {
        build(activity, handler, null);
    }

    public static void build(FragmentActivity activity, ABTScanSelectorEventsHandler handler, String title) {
        mHandler = handler;
        mTitle = title;
        BTScanSelectorDialog dialog = new BTScanSelectorDialog();
        dialog.show(activity.getSupportFragmentManager(), "selectbtdevice");
    }

    public static ABTScanSelectorEventsHandler getHandler() {
        return mHandler;
    }

    public static String getTitle() {
        return mTitle;
    }
}
