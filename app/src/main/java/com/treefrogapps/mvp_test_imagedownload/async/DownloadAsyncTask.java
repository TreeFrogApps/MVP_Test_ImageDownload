package com.treefrogapps.mvp_test_imagedownload.async;

import android.os.AsyncTask;
import android.util.Log;

import com.treefrogapps.mvp_test_imagedownload.MVP;
import com.treefrogapps.mvp_test_imagedownload.utils.ImageThreadPool;
import com.treefrogapps.mvp_test_imagedownload.utils.ImageUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.CountDownLatch;

/**
 * Custom Asynctask to handle download of image
 */
public class DownloadAsyncTask extends AsyncTask<String, Void, String> {

    private String fileLocation;
    private MVP.AsyncFinishedObserver mAsyncFinishedObserver;
    private CountDownLatch mCountDownLatch;
    private int mIndex;

    public DownloadAsyncTask(int index, MVP.AsyncFinishedObserver asyncFinishedObserver,
                             CountDownLatch countDownLatch) {

        this.mCountDownLatch = countDownLatch;
        this.mIndex = index;
        this.mAsyncFinishedObserver = asyncFinishedObserver;
    }

    @Override
    protected String doInBackground(String... params) {

        Log.i("DownloadAsyncTask", String.valueOf(mIndex) + " doInBackground started");

        String fileLocation = null;

        this.fileLocation = params[0];

            HttpURLConnection httpURLConnection = null;
            try {
                URL imageUrl = new URL(this.fileLocation);
                httpURLConnection = (HttpURLConnection) imageUrl.openConnection();
                if (httpURLConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return null;
                }
                InputStream inputStream = httpURLConnection.getInputStream();

                if (ImageUtils.isValidImage(inputStream)) {
                    httpURLConnection = (HttpURLConnection) imageUrl.openConnection();
                    InputStream inputStream1 = httpURLConnection.getInputStream();

                    fileLocation = ImageUtils.saveBitmap(inputStream1);

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


        return fileLocation;
    }

    @Override
    protected void onPostExecute(String fileLocation) {
        super.onPostExecute(fileLocation);

        if (fileLocation != null) {
            ImageAsyncTask imageAsyncTask = new ImageAsyncTask(mIndex, mAsyncFinishedObserver, mCountDownLatch);
            imageAsyncTask.executeOnExecutor(ImageThreadPool.IMAGE_THREAD_POOL_EXECUTOR, fileLocation);
        }
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();

    }
}
