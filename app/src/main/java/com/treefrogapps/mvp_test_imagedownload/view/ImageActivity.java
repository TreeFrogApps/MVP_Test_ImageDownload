package com.treefrogapps.mvp_test_imagedownload.view;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.treefrogapps.mvp_test_imagedownload.MVP;
import com.treefrogapps.mvp_test_imagedownload.R;
import com.treefrogapps.mvp_test_imagedownload.presenter.ImagePresenter;
import com.treefrogapps.mvp_test_imagedownload.recyclerview.ImageRecyclerAdapter;
import com.treefrogapps.mvp_test_imagedownload.recyclerview.RecyclerBitmap;
import com.treefrogapps.mvp_test_imagedownload.retainedfragment.RetainedFragment;
import com.treefrogapps.mvp_test_imagedownload.utils.ViewContext;

import java.util.ArrayList;


public class ImageActivity extends AppCompatActivity implements MVP.ViewInterface, View.OnClickListener {

    // Permissions at runtime - sdk23 >
    public static boolean READ_PERMISSION;
    public static boolean WRITE_PERMISSION;
    private static int PERMISSION_REQUEST_CODE = 10;

    private RetainedFragment mRetainedFragment;

    private Toolbar mToolbar;
    private EditText urlEditText;
    private FloatingActionButton goButton, addButton;
    private TextView downloadCountTV, imageCountTV;

    private RecyclerView recyclerView;
    private ArrayList<RecyclerBitmap> recyclerBitmaps;
    private ImageRecyclerAdapter imageRecyclerAdapter;

    private ImagePresenter mImagePresenter;
    private ViewContext mViewContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        if (mToolbar != null) mToolbar.showOverflowMenu();

        /**
         * Initialise the Field Variables
         * Get an Instance of the retained fragment to hold reference to the Presenter layer
         *
         */

        initialiseFragmentAndPresenter();
        initialiseUI();


    }

    @Override
    public void getPermissions() {

        // Get Read/Write permissions - sdk23 > ------------------------------------------------------------

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            READ_PERMISSION = true;
            WRITE_PERMISSION = true;
        } else {
            READ_PERMISSION = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED;

            WRITE_PERMISSION = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED;

            Log.i("PERMISSIONS", "READ " + String.valueOf(READ_PERMISSION) + ", WRITE " + String.valueOf(WRITE_PERMISSION));

            if(!READ_PERMISSION || !WRITE_PERMISSION){
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CODE){

            if (grantResults[0] != PackageManager.PERMISSION_GRANTED && grantResults[1] != PackageManager.PERMISSION_GRANTED){
                showToast("Permissions not granted - app will not function correctly");
            } else {
                READ_PERMISSION = true;
                WRITE_PERMISSION = true;
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        }
    }

    private void initialiseFragmentAndPresenter() {

        FragmentManager fragmentManager = getSupportFragmentManager();
        mRetainedFragment = (RetainedFragment) fragmentManager.findFragmentByTag(RetainedFragment.RETAINED_FRAGMENT_TAG);
        if (mRetainedFragment == null) {
            mRetainedFragment = new RetainedFragment();
            fragmentManager.beginTransaction().add(mRetainedFragment, RetainedFragment.RETAINED_FRAGMENT_TAG).commit();
        }

        // handle config change by getting the instance of the presenter
        mImagePresenter = (ImagePresenter) mRetainedFragment.getObject(ImagePresenter.PRESENTER_KEY);

        // if null first time in
        if (mImagePresenter == null) mImagePresenter = new ImagePresenter();

        // initialise the view layer to the presenter layer
        mViewContext = new ViewContext(this);

        mImagePresenter.onCreate(mViewContext);
    }

    private void initialiseUI() {

        urlEditText = (EditText) findViewById(R.id.urlEditText);
        downloadCountTV = (TextView) findViewById(R.id.downloadCountTextView);
        downloadCountTV.setText(String.valueOf(mImagePresenter.getDownloadCount()));
        imageCountTV = (TextView) findViewById(R.id.imageCountTextView);
        imageCountTV.setText(String.valueOf(mImagePresenter.getImageCount()));
        addButton = (FloatingActionButton) findViewById(R.id.addFAB);
        addButton.setOnClickListener(this);
        goButton = (FloatingActionButton) findViewById(R.id.downloadFAB);
        goButton.setOnClickListener(this);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false));

        recyclerBitmaps = mImagePresenter.recyclerBitmaps();
        imageRecyclerAdapter = new ImageRecyclerAdapter(this, recyclerBitmaps, mImagePresenter);
        recyclerView.setAdapter(imageRecyclerAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.deleteImages:

                /**
                 * Only delete images if processing or downloading of images is not in operation
                 */

                if(!mImagePresenter.isProcessing() && mImagePresenter.getImageCount() > 0){
                    mImagePresenter.deleteImages();
                }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showToast(final String toastMessage) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), toastMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void updateRecyclerView() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                recyclerBitmaps = mImagePresenter.recyclerBitmaps();
                imageRecyclerAdapter.notifyDataSetChanged();
                imageCountTV.setText(String.valueOf(mImagePresenter.getImageCount()));
            }
        });
    }

    @Override
    public void updateDownloadCount() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                downloadCountTV.setText(String.valueOf(mImagePresenter.getDownloadCount()));

            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.downloadFAB:
                Log.i("Button Pressed", "Download");
                if (mImagePresenter.connectionStatus(this) && READ_PERMISSION && WRITE_PERMISSION){
                    mImagePresenter.handleDownloads();
                }

                break;

            case R.id.addFAB:
                String url = urlEditText.getText().toString();
                if (!url.equals("") && url.length() > 9) {
                    Log.i("Button Pressed", "Add");
                    mImagePresenter.handleButtonClick(url);
                    urlEditText.setText("");
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // let the presenter know that this is a configuration change like rotation
        mImagePresenter.setConfigChange(true);

        // handle config changes by placing presenter into retained fragment
        mRetainedFragment.putObject(ImagePresenter.PRESENTER_KEY, mImagePresenter);

        if (isFinishing()) {
            mImagePresenter.interruptThread();
        }
    }
}
