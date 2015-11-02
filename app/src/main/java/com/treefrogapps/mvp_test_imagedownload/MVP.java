package com.treefrogapps.mvp_test_imagedownload;

import android.graphics.Bitmap;

import com.treefrogapps.mvp_test_imagedownload.recyclerview.RecyclerBitmap;
import com.treefrogapps.mvp_test_imagedownload.utils.ViewContext;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

/**
 * Create interfaces for MVP pattern
 *
 * Try and base on MVP pattern providing layer separation
 * of tasks
 */


public interface MVP {

    interface PresenterInterface {
        void handleButtonClick(String url);
        void handleDownloads(ViewContext viewContext);
        void onCreate(MVP.ViewInterface viewInterface);
        ArrayList<RecyclerBitmap> recyclerBitmaps();
        boolean downloadSuccess();
    }

    interface ViewInterface {
        void showToast(String toastMessage);
        void displayImage(Bitmap bitmap);
        void updateRecyclerView();
    }

    interface ModelInterface {
        void downloadBitmaps(ViewContext viewContext, DownloadFinishedObserver downloadFinishedObserver,
                             ArrayList<String> urls, CountDownLatch countDownLatch);

    }

    interface DownloadFinishedObserver {
        void downloadedImage(Bitmap bitmap);
    }
}
