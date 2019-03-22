package com.albertech.inputdemo.chatoperator.func.emoji;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.albertech.inputdemo.R;

import java.util.ArrayList;
import java.util.List;


public class EmojiUtil implements Emojis {


    public static List<View> creatEmojiPagers(Context context) {
        List<View> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            list.add(createSingleEmojiPage(context, i));
        }
        return list;
    }


    private static View createSingleEmojiPage(Context context, int pageIndex) {
        View page = View.inflate(context, R.layout.item_emoji_page, null);
        RecyclerView rv = page.findViewById(R.id.rv_page_emoji);
        fillPageWithEmojiData(rv, pageIndex);
        return page;
    }

    private static void fillPageWithEmojiData(RecyclerView rv, int pageIndex) {
        EmojiSinglePageAdapter adapter = new EmojiSinglePageAdapter();
        adapter.updateData(createEmojiListWithPageIndex(pageIndex));
        rv.setLayoutManager(new GridLayoutManager(rv.getContext(), 7));
        rv.setAdapter(adapter);
    }

    private static List<EmojiBean> createEmojiListWithPageIndex(int pageIndex) {
        switch (pageIndex) {
            case 0:
                return createEmojiBeanList(PAGE_0_RES, PAGE_0_CODE);
            case 1:
                return createEmojiBeanList(PAGE_1_RES, PAGE_1_CODE);
            case 2:
                return createEmojiBeanList(PAGE_2_RES, PAGE_2_CODE);
            case 3:
                return createEmojiBeanList(PAGE_3_RES, PAGE_3_CODE);
            case 4:
                return createEmojiBeanList(PAGE_4_RES, PAGE_4_CODE);
            default:
                return new ArrayList<>();
        }
    }

    private static List<EmojiBean> createEmojiBeanList(int[] resArr, String[] codeArr) {
        List<EmojiBean> list = new ArrayList<>();
        for (int i = 0; i < resArr.length; i++) {
            list.add(new EmojiBean(resArr[i], codeArr[i]));
        }
        list.add(new EmojiBean(R.drawable.selector_ip_btn_back, "del"));
        return list;
    }

}
