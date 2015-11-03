package com.treefrogapps.mvp_test_imagedownload;

import android.graphics.Bitmap;

import com.treefrogapps.mvp_test_imagedownload.async.ImageDownloadAsyncTask;
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

        void handleDownloads(ViewContext viewContext);

        void onCreate(MVP.ViewInterface viewInterface);

        ArrayList<RecyclerBitmap> recyclerBitmaps();

        void shutdownAsyncTasks();

        void interruptThread();

        void deleteImages();

        int getDownloadCount();
    }

    interface ViewInterface {

        void showToast(String toastMessage);

        void updateRecyclerView();

        void updateDownloadCount();

    }

    interface ModelInterface {

        void downloadBitmaps(ViewContext viewContext, DownloadFinishedObserver downloadFinishedObserver,
                             ArrayList<String> urls, CountDownLatch countDownLatch);

        ArrayList<ImageDownloadAsyncTask> getImageAsyncTasks();

    }

    interface DownloadFinishedObserver {

        void downloadedImage(Bitmap bitmap);
    }
}
