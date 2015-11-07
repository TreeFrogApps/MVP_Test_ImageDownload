package com.treefrogapps.mvp_test_imagedownload.view;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.treefrogapps.mvp_test_imagedownload.R;
import com.treefrogapps.mvp_test_imagedownload.recyclerview.ImageRecyclerAdapter;


public class ImageViewActivity extends AppCompatActivity {

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);

        Bitmap bitmap = null;

        String imageLocation = getIntent().getStringExtra(ImageRecyclerAdapter.IMAGE_FILE_LOCATION);

        if (imageLocation != null) {

            bitmap = BitmapFactory.decodeFile(imageLocation);
        }

        imageView = (ImageView) findViewById(R.id.my_image_view);
        imageView.setImageBitmap(bitmap);


    }

}
