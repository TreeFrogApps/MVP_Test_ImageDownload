package com.treefrogapps.mvp_test_imagedownload.view;

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
import com.treefrogapps.mvp_test_imagedownload.utils.ViewContext;

import java.util.ArrayList;


public class ImageActivity extends AppCompatActivity implements MVP.ViewInterface, View.OnClickListener {

    private Toolbar mToolbar;
    private EditText urlEditText;
    private Button goButton;
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

        if (mToolbar !=null){
            mToolbar.showOverflowMenu();
        }

        initialiseUI();

        //first time in - or config change
        if (mImagePresenter == null) mImagePresenter = new ImagePresenter(this);

    }

    private void initialiseUI() {

        urlEditText = (EditText) findViewById(R.id.urlEditText);
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

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.goButton :
                String url = urlEditText.getText().toString();

                if (!url.equals("")) {
                    mViewContext = new ViewContext(this);
                    mImagePresenter.handleButtonClick(mViewContext, url);
                }
        }
    }
}
