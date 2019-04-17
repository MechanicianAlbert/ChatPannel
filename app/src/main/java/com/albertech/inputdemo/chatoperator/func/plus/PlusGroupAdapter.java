package com.albertech.inputdemo.chatoperator.func.plus;

import android.view.View;

import com.albertech.inputdemo.R;
import com.albertech.inputdemo.base.BaseRecyclerAdapter;


public class PlusGroupAdapter extends BaseRecyclerAdapter<PlusHolder, PlusBean> {


    @Override
    public int getItemViewType(int position) {
        return R.layout.item_plus_icon;
    }

    @Override
    protected PlusHolder getHolderByViewType(View itemView, int viewType) {
        return new PlusHolder(this, itemView);
    }

}