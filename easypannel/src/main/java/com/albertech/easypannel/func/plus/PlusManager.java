package com.albertech.easypannel.func.plus;


import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.albertech.easypannel.R;
import com.albertech.easypannel.func.plus.adapter.PlusGroupAdapter;
import com.albertech.easypannel.func.plus.api.IPlusConfig;
import com.albertech.easypannel.func.plus.api.OnPlusItemClickListener;
import com.albertech.easypannel.func.plus.bean.PlusBean;

import java.util.ArrayList;
import java.util.List;


class PlusManager {

    private static final List<List<PlusBean>> PLUS_LIST = new ArrayList<>();


    PlusManager(IPlusConfig config) {
        throw new RuntimeException("This class should not be instantiate");
    }


    static void setPlusConfig(IPlusConfig config) {
        checkConfigValidation(config);
        initPlusList(config);
    }

    static List<View> creatPlusPagers(Context context, OnPlusItemClickListener listener) {
        List<View> list = new ArrayList<>();
        for (int i = 0; i < PLUS_LIST.size(); i++) {
            list.add(createSinglePlusPage(context, i, listener));
        }
        return list;
    }


    private static void checkConfigValidation(IPlusConfig config) {
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

    private static void initPlusList(IPlusConfig config) {
        PLUS_LIST.clear();
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

    private static View createSinglePlusPage(Context context, int pageIndex, OnPlusItemClickListener listener) {
        View page = View.inflate(context, R.layout.item_plus_page, null);
        RecyclerView rv = page.findViewById(R.id.rv_page_plus);
        fillPageWithEmojiData(rv, pageIndex, listener);
        return page;
    }

    private static void fillPageWithEmojiData(final RecyclerView rv, int pageIndex, final OnPlusItemClickListener listener) {
        PlusGroupAdapter adapter = new PlusGroupAdapter() {
            @Override
            public boolean onItemClick(int position, PlusBean plusBean) {
                if (listener != null) {
                    listener.onPlusItemClick(plusBean.NAME);
                }
                return false;
            }
        };
        adapter.updateData(PLUS_LIST.get(pageIndex));
        rv.setLayoutManager(new GridLayoutManager(rv.getContext(), 4));
        rv.setAdapter(adapter);
    }

}
