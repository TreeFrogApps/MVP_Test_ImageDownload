package com.treefrogapps.mvp_test_imagedownload;

import android.graphics.Bitmap;

import com.treefrogapps.mvp_test_imagedownload.utils.ViewContext;

/**
 * Create interfaces for MVP pattern
 *
 * Try and base on MVP pattern providing layer separation
 * of tasks
 */


public interface MVP {

    interface PresenterInterface {

        void handleButtonClick(ViewContext viewContext, String url);
        boolean downloadSuccess();
    }

    interface ViewInterface {

        void showToast(String toastMessage);
        void displayImage(Bitmap bitmap);
        void updateRecyclerView();
    }

    interface ModelInterface {

        void downloadBitmap(ViewContext viewContext, String url);
    }
}
