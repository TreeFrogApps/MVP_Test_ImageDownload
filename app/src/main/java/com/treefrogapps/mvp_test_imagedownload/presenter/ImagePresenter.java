package com.treefrogapps.mvp_test_imagedownload.presenter;


import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import com.treefrogapps.mvp_test_imagedownload.MVP;
import com.treefrogapps.mvp_test_imagedownload.async.ImageAsyncTask;
import com.treefrogapps.mvp_test_imagedownload.model.ImageModel;
import com.treefrogapps.mvp_test_imagedownload.recyclerview.RecyclerBitmap;
import com.treefrogapps.mvp_test_imagedownload.utils.ImageUtils;
import com.treefrogapps.mvp_test_imagedownload.utils.ViewContext;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

public class ImagePresenter implements MVP.PresenterInterface, MVP.AsyncFinishedObserver {

    public static final String PRESENTER_KEY = "com.treefrogapps.mvp_test_imagedownload.presenter.key";
    public static final String FOLDER_LOCATION = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/Assignment_Images";

    private ViewContext mViewInterface;
    private ImageModel mImageModel;
    private MVP.AsyncFinishedObserver mAsyncFinishedObserver;
    private File mFileFolder;
    private ArrayList<String> mImagesToGet;
    private ArrayList<RecyclerBitmap> mRecyclerBitmaps;
    private CountDownLatch mCountDownLatch;
    private Thread mThread;
    private int mDownloadCount = 0;

    public ImagePresenter() {

        this.mImageModel = new ImageModel();
        this.mAsyncFinishedObserver = this;
        this.mImagesToGet = new ArrayList<>();
        this.mRecyclerBitmaps = new ArrayList<>();
    }

    @Override
    public void onCreate(ViewContext viewContext) {

        this.mViewInterface = viewContext;

        mFileFolder = new File(FOLDER_LOCATION);
        if (!mFileFolder.exists()) {
            mFileFolder.mkdirs();
            Log.i("Folder", "Created");
        }

        if (mRecyclerBitmaps.size() == 0) {

            File[] fileArray = mFileFolder.listFiles();
            if (fileArray != null) {
                if (fileArray.length > 0) {
                    for (File file : fileArray) {

                        mImagesToGet.add(file.getAbsolutePath());
                    }

                    mCountDownLatch = new CountDownLatch(mImagesToGet.size());

                    mThread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                mCountDownLatch.await();
                            } catch (InterruptedException e) {
                                Log.d("AsyncTask Interrupt", "Shutting down AsyncTasks");
                                shutdownAsyncTasks();
                            } finally {
                                mImagesToGet.clear();
                                if (mViewInterface.getmView().get() != null) {
                                    mViewInterface.getmView().get().updateRecyclerView();
                                }
                            }
                        }
                    });

                    mThread.start();
                    mImageModel.processBitmaps(ImageModel.PROCESS_IMAGES,
                            viewContext, this, mImagesToGet, mCountDownLatch);
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
        mImagesToGet.add(ImageUtils.checkURL(url));
        mDownloadCount++;
        mViewInterface.getmView().get().updateDownloadCount();
    }

    @Override
    public void deleteImages() {

        File[] fileArray = mFileFolder.listFiles();
        if (fileArray != null) {
            if (fileArray.length > 0) {
                for (File file : fileArray) {
                    file.delete();
                }
            }
        }
        mRecyclerBitmaps.clear();
        mViewInterface.getmView().get().updateRecyclerView();
        mViewInterface.getmView().get().showToast("Images Deleted");

    }

    @Override
    public int getDownloadCount() {
        return mDownloadCount;
    }

    @Override
    public void handleDownloads(ViewContext viewContext) {

        mCountDownLatch = new CountDownLatch(mImagesToGet.size());

        mThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    mCountDownLatch.await();
                } catch (InterruptedException e) {
                    Log.d("AsyncTask Interrupt", "Shutting down AsyncTasks");
                    shutdownAsyncTasks();
                } finally {
                    mDownloadCount = 0;
                    mImagesToGet.clear();
                    if (mViewInterface.getmView().get() != null) {
                        mViewInterface.getmView().get().updateRecyclerView();
                        mViewInterface.getmView().get().updateDownloadCount();

                        mViewInterface.getmView().get().showToast("Downloads Complete");
                    }
                }
            }
        });

        mThread.start();
        mImageModel.processBitmaps(ImageModel.DOWNLOAD_AND_PROCESS_IMAGES,
                viewContext, this, mImagesToGet, mCountDownLatch);
    }

    @Override
    public void processedImage(Bitmap bitmap) {

        synchronized (this) {
            RecyclerBitmap recyclerBitmap = new RecyclerBitmap(bitmap);
            mRecyclerBitmaps.add(recyclerBitmap);
        }

    }

    @Override
    public void downloadedImage(String fileLocation) {

    }

    @Override
    public void shutdownAsyncTasks() {

        // get current ImageAsyncTasks
        ArrayList<ImageAsyncTask> imageAsyncTasks = mImageModel.getImageAsyncTasks();

        synchronized (this) {
            // set each to cancel
            for (ImageAsyncTask task : imageAsyncTasks) {
                task.cancel(true);
            }
        }
    }

    @Override
    public void interruptThread() {
        // only interrupt if the app is running AsyncTasks
        if (mThread != null) mThread.interrupt();
    }


}
