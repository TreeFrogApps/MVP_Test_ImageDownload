package com.treefrogapps.mvp_test_imagedownload.recyclerview;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class ImageRecyclerView extends RecyclerView.Adapter<ImageRecyclerView.MYViewHolder> {

    private ArrayList<RecyclerBitmap> mRecyclerBitmaps;

    public ImageRecyclerView(ArrayList<RecyclerBitmap> recyclerBitmaps){

        this.mRecyclerBitmaps = recyclerBitmaps;
    }


    @Override
    public MYViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    public class MYViewHolder extends RecyclerView.ViewHolder {

        public MYViewHolder(View itemView) {
            super(itemView);
        }
    }

    @Override
    public void onBindViewHolder(MYViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mRecyclerBitmaps.size();
    }


}
