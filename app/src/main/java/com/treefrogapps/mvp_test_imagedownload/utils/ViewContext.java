package com.treefrogapps.mvp_test_imagedownload.utils;

import com.treefrogapps.mvp_test_imagedownload.MVP;

import java.lang.ref.WeakReference;

/**
 * Create a context to the view layer - used to update to UI
 */


public class ViewContext {

    public static final String VIEW_CONTEXT_KEY = "com.treefrogapps.mvp_test_imagedownload.utils.viewcontext.key";

    private final WeakReference<MVP.ViewInterface> mView;

    public ViewContext(MVP.ViewInterface mView) {

        this.mView = new WeakReference<MVP.ViewInterface>(mView);
    }

    public WeakReference<MVP.ViewInterface> getmView() {
        return mView;
    }
}
