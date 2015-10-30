package com.treefrogapps.mvp_test_imagedownload.presenter;


import com.treefrogapps.mvp_test_imagedownload.MVP;
import com.treefrogapps.mvp_test_imagedownload.model.ImageModel;
import com.treefrogapps.mvp_test_imagedownload.utils.ViewContext;

import java.lang.ref.WeakReference;

public class ImagePresenter implements MVP.PresenterInterface {

    private WeakReference<MVP.ViewInterface> viewInterface;
    private ImageModel mImageModel;

    public ImagePresenter(MVP.ViewInterface viewInterface){

        this.viewInterface = new WeakReference<>(viewInterface);
        this.mImageModel = new ImageModel();
    }



    @Override
    public void handleButtonClick(ViewContext viewContext, String url) {

        mImageModel.downloadBitmap(viewContext, url);

    }

    @Override
    public boolean downloadSuccess() {
        return false;
    }
}
