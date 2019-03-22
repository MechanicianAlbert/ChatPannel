package com.albertech.inputdemo.chatoperator.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import com.albertech.inputdemo.R;


public class PagerDotIndicator extends LinearLayout implements View.OnClickListener, ViewPager.OnPageChangeListener {

    private LinearLayout.LayoutParams mLayoutParames;
    private ViewPager mVp;


    public PagerDotIndicator(Context context) {
        super(context);
        init();
    }

    public PagerDotIndicator(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PagerDotIndicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER);
        initLayoutParams();
    }

    private void initLayoutParams() {
        int size = getContext().getResources().getDimensionPixelSize(R.dimen.input_pannel_dot_indicator_size);
        int margin = getContext().getResources().getDimensionPixelSize(R.dimen.input_pannel_dot_indicator_margin);
        mLayoutParames = new LayoutParams(size, size);
        mLayoutParames.setMargins(margin, margin, margin, margin);
    }

    private void updateWithViewPager() {
        PagerAdapter adapter = mVp.getAdapter();
        if (adapter != null) {
            createDotsWithPageCount(adapter.getCount());
            selectDotByCurrentIndex(mVp.getCurrentItem());
        }
    }

    private void createDotsWithPageCount(int pageCount) {
        removeAllViews();
        for (int i = 0; i < pageCount; i++) {
            View dot = new View(getContext());
            dot.setLayoutParams(mLayoutParames);
            dot.setTag(i);
            dot.setBackgroundResource(R.drawable.selector_ip_indicator_dot);
            dot.setOnClickListener(this);
            addView(dot);
        }
    }

    private void selectDotByCurrentIndex(int index) {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            getChildAt(i).setSelected(i == index);
        }
    }


    public void setUpWithViewPager(ViewPager vp) {
        if (vp != null) {
            mVp = vp;
            mVp.addOnPageChangeListener(this);
            updateWithViewPager();
        }
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mVp.removeOnPageChangeListener(this);
        mVp = null;
    }

    @Override
    public void onClick(View view) {
        if (mVp != null) {
            Integer integer = (Integer) view.getTag();
            if (integer != null) {
                mVp.setCurrentItem(integer);
            }
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        selectDotByCurrentIndex(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
