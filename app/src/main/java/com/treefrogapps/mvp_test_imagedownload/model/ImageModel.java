package com.treefrogapps.mvp_test_imagedownload.model;


import android.graphics.Bitmap;

import com.treefrogapps.mvp_test_imagedownload.MVP;
import com.treefrogapps.mvp_test_imagedownload.async.ImageAsyncTask;
import com.treefrogapps.mvp_test_imagedownload.utils.ImageThreadPool;
import com.treefrogapps.mvp_test_imagedownload.utils.ImageUtils;
import com.treefrogapps.mvp_test_imagedownload.utils.ViewContext;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

public class ImageModel implements MVP.ModelInterface {

    public static final String MODEL_KEY = "com.treefrogapps.mvp_test_imagedownload.model.key";

    public static int DOWNLOAD_AND_PROCESS_IMAGES = 1;
    public static int PROCESS_IMAGES = 2;


    private ArrayList<ImageAsyncTask> mImageAsyncTasks;

    @Override
    public void processBitmaps(int asyncType, ViewContext viewContext, MVP.AsyncFinishedObserver asyncFinishedObserver,
                               ArrayList<String> fileLocation, CountDownLatch countDownLatch) {

        mImageAsyncTasks = new ArrayList<>();

        if (fileLocation.size() > 0) {

            if (asyncType == DOWNLOAD_AND_PROCESS_IMAGES)
                viewContext.getmView().get().showToast("Downloading Images");

            if (asyncType == PROCESS_IMAGES)
                viewContext.getmView().get().showToast("Loading Images");

            for (int i = 0; i < fileLocation.size(); i++) {
                mImageAsyncTasks.add(i, new ImageAsyncTask(asyncType, i, viewContext, asyncFinishedObserver, countDownLatch));
                mImageAsyncTasks.get(i).executeOnExecutor(ImageThreadPool.IMAGE_THREAD_POOL_EXECUTOR, fileLocation.get(i));
            }
        }
    }

    @Override
    public ArrayList<ImageAsyncTask> getImageAsyncTasks() {
        return mImageAsyncTasks;
    }

    @Override
    public Bitmap imageLoader(File imageFile) {

        // TODO - return using asynctask

        return ImageUtils.imageLoader(imageFile);
    }
}
