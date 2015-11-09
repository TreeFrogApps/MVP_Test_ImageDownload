package com.treefrogapps.mvp_test_imagedownload.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.treefrogapps.mvp_test_imagedownload.R;
import com.treefrogapps.mvp_test_imagedownload.recyclerview.ImageRecyclerAdapter;
import com.treefrogapps.mvp_test_imagedownload.utils.ImageUtils;


public class ImageViewActivity extends AppCompatActivity {

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);

        String imageLocation = getIntent().getStringExtra(ImageRecyclerAdapter.IMAGE_FILE_LOCATION);

        if (imageLocation != null) {

            imageView = (ImageView) findViewById(R.id.my_image_view);
            imageView.setImageBitmap(ImageUtils.openOriginalImage(imageLocation));
        }




    }

}
