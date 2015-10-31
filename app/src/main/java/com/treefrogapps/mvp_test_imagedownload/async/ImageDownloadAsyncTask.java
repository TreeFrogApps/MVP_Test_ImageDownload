package com.treefrogapps.mvp_test_imagedownload.async;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.treefrogapps.mvp_test_imagedownload.MVP;
import com.treefrogapps.mvp_test_imagedownload.utils.ImageUtils;
import com.treefrogapps.mvp_test_imagedownload.utils.ViewContext;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Custom Asynctask to handle download of image
 */
public class ImageDownloadAsyncTask extends AsyncTask<String, Void, Bitmap> {

    private String url;
    private WeakReference<MVP.ViewInterface> viewContext;
    private WeakReference<MVP.DownloadFinishedObserver> downloadFinishedObserver;

    public ImageDownloadAsyncTask(ViewContext viewContext, MVP.DownloadFinishedObserver downloadFinishedObserver){
        this.viewContext = viewContext.getmView();
        this.downloadFinishedObserver = new WeakReference<>(downloadFinishedObserver);
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

        Bitmap bitmap = null;

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
                    bitmap = BitmapFactory.decodeStream(inputStream);
                } else {
                   return null;
                }

                inputStream.close();
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
        } else {
            viewContext.get().showToast("Error - not a valid file format");
        }
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();

        viewContext.get().showToast("Download cancelled");
    }
}
