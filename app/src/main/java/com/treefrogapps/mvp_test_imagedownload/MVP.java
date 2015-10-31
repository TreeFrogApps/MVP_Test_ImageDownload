package com.treefrogapps.mvp_test_imagedownload;

import android.graphics.Bitmap;

import com.treefrogapps.mvp_test_imagedownload.recyclerview.RecyclerBitmap;
import com.treefrogapps.mvp_test_imagedownload.utils.ViewContext;

import java.util.ArrayList;

/**
 * Create interfaces for MVP pattern
 *
 * Try and base on MVP pattern providing layer separation
 * of tasks
 */


public interface MVP {

    interface PresenterInterface {

        void handleButtonClick(ViewContext viewContext, String url);
        void onCreate();
        ArrayList<RecyclerBitmap> recyclerBitmaps();
        boolean downloadSuccess();
    }

    interface ViewInterface {

        void showToast(String toastMessage);
        void displayImage(Bitmap bitmap);
        void updateRecyclerView();
    }

    interface ModelInterface {

        void downloadBitmap(ViewContext viewContext, DownloadFinishedObserver downloadFinishedObserver,  String url);
    }

    interface DownloadFinishedObserver {

        void downloadedImage(Bitmap bitmap);

    }
}
