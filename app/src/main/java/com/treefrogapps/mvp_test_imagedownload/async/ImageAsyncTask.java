package com.treefrogapps.mvp_test_imagedownload.async;

import android.os.AsyncTask;
import android.util.Log;

import com.treefrogapps.mvp_test_imagedownload.MVP;
import com.treefrogapps.mvp_test_imagedownload.recyclerview.RecyclerBitmap;
import com.treefrogapps.mvp_test_imagedownload.utils.ImageUtils;

import java.lang.ref.WeakReference;
import java.util.concurrent.CountDownLatch;

/**
 * Custom Async task to handle processing of image
 * <p/>
 * Async task uses Image Utils to convert downloaded and saved bitmap to grey scale
 *
 * A POJO object RecyclerBitmap is returned containing a File Location and the processed Bitmap
 */

public class ImageAsyncTask extends AsyncTask<String, Void, RecyclerBitmap> {

    // weak reference to the finish observer interface
    private WeakReference<MVP.AsyncFinishedObserver> mAsyncFinishedObserver;
    private CountDownLatch mCountDownLatch;
    private int mIndex;

    public ImageAsyncTask(int index, MVP.AsyncFinishedObserver asyncFinishedObserver,
                          CountDownLatch countDownLatch) {

        this.mAsyncFinishedObserver = new WeakReference<>(asyncFinishedObserver);
        this.mCountDownLatch = countDownLatch;
        this.mIndex = index;

    }

    @Override
    protected RecyclerBitmap doInBackground(String... params) {

        Log.i("ImageAsyncTask", String.valueOf(mIndex) + " doInBackground started");

        String fileLocation = params[0];

        return new RecyclerBitmap(fileLocation, ImageUtils.scaleFilteredBitmap(fileLocation));
    }

    @Override
    protected void onPostExecute(RecyclerBitmap recyclerBitmap) {
        super.onPostExecute(recyclerBitmap);

        if (recyclerBitmap.getBitmap() != null) {
            mAsyncFinishedObserver.get().processedImage(recyclerBitmap);
        }

        mCountDownLatch.countDown();
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        mAsyncFinishedObserver.get().asyncCancelled("Processing Image " + String.valueOf(mIndex) + " canceled");
        mCountDownLatch.countDown();
    }
}
