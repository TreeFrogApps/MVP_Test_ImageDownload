package com.treefrogapps.mvp_test_imagedownload.async;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import com.treefrogapps.mvp_test_imagedownload.MVP;
import com.treefrogapps.mvp_test_imagedownload.utils.ImageUtils;

import java.lang.ref.WeakReference;
import java.util.concurrent.CountDownLatch;

/**
 * Custom Asynctask to handle download of image
 */
public class ImageAsyncTask extends AsyncTask<String, Void, Bitmap> {

    private WeakReference<MVP.ViewInterface> viewContext;
    private WeakReference<MVP.AsyncFinishedObserver> downloadFinishedObserver;
    private CountDownLatch mCountDownLatch;
    private int mIndex;

    public ImageAsyncTask(int index, MVP.AsyncFinishedObserver asyncFinishedObserver,
                          CountDownLatch countDownLatch) {
        this.downloadFinishedObserver = new WeakReference<>(asyncFinishedObserver);
        this.mCountDownLatch = countDownLatch;
        this.mIndex = index;

    }

    @Override
    protected Bitmap doInBackground(String... params) {

        Log.i("ImageAsyncTask", String.valueOf(mIndex) + " doInBackground started");

            String fileLocation = params[0];

        return ImageUtils.scaleFilteredBitmap(fileLocation);
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);

        if (bitmap != null) {
            downloadFinishedObserver.get().processedImage(bitmap);
        }

        mCountDownLatch.countDown();
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();

        if (viewContext.get() != null)
            viewContext.get().showToast("Processing " + String.valueOf(mIndex) + " cancelled");
        mCountDownLatch.countDown();
    }
}
