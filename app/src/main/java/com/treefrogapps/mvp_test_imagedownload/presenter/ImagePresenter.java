package com.treefrogapps.mvp_test_imagedownload.presenter;


import android.graphics.Bitmap;

import com.treefrogapps.mvp_test_imagedownload.MVP;
import com.treefrogapps.mvp_test_imagedownload.model.ImageModel;
import com.treefrogapps.mvp_test_imagedownload.recyclerview.RecyclerBitmap;
import com.treefrogapps.mvp_test_imagedownload.utils.ViewContext;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class ImagePresenter implements MVP.PresenterInterface, MVP.DownloadFinishedObserver {

    public static final String PRESENTER_KEY = "com.treefrogapps.mvp_test_imagedownload.presenter.key";

    private WeakReference<MVP.ViewInterface> mViewInterface;
    private ImageModel mImageModel;
    private MVP.DownloadFinishedObserver mDownloadFinishedObserver;
    private ArrayList<RecyclerBitmap> mRecyclerBitmaps;

    public ImagePresenter(MVP.ViewInterface viewInterface){

        this.mViewInterface = new WeakReference<>(viewInterface);
        this.mImageModel = new ImageModel();
        this.mDownloadFinishedObserver = this;
    }

    @Override
    public void onCreate() {

        // TODO - storage location
    }

    @Override
    public ArrayList<RecyclerBitmap> recyclerBitmaps() {

        // TODO - all the bitmaps from the storage location loaded in multiple async tasks
        return null;
    }


    @Override
    public void handleButtonClick(ViewContext viewContext, String url) {
        mImageModel.downloadBitmap(viewContext, this, url);
    }



    @Override
    public boolean downloadSuccess() {
        return false;
        // TODO
    }


    @Override
    public void downloadedImage(Bitmap bitmap) {

        // TODO

    }
}
