package com.treefrogapps.mvp_test_imagedownload.async;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import com.treefrogapps.mvp_test_imagedownload.MVP;
import com.treefrogapps.mvp_test_imagedownload.utils.ImageUtils;
import com.treefrogapps.mvp_test_imagedownload.utils.ViewContext;

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
public class ImageDownloadAsyncTask extends AsyncTask<String, Void, Bitmap> {

    private String url;
    private WeakReference<MVP.ViewInterface> viewContext;
    private WeakReference<MVP.DownloadFinishedObserver> downloadFinishedObserver;
    private CountDownLatch mCountDownLatch;
    private int mIndex;

    public ImageDownloadAsyncTask(int index, ViewContext viewContext, MVP.DownloadFinishedObserver downloadFinishedObserver,
                                  CountDownLatch countDownLatch) {
        this.viewContext = viewContext.getmView();
        this.downloadFinishedObserver = new WeakReference<>(downloadFinishedObserver);
        this.mCountDownLatch = countDownLatch;
        this.mIndex = index;
    }

    @Override
    protected Bitmap doInBackground(String... params) {

        Log.i("AsyncTask", String.valueOf(mIndex) + " doInBackground started");

        this.url = params[0];

        Bitmap bitmap = null;

        HttpURLConnection httpURLConnection = null;
        try {
            URL imageUrl = new URL(url);
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

        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);

        if (bitmap != null) {
            downloadFinishedObserver.get().downloadedImage(bitmap);
        }

        mCountDownLatch.countDown();

    }

    @Override
    protected void onCancelled() {
        super.onCancelled();

        if (viewContext.get() != null)
            viewContext.get().showToast("Download " + String.valueOf(mIndex) + " cancelled");
        mCountDownLatch.countDown();
    }
}
