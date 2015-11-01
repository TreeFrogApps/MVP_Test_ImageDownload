package com.treefrogapps.mvp_test_imagedownload.recyclerview;


import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.treefrogapps.mvp_test_imagedownload.R;

import java.util.ArrayList;

public class ImageRecyclerAdapter extends RecyclerView.Adapter<ImageRecyclerAdapter.MYViewHolder> {

    private Context mContext;
    private ArrayList<RecyclerBitmap> mRecyclerBitmaps;

    public ImageRecyclerAdapter(Context context, ArrayList<RecyclerBitmap> recyclerBitmaps){

        this.mContext = context;
        this.mRecyclerBitmaps = recyclerBitmaps;
    }

    @Override
    public MYViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_image_view, parent, false);
        return new MYViewHolder(itemView);
    }

    public class MYViewHolder extends RecyclerView.ViewHolder {

        private CardView recyclerCardView;
        private ImageView recyclerImageView;

        public MYViewHolder(View itemView) {
            super(itemView);

            recyclerCardView = (CardView) itemView.findViewById(R.id.recyclerCardView);
            recyclerImageView = (ImageView) itemView.findViewById(R.id.recyclerImageView);
        }
    }

    @Override
    public void onBindViewHolder(MYViewHolder holder, int position) {

        holder.recyclerImageView.setImageBitmap(mRecyclerBitmaps.get(position).getBitmap());
    }

    @Override
    public int getItemCount() {
        return mRecyclerBitmaps.size();
    }


}
