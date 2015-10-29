package com.treefrogapps.mvp_test_imagedownload.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.treefrogapps.mvp_test_imagedownload.MVP;
import com.treefrogapps.mvp_test_imagedownload.R;
import com.treefrogapps.mvp_test_imagedownload.presenter.ImagePresenter;


public class ImageActivity extends AppCompatActivity implements MVP.ViewInterface {

    private Toolbar mToolbar;
    private EditText urlEditText;
    private Button goButton;
    private ImageView imageView;

    private ImagePresenter mImagePresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        if (mToolbar !=null){
            mToolbar.showOverflowMenu();
        }

        urlEditText = (EditText) findViewById(R.id.urlEditText);
        goButton = (Button) findViewById(R.id.goButton);
        imageView = (ImageView) findViewById(R.id.imageView);


        //first time in - or config change
        if (mImagePresenter == null) mImagePresenter = new ImagePresenter(this);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }




    @Override
    public void onSuccess() {


    }
}
