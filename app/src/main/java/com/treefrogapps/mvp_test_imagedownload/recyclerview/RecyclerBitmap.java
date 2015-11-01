package com.treefrogapps.mvp_test_imagedownload.recyclerview;

import android.graphics.Bitmap;
import android.net.Uri;


public class RecyclerBitmap {

    private Bitmap mBitmap;
    private String mFilename;
    private Uri mFileLocation;

    public RecyclerBitmap(Bitmap bitmap){

        this.mBitmap = bitmap;

    }

    public Bitmap getBitmap() {
        return mBitmap;
    }
}
