package com.albertech.inputdemo.chatoperator.func.plus;

import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.albertech.inputdemo.R;
import com.albertech.inputdemo.base.BaseHolder;
import com.albertech.inputdemo.base.BaseRecyclerAdapter;


public class PlusSinglePageAdapter extends BaseRecyclerAdapter<PlusHolder, PlusBean> {

//    static class PlusHolder extends BaseHolder<PlusBean> {
//
//        public PlusHolder(@NonNull View itemView) {
//            super(itemView);
//        }
//
//        @Override
//        protected void onBind(int position, PlusBean plusBean) {
//            ImageView iv = $(R.id.iv_plus);
//            iv.setBackgroundResource(plusBean.getRes());
//            TextView tv = $(R.id.tv_plus);
//            tv.setText(plusBean.getName());
//        }
//    }


    @Override
    public int getItemViewType(int position) {
        return R.layout.item_plus_icon;
    }

    @Override
    protected PlusHolder getHolderByViewType(View itemView, int viewType) {
        return new PlusHolder(this, itemView);
    }

    @Override
    public boolean onItemClick(int position, PlusBean plusBean) {
        Log.e("AAA", plusBean.getName());
        return false;
    }
}
