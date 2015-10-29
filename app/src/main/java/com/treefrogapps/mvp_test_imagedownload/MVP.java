package com.treefrogapps.mvp_test_imagedownload;

import android.graphics.Bitmap;

/**
 * Create interfaces for MVP pattern
 */


public interface MVP {

    interface PresenterInterface {

        void displayImage(Bitmap bitmap);
        void handleButtonClick(String url);
        boolean downloadSuccess();
    }

    interface ViewInterface {

        void onSuccess();
    }

    interface ModelInterface {


    }
}
