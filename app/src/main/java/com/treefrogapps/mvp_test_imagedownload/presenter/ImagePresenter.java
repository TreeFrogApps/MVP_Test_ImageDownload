package com.treefrogapps.mvp_test_imagedownload.presenter;


import android.graphics.Bitmap;

import com.treefrogapps.mvp_test_imagedownload.MVP;
import com.treefrogapps.mvp_test_imagedownload.model.ImageModel;
import com.treefrogapps.mvp_test_imagedownload.utils.ViewContext;

import java.lang.ref.WeakReference;

public class ImagePresenter implements MVP.PresenterInterface, MVP.DownloadFinishedObserver {

    public static final String PRESENTER_KEY = "com.treefrogapps.mvp_test_imagedownload.presenter.key";

    private WeakReference<MVP.ViewInterface> viewInterface;
    private ImageModel mImageModel;
    private MVP.DownloadFinishedObserver downloadFinishedObserver;

    public ImagePresenter(MVP.ViewInterface viewInterface){

        this.viewInterface = new WeakReference<>(viewInterface);
        this.mImageModel = new ImageModel();
        this.downloadFinishedObserver = this;
    }



    @Override
    public void handleButtonClick(ViewContext viewContext, String url) {
        mImageModel.downloadBitmap(viewContext, this, url);
    }

    @Override
    public boolean downloadSuccess() {
        return false;
    }


    @Override
    public void downloadedImage(Bitmap bitmap) {



    }
}
