package com.treefrogapps.mvp_test_imagedownload.recyclerview;

import android.graphics.Bitmap;


public class RecyclerBitmap {

    private Bitmap mBitmap;
    private String mFileLocation;

    /**
     * RecyclerBitmap class to hole the file path and processed bitmap
     * used in the RecyclerView Adapter
     *
     * @param fileLocation String to the file path of the image
     * @param bitmap reduced and processed bitmap image
     */

    public RecyclerBitmap(String fileLocation, Bitmap bitmap) {

        this.mFileLocation = fileLocation;
        this.mBitmap = bitmap;

    }

    public Bitmap getBitmap() {
        return mBitmap;
    }

    public String getFileLocation() {
        return mFileLocation;
    }
}
