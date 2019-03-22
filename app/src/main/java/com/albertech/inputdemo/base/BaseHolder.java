package com.albertech.inputdemo.base;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;


public abstract class BaseHolder<Bean> extends RecyclerView.ViewHolder implements View.OnClickListener, OnItemClickListener<Bean> {

    private final SparseArray<View> VIEWS = new SparseArray<>();
    private final View mRootView;
    private BaseRecyclerAdapter<Bean> mAdapter;


    public BaseHolder(@NonNull View itemView) {
        super(itemView);
        mRootView = itemView;
        mRootView.setOnClickListener(this);
        onCreate();
    }


    @Override
    public final void onClick(View v) {
        onItemClick(getAdapterPosition(), getItemData(getAdapterPosition()));
        getAdapter().onItemClick(getAdapterPosition(), getItemData(getAdapterPosition()));
    }


    private void onRealBind(int position, Bean bean) {
        onBind(position, bean);
    }


    void setAdapter(BaseRecyclerAdapter<Bean> adapter) {
        mAdapter = adapter;
    }


    protected final BaseRecyclerAdapter<Bean> getAdapter() {
        return mAdapter;
    }

    protected final Bean getItemData(int position) {
        return mAdapter.getItemData(position);
    }

    protected final void notifyAdapterDataSetChanged() {
        mAdapter.notifyDataSetChanged();
    }

    protected final View getRootView() {
        return mRootView;
    }

    protected final Context getContext() {
        return mRootView.getContext().getApplicationContext();
    }

    protected final <V extends View> V $(@IdRes int idRes) {
        View v = VIEWS.get(idRes);
        if (v != null) {
            return (V) v;
        } else {
            V view = mRootView.findViewById(idRes);
            VIEWS.put(idRes, view);
            return view;
        }
    }

    protected final void setSelected(@IdRes int id, boolean selected) {
        $(id).setSelected(selected);
    }

    protected final void setText(@IdRes int id, String string) {
        TextView tv = $(id);
        tv.setText(string);
    }

    protected void onCreate() {

    }

    protected void onBind(int position, Bean bean) {

    }

    @Override
    public void onItemClick(int position, Bean bean) {

    }

}
