package com.treefrogapps.mvp_test_imagedownload.view;

import android.support.v4.app.FragmentManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    private Button goButton;
    private Button addButton;

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
        if (mToolbar !=null) mToolbar.showOverflowMenu();

        FragmentManager fragmentManager = getSupportFragmentManager();
        mRetainedFragment = (RetainedFragment) fragmentManager.findFragmentByTag(RetainedFragment.REATAINED_FRAGMENT_TAG);
        if(mRetainedFragment == null){
            mRetainedFragment = new RetainedFragment();
            fragmentManager.beginTransaction().add(mRetainedFragment, RetainedFragment.REATAINED_FRAGMENT_TAG).commit();
        }

        initialiseUI();

        // handle config change by getting the instance of the presenter
        mImagePresenter = (ImagePresenter) mRetainedFragment.getObject(ImagePresenter.PRESENTER_KEY);
        mViewContext = (ViewContext) mRetainedFragment.getObject(ViewContext.VIEW_CONTEXT_KEY);
        //if null first time in
        if (mImagePresenter == null) mImagePresenter = new ImagePresenter(this);
        if (mViewContext == null) mViewContext = new ViewContext(this);

        mImagePresenter.onCreate();

    }

    private void initialiseUI() {

        urlEditText = (EditText) findViewById(R.id.urlEditText);
        addButton = (Button) findViewById(R.id.addButton);
        addButton.setOnClickListener(this);
        goButton = (Button) findViewById(R.id.goButton);
        goButton.setOnClickListener(this);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),
                2,GridLayoutManager.HORIZONTAL, false));
        recyclerBitmaps = new ArrayList<>();
        imageRecyclerAdapter = new ImageRecyclerAdapter(recyclerBitmaps);
        recyclerView.setAdapter(imageRecyclerAdapter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public void showToast(String toastMessage) {
        Toast.makeText(getApplicationContext(), toastMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void displayImage(Bitmap bitmap) {

    }

    @Override
    public void updateRecyclerView() {

        recyclerBitmaps = mImagePresenter.recyclerBitmaps();
        imageRecyclerAdapter.notifyDataSetChanged();

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.goButton:
                mImagePresenter.handleDownloads(mViewContext);

            case R.id.addButton :
                String url = urlEditText.getText().toString();

                if (!url.equals("")) {
                    mImagePresenter.handleButtonClick(url);
                }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // handle config changes by placing presenter into retained fragment
        mRetainedFragment.putObject(ImagePresenter.PRESENTER_KEY, mImagePresenter);
        mRetainedFragment.putObject(ViewContext.VIEW_CONTEXT_KEY, mViewContext);
    }
}
