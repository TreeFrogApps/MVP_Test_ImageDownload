package com.treefrogapps.mvp_test_imagedownload.recyclerview;

import android.graphics.Bitmap;


public class RecyclerBitmap {

    private Bitmap mBitmap;
    private String mFileLocation;

    public RecyclerBitmap(String filleLocation, Bitmap bitmap) {

        this.mFileLocation = filleLocation;
        this.mBitmap = bitmap;

    }

    public Bitmap getBitmap() {
        return mBitmap;
    }

    public String getFileLocation() {
        return mFileLocation;
    }
}
