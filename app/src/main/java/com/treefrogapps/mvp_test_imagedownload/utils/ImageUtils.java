package com.treefrogapps.mvp_test_imagedownload.utils;

import android.graphics.Bitmap;

import java.io.InputStream;

/**
 * Class to check downloaded bitmap file type and decode bitmap
 * reads the first byte and compares to common file types
 * (not done by extension as this is not guaranteed to work)
 */
public class ImageUtils {

    public boolean isValidImage(Bitmap bitmap){

        // TODO

        return true;
    }

    public Bitmap decodeBitmap(InputStream is){

        Bitmap bitmap = null;

        // TODO

        return bitmap;
    }
}
