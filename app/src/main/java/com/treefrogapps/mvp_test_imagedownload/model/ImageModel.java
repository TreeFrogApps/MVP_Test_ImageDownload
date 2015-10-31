package com.treefrogapps.mvp_test_imagedownload.model;


import com.treefrogapps.mvp_test_imagedownload.MVP;
import com.treefrogapps.mvp_test_imagedownload.async.ImageDownloadAsyncTask;
import com.treefrogapps.mvp_test_imagedownload.utils.ViewContext;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ImageModel implements MVP.ModelInterface {

    public static final String MODEL_KEY = "com.treefrogapps.mvp_test_imagedownload.model.key";

    private ImageDownloadAsyncTask mImageDownloadTask;


    @Override
    public void downloadBitmap(ViewContext viewContext, MVP.DownloadFinishedObserver downloadFinishedObserver, String url) {

        this.mImageDownloadTask = new ImageDownloadAsyncTask(viewContext, downloadFinishedObserver);

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1,1,0,TimeUnit.MILLISECONDS,
                new LinkedBlockingDeque<Runnable>(1));

        mImageDownloadTask.executeOnExecutor(threadPoolExecutor, url);
    }
}
