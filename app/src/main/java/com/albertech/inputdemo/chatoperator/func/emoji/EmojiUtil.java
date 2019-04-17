package com.albertech.inputdemo.chatoperator.func.emoji;


import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.albertech.inputdemo.R;

import java.util.ArrayList;
import java.util.List;


public class EmojiUtil implements Emojis {

    public static int getDrawableResByCode(String code) {
        for (int page = 0; page < ALL_CODE.length; page++) {
            for (int index = 0; index < ALL_CODE[page].length; index++) {
                if (TextUtils.equals(ALL_CODE[page][index], code)) {
                    return ALL_RES[page][index];
                }
            }
        }
        return 0;
    }

    public static String getEmojiPattern() {
        StringBuffer sb = new StringBuffer("");
        for (int page = 0; page < ALL_CODE.length; page++) {
            for (int index = 0; index < ALL_CODE[page].length; index++) {
                sb.append(ALL_CODE[page][index]);
            }
        }
        String pattern = sb.toString()
                .replace("[", "(\\[")
                .replace("]", "\\])?");
        return pattern;
    }

    public static List<View> creatEmojiPagers(Context context, OnEmojiClickListener listener) {
        List<View> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            list.add(createSingleEmojiPage(context, i, listener));
        }
        return list;
    }


    private static View createSingleEmojiPage(Context context, int pageIndex, OnEmojiClickListener listener) {
        View page = View.inflate(context, R.layout.item_emoji_page, null);
        RecyclerView rv = page.findViewById(R.id.rv_page_emoji);
        fillPageWithEmojiData(rv, pageIndex, listener);
        return page;
    }

    private static void fillPageWithEmojiData(RecyclerView rv, int pageIndex, final OnEmojiClickListener listener) {
        EmojiGroupAdapter adapter = new EmojiGroupAdapter() {
            @Override
            public boolean onItemClick(int position, EmojiBean emojiBean) {
                if (listener != null) {
                    if (position == getItemCount() - 1) {
                        listener.onBackspaceClick();
                    } else {
                        listener.onEmojiClick(emojiBean.getRes(), emojiBean.getCode());
                    }
                }
                return false;
            }
        };
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
