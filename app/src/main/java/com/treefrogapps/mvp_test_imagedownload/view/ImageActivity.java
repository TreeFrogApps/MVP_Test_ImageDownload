package com.treefrogapps.mvp_test_imagedownload.view;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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

    private RetainedFragment mRetainedFragment;

    private Toolbar mToolbar;
    private EditText urlEditText;
    private FloatingActionButton goButton, addButton;
    private TextView downloadCountTV;

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

        FragmentManager fragmentManager = getSupportFragmentManager();
        mRetainedFragment = (RetainedFragment) fragmentManager.findFragmentByTag(RetainedFragment.RETAINED_FRAGMENT_TAG);
        if (mRetainedFragment == null) {
            mRetainedFragment = new RetainedFragment();
            fragmentManager.beginTransaction().add(mRetainedFragment, RetainedFragment.RETAINED_FRAGMENT_TAG).commit();
        }

        // handle config change by getting the instance of the presenter
        mImagePresenter = (ImagePresenter) mRetainedFragment.getObject(ImagePresenter.PRESENTER_KEY);
        mViewContext = (ViewContext) mRetainedFragment.getObject(ViewContext.VIEW_CONTEXT_KEY);
        //if null first time in
        if (mImagePresenter == null) mImagePresenter = new ImagePresenter();
        if (mViewContext == null) {
            Log.e("View Context", "NULL");
            mViewContext = new ViewContext(this);
        }

        mImagePresenter.onCreate(mViewContext);
        initialiseUI();

    }

    private void initialiseUI() {

        urlEditText = (EditText) findViewById(R.id.urlEditText);
        downloadCountTV = (TextView) findViewById(R.id.downloadCountTextView);
        downloadCountTV.setText(String.valueOf(mImagePresenter.getDownloadCount()));
        addButton = (FloatingActionButton) findViewById(R.id.addFAB);
        addButton.setOnClickListener(this);
        goButton = (FloatingActionButton) findViewById(R.id.downloadFAB);
        goButton.setOnClickListener(this);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),
                2, GridLayoutManager.VERTICAL, false));

        recyclerBitmaps = mImagePresenter.recyclerBitmaps();
        imageRecyclerAdapter = new ImageRecyclerAdapter(this, recyclerBitmaps);
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

                mImagePresenter.deleteImages();
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
                mImagePresenter.handleDownloads(mViewContext);
                break;

            case R.id.addFAB:
                String url = urlEditText.getText().toString();
                if (!url.equals("")) {
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

        // handle config changes by placing presenter into retained fragment
        mRetainedFragment.putObject(ImagePresenter.PRESENTER_KEY, mImagePresenter);
        mRetainedFragment.putObject(ViewContext.VIEW_CONTEXT_KEY, mViewContext);

        if (isFinishing()) {
            mImagePresenter.interruptThread();
        }
    }
}
