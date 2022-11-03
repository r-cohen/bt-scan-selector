package com.phearme.btscanselector;


import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

public class DataBindingAdapters {
    @BindingAdapter("android:src")
    public static void setImageResource(ImageView imageView, int resource){
        imageView.setImageResource(resource);
    }
}
