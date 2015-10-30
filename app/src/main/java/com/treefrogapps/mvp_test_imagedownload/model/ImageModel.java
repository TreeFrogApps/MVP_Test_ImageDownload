package com.treefrogapps.mvp_test_imagedownload.model;


import com.treefrogapps.mvp_test_imagedownload.MVP;
import com.treefrogapps.mvp_test_imagedownload.async.ImageAsyncTask;
import com.treefrogapps.mvp_test_imagedownload.utils.ViewContext;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ImageModel implements MVP.ModelInterface {

    private ImageAsyncTask mImageDownloadTask;


    @Override
    public void downloadBitmap(ViewContext viewContext, String url) {

        this.mImageDownloadTask = new ImageAsyncTask(viewContext);

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1,1,0,TimeUnit.MILLISECONDS,
                new LinkedBlockingDeque<Runnable>(1));

        mImageDownloadTask.executeOnExecutor(threadPoolExecutor, url);
    }
}
