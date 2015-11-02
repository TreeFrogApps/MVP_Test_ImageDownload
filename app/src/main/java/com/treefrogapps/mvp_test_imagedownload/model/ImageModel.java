package com.treefrogapps.mvp_test_imagedownload.model;


import com.treefrogapps.mvp_test_imagedownload.MVP;
import com.treefrogapps.mvp_test_imagedownload.async.ImageDownloadAsyncTask;
import com.treefrogapps.mvp_test_imagedownload.utils.ViewContext;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ImageModel implements MVP.ModelInterface {

    public static final String MODEL_KEY = "com.treefrogapps.mvp_test_imagedownload.model.key";

    private static final int CORE_POOL_SIZE = 20;
    private static final int MAX_POOL_SIZE = 256;
    private static final int KEEP_ALIVE_TIME = 1000;

    @Override
    public void downloadBitmaps(ViewContext viewContext, MVP.DownloadFinishedObserver downloadFinishedObserver,
                                ArrayList<String> urls, CountDownLatch countDownLatch) {

        if (urls.size() > 0) {

            ImageDownloadAsyncTask imageDownloadTask;

            ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                    CORE_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.MILLISECONDS,
                    new LinkedBlockingDeque<Runnable>(MAX_POOL_SIZE));

            for (String url : urls) {
                imageDownloadTask = new ImageDownloadAsyncTask(viewContext, downloadFinishedObserver, countDownLatch);
                imageDownloadTask.executeOnExecutor(threadPoolExecutor, url);
            }
        }
    }
}
