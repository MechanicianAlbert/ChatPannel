package com.albertech.easypannel.func.plus.adapter;


import android.support.annotation.NonNull;
import android.view.View;

import com.albertech.common.base.recycler.normal.BaseRecyclerAdapter;
import com.albertech.common.base.recycler.normal.BaseHolder;
import com.albertech.easypannel.R;
import com.albertech.easypannel.func.plus.bean.PlusBean;


public class PlusHolder extends BaseHolder<BaseRecyclerAdapter<PlusHolder, PlusBean>, PlusBean> {


    public PlusHolder(BaseRecyclerAdapter<PlusHolder, PlusBean> adapter, @NonNull View itemView) {
        super(adapter, itemView);
    }


    @Override
    protected void onBind(int position, PlusBean plusBean) {
        setImage(R.id.iv_plus, plusBean.RES);
        setText(R.id.tv_plus, plusBean.NAME);
    }

}
