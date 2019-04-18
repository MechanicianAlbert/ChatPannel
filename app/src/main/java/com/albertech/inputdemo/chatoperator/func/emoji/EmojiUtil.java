package com.albertech.inputdemo.chatoperator.func.emoji;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;

import com.albertech.inputdemo.R;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class EmojiUtil implements Emojis {

    private static final String EMOJI_PATTERN = buildEmojiPattern();


    public static SpannableString decorateTextByEmoji(Context context, String text) {
        SpannableString ss = new SpannableString(text);
        Pattern pattern = Pattern.compile(EMOJI_PATTERN);
        Matcher matcher = pattern.matcher(ss);
        while (matcher.find()){
            String code = matcher.group();
            int drawableRes = EmojiUtil.getDrawableResByCode(code);
            if (drawableRes != 0){
                Drawable drawable = context.getResources().getDrawable(drawableRes);
                int size = (int) (1.3f * context.getResources().getDimensionPixelSize(R.dimen.input_pannel_edit_text_size));
                drawable.setBounds(0,0, size, size);
                ImageSpan span = new ImageSpan(drawable);
                ss.setSpan(span,matcher.start(),matcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return ss;
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
        adapter.updateData(createEmojiBeanList(ALL_RES[pageIndex], ALL_CODE[pageIndex]));
        rv.setLayoutManager(new GridLayoutManager(rv.getContext(), 7));
        rv.setAdapter(adapter);
    }

    private static List<EmojiBean> createEmojiBeanList(int[] resArr, String[] codeArr) {
        List<EmojiBean> list = new ArrayList<>();
        for (int i = 0; i < resArr.length; i++) {
            list.add(new EmojiBean(resArr[i], codeArr[i]));
        }
        list.add(new EmojiBean(R.drawable.selector_ip_btn_back, "del"));
        return list;
    }


    private static int getDrawableResByCode(String code) {
        for (int page = 0; page < ALL_CODE.length; page++) {
            for (int index = 0; index < ALL_CODE[page].length; index++) {
                if (TextUtils.equals(ALL_CODE[page][index], code)) {
                    return ALL_RES[page][index];
                }
            }
        }
        return 0;
    }

    private static String buildEmojiPattern() {
        int page;
        int index = 0;
        StringBuffer sb = new StringBuffer("");
        for ( page = 0; page < ALL_CODE.length; page++) {
            for ( index = 0; index < ALL_CODE[page].length; index++) {
                sb.append(ALL_CODE[page][index]);
            }
        }
        return sb.toString()
                .replace("[", "|(\\[")
                .replace("]", "\\])")
                .replace("<s", "|(<s")
                .replace("n>", "n>)")
                .substring(1);
    }

}
