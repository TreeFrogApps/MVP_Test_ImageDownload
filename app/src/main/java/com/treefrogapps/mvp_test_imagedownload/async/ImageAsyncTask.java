package com.treefrogapps.mvp_test_imagedownload.async;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.treefrogapps.mvp_test_imagedownload.MVP;
import com.treefrogapps.mvp_test_imagedownload.utils.ViewContext;

import java.lang.ref.WeakReference;

/**
 * Custom Asynctask to handle download of image
 */
public class ImageAsyncTask extends AsyncTask<String, Void, Bitmap> {

    private String url;
    private WeakReference<MVP.ViewInterface> viewContext;

    public ImageAsyncTask(ViewContext viewContext){
        this.viewContext = viewContext.getmView();
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

        // TODO

        return null;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);


        // TODO - check image format and run new asynctask to perform a filter on the image
        viewContext.get().displayImage(bitmap);

    }

    @Override
    protected void onCancelled() {
        super.onCancelled();

        // TODO
    }
}
