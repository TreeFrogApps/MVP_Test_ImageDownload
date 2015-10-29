package com.treefrogapps.mvp_test_imagedownload.presenter;


import android.graphics.Bitmap;

import com.treefrogapps.mvp_test_imagedownload.MVP;

import java.lang.ref.WeakReference;

public class ImagePresenter implements MVP.PresenterInterface {

    private WeakReference<MVP.ViewInterface> viewInterface;

    public ImagePresenter(MVP.ViewInterface viewInterface){

        this.viewInterface = new WeakReference<>(viewInterface);


    }



    @Override
    public void displayImage(Bitmap bitmap) {

    }

    @Override
    public void handleButtonClick(String url) {

    }

    @Override
    public boolean downloadSuccess() {
        return false;
    }
}
