package com.treefrogapps.mvp_test_imagedownload;

import android.graphics.Bitmap;

import com.treefrogapps.mvp_test_imagedownload.async.ImageAsyncTask;
import com.treefrogapps.mvp_test_imagedownload.recyclerview.RecyclerBitmap;
import com.treefrogapps.mvp_test_imagedownload.utils.ViewContext;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

/**
 * Create interfaces for MVP pattern
 * <p/>
 * Try and base on MVP pattern providing layer separation
 * of tasks
 */


public interface MVP {

    interface PresenterInterface {

        void handleButtonClick(String url);

        void handleDownloads(ViewContext viewContext);

        void onCreate(ViewContext viewContext);

        ArrayList<RecyclerBitmap> recyclerBitmaps();

        void shutdownAsyncTasks();

        void interruptThread();

        void deleteImages();

        int getDownloadCount();
    }

    interface ViewInterface {

        void showToast(final String toastMessage);

        void updateRecyclerView();

        void updateDownloadCount();

    }

    interface ModelInterface {

        void processBitmaps(int asyncType, ViewContext viewContext, AsyncFinishedObserver asyncFinishedObserver,
                            ArrayList<String> fileLocation, CountDownLatch countDownLatch);

        ArrayList<ImageAsyncTask> getImageAsyncTasks();

        Bitmap imageLoader(File imageFile);

    }

    interface AsyncFinishedObserver {

        void processedImage(Bitmap bitmap);

        void downloadedImage(String fileLocation);
    }
}
