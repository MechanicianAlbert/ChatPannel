package com.albertech.inputdemo.chatoperator.func.plus;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.albertech.inputdemo.R;

import java.util.ArrayList;
import java.util.List;

public class PlusUtil implements PlusDefine {

    public static List<View> createPlusPagers(Context context, OnPlusItemClickListener listener) {
        List<View> list = new ArrayList<>();
        list.add(createSinglePlusPage(context, 0, listener));
        return list;
    }


    private static View createSinglePlusPage(Context context, int pageIndex, OnPlusItemClickListener listener) {
        View page = View.inflate(context, R.layout.item_plus_page, null);
        RecyclerView rv = page.findViewById(R.id.rv_page_plus);
        fillPageWithPlusData(rv, pageIndex, listener);
        return page;
    }

    private static void fillPageWithPlusData(RecyclerView rv, int pageIndex, final OnPlusItemClickListener listener) {
        PlusGroupAdapter adapter = new PlusGroupAdapter() {
            @Override
            public boolean onItemClick(int position, PlusBean plusBean) {
                if (listener != null) {
                    listener.onPlusItemClick(plusBean.getName());
                }
                return false;
            }
        };
        adapter.updateData(createPlusListWithPageIndex(pageIndex));
        rv.setLayoutManager(new GridLayoutManager(rv.getContext(), 4));
        rv.setAdapter(adapter);
    }

    private static List<PlusBean> createPlusListWithPageIndex(int pageIndex) {
        switch (pageIndex) {
            case 0:
                return createPlusBeanList(PAGE_0_RES, PAGE_0_NAME);
            default:
                return new ArrayList<>();
        }
    }

    private static List<PlusBean> createPlusBeanList(int[] resArr, String[] nameArr) {
        List<PlusBean> list = new ArrayList<>();
        for (int i = 0; i < resArr.length; i++) {
            list.add(new PlusBean(resArr[i], nameArr[i]));
        }
        return list;
    }
}
