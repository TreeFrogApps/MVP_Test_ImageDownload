package com.treefrogapps.mvp_test_imagedownload.async;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;

import com.treefrogapps.mvp_test_imagedownload.MVP;
import com.treefrogapps.mvp_test_imagedownload.utils.ImageUtils;
import com.treefrogapps.mvp_test_imagedownload.utils.ViewContext;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

/**
 * Custom Asynctask to handle download of image
 */
public class ImageDownloadAsyncTask extends AsyncTask<String, Void, Bitmap> {

    private String url;
    private WeakReference<MVP.ViewInterface> viewContext;
    private WeakReference<MVP.DownloadFinishedObserver> downloadFinishedObserver;
    private CountDownLatch mCountDownLatch;

    public ImageDownloadAsyncTask(ViewContext viewContext, MVP.DownloadFinishedObserver downloadFinishedObserver,
                                  CountDownLatch countDownLatch){
        this.viewContext = viewContext.getmView();
        this.downloadFinishedObserver = new WeakReference<>(downloadFinishedObserver);
        this.mCountDownLatch = countDownLatch;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        viewContext.get().showToast("Download image started");

        // TODO
    }

    @Override
    protected Bitmap doInBackground(String... params) {

        this.url = params[0];

        Uri uri = null;
        Bitmap bitmap = null;
        String filename;
        String folderLocation = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/";

        HttpURLConnection httpURLConnection = null;
        try {
            URL imageUrl = new URL(url);
            httpURLConnection = (HttpURLConnection) imageUrl.openConnection();

            if (httpURLConnection.getResponseCode() != HttpURLConnection.HTTP_OK){
                return null;
            }

            InputStream inputStream = httpURLConnection.getInputStream();

            if (inputStream !=null){

                if (ImageUtils.isValidImage(inputStream)){
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh_mm_ss");
                    filename = sdf.format(new Date());
                    File imageFile = new File(folderLocation + filename);
                    uri = Uri.parse(folderLocation + filename);
                    FileOutputStream fileOutputStream = new FileOutputStream(imageFile);

                    byte[] buffer = new byte[1024];
                    int read;
                    while ((read = inputStream.read(buffer)) != -1) {
                        fileOutputStream.write(buffer, 0, read);
                    }

                    inputStream.close();
                    fileOutputStream.close();
                } else {
                   return null;
                }
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (httpURLConnection != null){
                httpURLConnection.disconnect();
            }
        }

        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);

        if (bitmap != null){
            downloadFinishedObserver.get().downloadedImage(bitmap);
            mCountDownLatch.countDown();
        } else {
            viewContext.get().showToast("Error - not a valid file format");
            mCountDownLatch.countDown();
        }
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();

        viewContext.get().showToast("Download cancelled");
        mCountDownLatch.countDown();
    }
}
