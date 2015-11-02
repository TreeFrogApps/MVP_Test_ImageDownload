package com.treefrogapps.mvp_test_imagedownload.presenter;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import com.treefrogapps.mvp_test_imagedownload.MVP;
import com.treefrogapps.mvp_test_imagedownload.model.ImageModel;
import com.treefrogapps.mvp_test_imagedownload.recyclerview.RecyclerBitmap;
import com.treefrogapps.mvp_test_imagedownload.utils.ViewContext;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

public class ImagePresenter implements MVP.PresenterInterface, MVP.DownloadFinishedObserver {

    public static final String PRESENTER_KEY = "com.treefrogapps.mvp_test_imagedownload.presenter.key";

    private WeakReference<MVP.ViewInterface> mViewInterface;
    private ImageModel mImageModel;
    private MVP.DownloadFinishedObserver mDownloadFinishedObserver;
    private String mFolderLocation;
    private File mFileFolder;
    private ArrayList<String> mImagesToDownload;
    private ArrayList<RecyclerBitmap> mRecyclerBitmaps;
    private CountDownLatch mCountDownLatch;

    public ImagePresenter(){

        this.mImageModel = new ImageModel();
        this.mDownloadFinishedObserver = this;
        this.mImagesToDownload = new ArrayList<>();
        this.mRecyclerBitmaps = new ArrayList<>();
    }

    @Override
    public void onCreate(MVP.ViewInterface viewInterface) {

        // link / relink the viewInterface
        this.mViewInterface = new WeakReference<>(viewInterface);

        mFolderLocation = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/Assignment_Images";
        mFileFolder = new File(mFolderLocation);
        if (!mFileFolder.exists()){
            mFileFolder.mkdirs();
            Log.i("Folder", "Created");
        }

        if (mRecyclerBitmaps.size() == 0){

            File[] fileArray = mFileFolder.listFiles();
            if(fileArray.length > 0) {
                for (File file : fileArray) {
                    mRecyclerBitmaps.add(new RecyclerBitmap(BitmapFactory.decodeFile(file.getAbsolutePath())));
                }
            }
        }
    }

    @Override
    public ArrayList<RecyclerBitmap> recyclerBitmaps() {
        return mRecyclerBitmaps;
    }


    @Override
    public void handleButtonClick(String url) {
        mImagesToDownload.add(url);
    }

    @Override
    public void handleDownloads(ViewContext viewContext) {

        mCountDownLatch = new CountDownLatch(mImagesToDownload.size());

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mCountDownLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                   if (mViewInterface != null) mViewInterface.get().updateRecyclerView();
                }
            }
        }).start();

        mImageModel.downloadBitmaps(viewContext, this, mImagesToDownload, mCountDownLatch);

    }


    @Override
    public boolean downloadSuccess() {
        return false;
        // TODO
    }



    @Override
    public void downloadedImage(Bitmap bitmap) {

        synchronized (this){
            RecyclerBitmap recyclerBitmap = new RecyclerBitmap(bitmap);
            mRecyclerBitmaps.add(recyclerBitmap);
        }

    }
}
