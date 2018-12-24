package com.albertech.inputdemo.base;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;


public abstract class BaseSingleTypeAdapter<T, Q extends BaseSingleTypeAdapter.BaseHolder> extends BaseAdapter {

    protected final List<T> DATA = new ArrayList<>();


    public final void updateData(List<T> data) {
        DATA.clear();
        if (data != null) {
            DATA.addAll(data);
        }
        notifyDataSetChanged();
    }


    @Override
    public final int getCount() {
        return DATA.size();
    }

    @Override
    public final T getItem(int position) {
        return DATA.get(position);
    }

    @Override
    public final long getItemId(int position) {
        return position;
    }

    @Override
    public final View getView(int position, View convertView, ViewGroup parent) {
        Q holder;
        if (convertView == null) {
            holder = onCreateViewHolder(parent);
            convertView = holder.getConvertView();
        } else {
            holder = (Q) convertView.getTag();
        }
        onBindViewHolder(holder, position, getItem(position));
        return convertView;
    }

    protected abstract Q onCreateViewHolder(final ViewGroup parent);

    protected abstract void onBindViewHolder(final Q holder, final int position, final T item);


    public static abstract class BaseHolder {

        private final View mConvertView;
        protected final Context mContext;

        public BaseHolder(final View convertView) {
            if (convertView != null) {
                mConvertView = convertView;
                mContext = convertView.getContext();
                mConvertView.setTag(this);
            } else {
                throw new RuntimeException("Convert view is null!");
            }
        }

        public Context getContext() {
            return mContext;
        }

        public View getConvertView() {
            return mConvertView;
        }

        protected final <T extends View> T $(final int resId) {
            return mConvertView.findViewById(resId);
        }
    }
}
