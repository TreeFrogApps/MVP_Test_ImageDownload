package com.treefrogapps.mvp_test_imagedownload.recyclerview;


import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.treefrogapps.mvp_test_imagedownload.R;
import com.treefrogapps.mvp_test_imagedownload.presenter.ImagePresenter;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class ImageRecyclerAdapter extends RecyclerView.Adapter<ImageRecyclerAdapter.MYViewHolder> {

    /**
     * Basic RecyclerView Adapter to hold all the RecyclerBitmaps
     *
     * Weak reference to the presenter is passed in to handle the intent to start Image View Activity
     */

    private Context mContext;
    private ArrayList<RecyclerBitmap> mRecyclerBitmaps;
    private WeakReference<ImagePresenter> mPresenter;
    public final static String IMAGE_FILE_LOCATION = "com.treefrogapps.mvp_test_imagedownload.recyclerview.image_location";

    public ImageRecyclerAdapter(Context context, ArrayList<RecyclerBitmap> recyclerBitmaps, ImagePresenter imagePresenter) {

        this.mContext = context;
        this.mRecyclerBitmaps = recyclerBitmaps;

        this.mPresenter = new WeakReference<>(imagePresenter);
    }

    @Override
    public MYViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_image_view, parent, false);
        return new MYViewHolder(itemView);
    }

    public class MYViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private CardView recyclerCardView;
        private ImageView recyclerImageView;

        public MYViewHolder(View itemView) {
            super(itemView);

            recyclerCardView = (CardView) itemView.findViewById(R.id.recyclerCardView);
            recyclerCardView.setOnClickListener(this);
            recyclerImageView = (ImageView) itemView.findViewById(R.id.recyclerImageView);
        }

        @Override
        public void onClick(View v) {

            switch (v.getId()) {

                case R.id.recyclerCardView:

                    mPresenter.get().handleRecyclerButtonClick(mContext,
                            mRecyclerBitmaps.get(getLayoutPosition()).getFileLocation());

                    break;
                default:
                    break;
            }
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
