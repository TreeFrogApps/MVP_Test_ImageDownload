package com.treefrogapps.mvp_test_imagedownload;

import android.content.Context;

import com.treefrogapps.mvp_test_imagedownload.async.DownloadAsyncTask;
import com.treefrogapps.mvp_test_imagedownload.async.ImageAsyncTask;
import com.treefrogapps.mvp_test_imagedownload.recyclerview.RecyclerBitmap;
import com.treefrogapps.mvp_test_imagedownload.utils.ViewContext;

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

        void handleDownloads();

        void onCreate(ViewContext viewContext);

        ArrayList<RecyclerBitmap> recyclerBitmaps();

        void shutdownAsyncTasks();

        void interruptThread();

        void deleteImages();

        int getDownloadCount();

        int getImageCount();

        void handleRecyclerButtonClick(Context context, String fileLocation);

        boolean connectionStatus(Context context);

        void setConfigChange(boolean isConfigChange);

        boolean isDownloading();
    }

    interface ViewInterface {

        void showToast(final String toastMessage);

        void updateRecyclerView();

        void updateDownloadCount();

    }

    interface ModelInterface {

        void downloadBitmaps(ViewContext viewContext, AsyncFinishedObserver asyncFinishedObserver,
                             ArrayList<String> fileLocation, CountDownLatch countDownLatch);

        void processBitmaps(ViewContext viewContext, AsyncFinishedObserver asyncFinishedObserver,
                            ArrayList<String> fileLocation, CountDownLatch countDownLatch);

        ArrayList<DownloadAsyncTask> getDownloadAsyncTasks();

        ArrayList<ImageAsyncTask> getImageAsyncTasks();

    }

    interface AsyncFinishedObserver {

        void processedImage(RecyclerBitmap recyclerBitmap);

        void asyncCancelled(String message);

    }
}
