package com.treefrogapps.mvp_test_imagedownload.async;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import com.treefrogapps.mvp_test_imagedownload.MVP;
import com.treefrogapps.mvp_test_imagedownload.model.ImageModel;
import com.treefrogapps.mvp_test_imagedownload.utils.ImageUtils;
import com.treefrogapps.mvp_test_imagedownload.utils.ViewContext;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.CountDownLatch;

/**
 * Custom Asynctask to handle download of image
 */
public class ImageAsyncTask extends AsyncTask<String, Void, Bitmap> {

    private String fileLocation;
    private WeakReference<MVP.ViewInterface> viewContext;
    private WeakReference<MVP.AsyncFinishedObserver> downloadFinishedObserver;
    private CountDownLatch mCountDownLatch;
    private int mIndex;
    private int mAsyncType;

    public ImageAsyncTask(int asyncType, int index, ViewContext viewContext, MVP.AsyncFinishedObserver asyncFinishedObserver,
                          CountDownLatch countDownLatch) {
        this.viewContext = viewContext.getmView();
        this.downloadFinishedObserver = new WeakReference<>(asyncFinishedObserver);
        this.mCountDownLatch = countDownLatch;
        this.mIndex = index;
        this.mAsyncType = asyncType;
    }

    @Override
    protected Bitmap doInBackground(String... params) {

        Log.i("AsyncTask", String.valueOf(mIndex) + " doInBackground started");

        Bitmap bitmap = null;
        this.fileLocation = params[0];


        if (mAsyncType == ImageModel.DOWNLOAD_AND_PROCESS_IMAGES) {

            HttpURLConnection httpURLConnection = null;
            try {
                URL imageUrl = new URL(fileLocation);
                httpURLConnection = (HttpURLConnection) imageUrl.openConnection();
                if (httpURLConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return null;
                }
                InputStream inputStream = httpURLConnection.getInputStream();

                if (ImageUtils.isValidImage(inputStream)) {
                    httpURLConnection = (HttpURLConnection) imageUrl.openConnection();
                    InputStream inputStream1 = httpURLConnection.getInputStream();
                    bitmap = ImageUtils.scaleFilteredBitmap(inputStream1);

                    inputStream.close();
                    inputStream1.close();

                } else {
                    return null;
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
            }
        } else {

            File imageFile = new File(fileLocation);
            bitmap = ImageUtils.imageLoader(imageFile);

        }

        return bitmap;
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
