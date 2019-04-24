package com.albertech.easypannel.base;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class BasePagerAdapter<T extends View> extends PagerAdapter {

    private final List<T> PAGERS = new ArrayList<>();


    public final void updatePagers(List<T> pagers) {
        PAGERS.clear();
        PAGERS.addAll(pagers);
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return PAGERS.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView(PAGERS.get(position));
    }

    @NonNull
    @Override
    public T instantiateItem(@NonNull ViewGroup container, int position) {
        T v = PAGERS.get(position);
        container.addView(v);
        return v;
    }
}
