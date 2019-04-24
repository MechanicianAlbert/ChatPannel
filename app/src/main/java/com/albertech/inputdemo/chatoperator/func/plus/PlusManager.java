package com.albertech.inputdemo.chatoperator.func.plus;


import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.albertech.inputdemo.R;
import com.albertech.inputdemo.chatoperator.func.plus.adapter.PlusGroupAdapter;
import com.albertech.inputdemo.chatoperator.func.plus.api.IPlusConfig;
import com.albertech.inputdemo.chatoperator.func.plus.api.OnPlusItemClickListener;
import com.albertech.inputdemo.chatoperator.func.plus.bean.PlusBean;

import java.util.ArrayList;
import java.util.List;


class PlusManager {

    private final List<List<PlusBean>> PLUS_LIST = new ArrayList<>();

    private OnPlusItemClickListener mListener;


    PlusManager(IPlusConfig config) {
        checkConfigValidation(config);
        initPlusList(config);

        mListener = config.getOnPlusItemClickListener();
    }


    private void checkConfigValidation(IPlusConfig config) {
        if (config == null) {
            throw new NullPointerException("Config should not be null");
        }
        int [][] plusRes = config.getPlusRes();
        if (plusRes == null) {
            throw new NullPointerException("Emoji RES should not be null");
        }
        String[][] plusName = config.getPlusName();
        if (plusName == null) {
            throw new NullPointerException("Emoji code should not be null");
        }
        if (plusRes.length != plusName.length) {
            throw new IllegalArgumentException("The page count of code and RES should be equal");
        }
        for (int i = 0; i < plusRes.length; i++) {
            if (plusRes[i].length != plusName[i].length) {
                throw new IllegalArgumentException("The emoji count in one page of code and RES should be equal");
            }
        }
    }

    private void initPlusList(IPlusConfig config) {
        int[][] res = config.getPlusRes();
        String[][] code = config.getPlusName();
        for (int i = 0; i < res.length; i++) {
            List<PlusBean> l = new ArrayList<>();
            for (int j = 0; j < res[i].length; j++) {
                int r = res[i][j];
                String c = code[i][j];
                l.add(new PlusBean(r, c));
            }
            PLUS_LIST.add(l);
        }
    }

    List<View> creatPlusPagers(Context context) {
        List<View> list = new ArrayList<>();
        for (int i = 0; i < PLUS_LIST.size(); i++) {
            list.add(createSinglePlusPage(context, i));
        }
        return list;
    }


    private View createSinglePlusPage(Context context, int pageIndex) {
        View page = View.inflate(context, R.layout.item_plus_page, null);
        RecyclerView rv = page.findViewById(R.id.rv_page_plus);
        fillPageWithEmojiData(rv, pageIndex);
        return page;
    }

    private void fillPageWithEmojiData(final RecyclerView rv, int pageIndex) {
        PlusGroupAdapter adapter = new PlusGroupAdapter() {
            @Override
            public boolean onItemClick(int position, PlusBean plusBean) {
                if (mListener != null) {
                    mListener.onPlusItemClick(plusBean.NAME);
                }
                return false;
            }
        };
        adapter.updateData(PLUS_LIST.get(pageIndex));
        rv.setLayoutManager(new GridLayoutManager(rv.getContext(), 4));
        rv.setAdapter(adapter);
    }

}
