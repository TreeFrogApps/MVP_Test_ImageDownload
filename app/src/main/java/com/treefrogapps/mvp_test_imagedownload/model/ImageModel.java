package com.treefrogapps.mvp_test_imagedownload.model;


import com.treefrogapps.mvp_test_imagedownload.MVP;
import com.treefrogapps.mvp_test_imagedownload.async.DownloadAsyncTask;
import com.treefrogapps.mvp_test_imagedownload.async.ImageAsyncTask;
import com.treefrogapps.mvp_test_imagedownload.utils.ImageThreadPool;
import com.treefrogapps.mvp_test_imagedownload.utils.ViewContext;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

public class ImageModel implements MVP.ModelInterface {

    public static final String MODEL_KEY = "com.treefrogapps.mvp_test_imagedownload.model.key";


    private ArrayList<DownloadAsyncTask> mDownloadAsyncTasks;
    private ArrayList<ImageAsyncTask> mImageAsyncTasks;

    @Override
    public void downloadBitmaps(ViewContext viewContext, MVP.AsyncFinishedObserver asyncFinishedObserver,
                                ArrayList<String> fileLocation, CountDownLatch countDownLatch) {

        mDownloadAsyncTasks = new ArrayList<>();

        if (fileLocation.size() > 0) {

            viewContext.getmView().get().showToast("Downloading Images");

            for (int i = 0; i < fileLocation.size(); i++) {
                mDownloadAsyncTasks.add(i, new DownloadAsyncTask(i, asyncFinishedObserver, countDownLatch));
                mDownloadAsyncTasks.get(i).executeOnExecutor(ImageThreadPool.IMAGE_THREAD_POOL_EXECUTOR, fileLocation.get(i));
            }
        }
    }

    @Override
    public void processBitmaps(ViewContext viewContext, MVP.AsyncFinishedObserver asyncFinishedObserver,
                               ArrayList<String> fileLocation, CountDownLatch countDownLatch) {

        mImageAsyncTasks = new ArrayList<>();

        if (fileLocation.size() > 0) {

            viewContext.getmView().get().showToast("Loading Images");

            for (int i = 0; i < fileLocation.size(); i++) {
                mImageAsyncTasks.add(i, new ImageAsyncTask(i, asyncFinishedObserver, countDownLatch));
                mImageAsyncTasks.get(i).executeOnExecutor(ImageThreadPool.IMAGE_THREAD_POOL_EXECUTOR, fileLocation.get(i));
            }
        }
    }

    @Override
    public ArrayList<DownloadAsyncTask> getDownloadAsyncTasks() {
        return mDownloadAsyncTasks;
    }

    @Override
    public ArrayList<ImageAsyncTask> getImageAsyncTasks() {
        return mImageAsyncTasks;
    }


}
