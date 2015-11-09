package com.treefrogapps.mvp_test_imagedownload.presenter;


import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;

import com.treefrogapps.mvp_test_imagedownload.MVP;
import com.treefrogapps.mvp_test_imagedownload.async.DownloadAsyncTask;
import com.treefrogapps.mvp_test_imagedownload.async.ImageAsyncTask;
import com.treefrogapps.mvp_test_imagedownload.model.ImageModel;
import com.treefrogapps.mvp_test_imagedownload.recyclerview.ImageRecyclerAdapter;
import com.treefrogapps.mvp_test_imagedownload.recyclerview.RecyclerBitmap;
import com.treefrogapps.mvp_test_imagedownload.utils.ConnectionStatus;
import com.treefrogapps.mvp_test_imagedownload.utils.ImageUtils;
import com.treefrogapps.mvp_test_imagedownload.utils.ViewContext;
import com.treefrogapps.mvp_test_imagedownload.view.ImageViewActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

public class ImagePresenter implements MVP.PresenterInterface, MVP.AsyncFinishedObserver {

    /**
     * Presenter Layer to handle all the logic of updating the UI and getting data from the Model layer
     *
     * Implements 2 interfaces, one from the MVP pattern, and another from the Observer pattern
     * When Async tasks have completed they callback to the presenter directly either to update the
     * UI with a toast, or a RecyclerBitmap object
     */

    public static final String PRESENTER_KEY = "com.treefrogapps.mvp_test_imagedownload.presenter.key";
    public static final String FOLDER_LOCATION = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/Assignment_Images";

    private ViewContext mViewInterface;
    private ImageModel mImageModel;
    private MVP.AsyncFinishedObserver mAsyncFinishedObserver;
    private File mFileFolder;
    private ArrayList<String> mImagesToGet;
    private ArrayList<RecyclerBitmap> mRecyclerBitmaps;
    private ArrayList<String> mDownloadedImages;
    private CountDownLatch mCountDownLatch;
    private Thread mThread;
    private int mDownloadCount = 0;

    public ImagePresenter() {

        this.mImageModel = new ImageModel();
        this.mAsyncFinishedObserver = this;
        this.mImagesToGet = new ArrayList<>();
        this.mRecyclerBitmaps = new ArrayList<>();
        this.mDownloadedImages = new ArrayList<>();
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

            final File[] fileArray = mFileFolder.listFiles();
            if (fileArray != null) {
                if (fileArray.length > 0) {

                    /**
                     * Create a new thread that adds to a ArrayList of strings
                     * for current images in the Assignment_Images folder
                     * Make the calling thread (Main thread/UI Thread) wait for the
                     * thread before continuing using .join() method
                     *
                     * A new thread is required otherwise on configuration change
                     * in UI Thread would start re-listing files and adding more than once
                     * this thread continues on config changes in the main thread.
                     */

                    Thread fileLoadThread = new Thread(new Runnable() {
                        @Override
                        public void run() {

                            for (File file : fileArray) {

                                mDownloadedImages.add(file.getAbsolutePath());
                            }
                        }
                    });

                    fileLoadThread.start();

                    try {
                        fileLoadThread.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    mCountDownLatch = new CountDownLatch(mDownloadedImages.size());

                    mThread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                mCountDownLatch.await();
                            } catch (InterruptedException e) {
                                Log.d("AsyncTask Interrupt", "Shutting down AsyncTasks");
                                shutdownAsyncTasks();
                            } finally {
                                mDownloadedImages.clear();
                                if (mViewInterface.getmView().get() != null) {
                                    mViewInterface.getmView().get().updateRecyclerView();
                                }
                            }
                        }
                    });

                    mThread.start();
                    mImageModel.processBitmaps(viewContext, this, mDownloadedImages, mCountDownLatch);
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

        String urlToAdd = ImageUtils.checkURL(url);

        if (!urlToAdd.equals("")) {
            mImagesToGet.add(urlToAdd);
            mDownloadCount++;
            mViewInterface.getmView().get().updateDownloadCount();
        } else {
            mViewInterface.getmView().get().showToast("Not a valid image url");
        }

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
    public int getImageCount() {
        return mRecyclerBitmaps.size();
    }

    @Override
    public void handleRecyclerButtonClick(Context context, String fileLocation) {

        Intent intent = new Intent(context, ImageViewActivity.class);

        intent.putExtra(ImageRecyclerAdapter.IMAGE_FILE_LOCATION, fileLocation);
        context.startActivity(intent);
    }

    @Override
    public boolean connectionStatus(Context context) {
        return ConnectionStatus.isConnected(context);
    }

    @Override
    public void handleDownloads() {

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
        mImageModel.downloadBitmaps(mViewInterface, this, mImagesToGet, mCountDownLatch);
    }

    @Override
    public void processedImage(RecyclerBitmap recyclerBitmap) {

        synchronized (this) {
            mRecyclerBitmaps.add(0, recyclerBitmap);
        }

    }

    @Override
    public void asyncCancelled(String message) {

        mViewInterface.getmView().get().showToast(message);
    }

    @Override
    public void shutdownAsyncTasks() {

        // get current ImageAsyncTasks
        ArrayList<DownloadAsyncTask> downloadAsyncTasks = mImageModel.getDownloadAsyncTasks();
        ArrayList<ImageAsyncTask> imageAsyncTasks = mImageModel.getImageAsyncTasks();

        synchronized (this) {

            // set each to cancel
            if (downloadAsyncTasks != null) {
                for (DownloadAsyncTask task : downloadAsyncTasks) {
                    task.cancel(true);
                }
            }

            if (imageAsyncTasks != null) {
                for (ImageAsyncTask task : imageAsyncTasks) {
                    task.cancel(true);
                }
            }
        }
    }

    @Override
    public void interruptThread() {
        // only interrupt if the app is running AsyncTasks
        if (mThread != null) mThread.interrupt();
    }


}
