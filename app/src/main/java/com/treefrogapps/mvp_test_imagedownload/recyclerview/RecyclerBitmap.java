package com.treefrogapps.mvp_test_imagedownload.recyclerview;

import android.graphics.Bitmap;



public class RecyclerBitmap {

    private Bitmap mBitmap;
    private String mFilename;

    public RecyclerBitmap(Bitmap bitmap, String filename){

        this.mBitmap = bitmap;
        this.mFilename = filename;
    }


    public Bitmap getmBitmap() {
        return mBitmap;
    }

    public String getmFilename() {
        return mFilename;
    }
}
