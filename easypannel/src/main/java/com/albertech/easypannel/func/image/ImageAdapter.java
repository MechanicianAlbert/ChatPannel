package com.albertech.easypannel.func.image;

import android.view.View;

import com.albertech.common.base.recycler.select.SelectRecyclerAdapter;
import com.albertech.easypannel.R;


public class ImageAdapter extends SelectRecyclerAdapter<ImageHolder, String> {

    @Override
    public int getItemViewType(int position) {
        return R.layout.item_image;
    }

    @Override
    protected ImageHolder getHolderByViewType(View view, int i) {
        return new ImageHolder(this, view);
    }
}
