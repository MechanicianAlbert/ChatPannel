package com.albertech.easypannel.func.emoji;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.view.View;

import com.albertech.easypannel.R;
import com.albertech.easypannel.func.emoji.adapter.EmojiGroupAdapter;
import com.albertech.easypannel.func.emoji.api.IEmojiConfig;
import com.albertech.easypannel.func.emoji.api.OnEmojiClickListener;
import com.albertech.easypannel.func.emoji.bean.EmojiBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


class EmojiManager {

    private static final Map<String, Integer> EMOJI_CODE_RES_MAP = new HashMap<>();
    private static final List<List<EmojiBean>> EMOJI_LIST = new ArrayList<>();

    private static int mColumnCountEachPage;
    private static String mEmojiPattern;


    private EmojiManager() {
        throw new RuntimeException("This class should not be instantiate");
    }


    static void setEmojiConfig(IEmojiConfig config) {
        checkConfigValidation(config);
        initEmojiList(config);

        mColumnCountEachPage = config.getColumnCountEachPage();
        mEmojiPattern = config.getEmojiPattern();
    }

    static List<View> creatEmojiPagers(Context context, OnEmojiClickListener listener) {
        List<View> list = new ArrayList<>();
        for (int i = 0; i < EMOJI_LIST.size(); i++) {
            list.add(createSingleEmojiPage(context, i, listener));
        }
        return list;
    }


    private static void checkConfigValidation(IEmojiConfig config) {
        if (config == null) {
            throw new NullPointerException("Config should not be null");
        }
        int [][] emojiRes = config.getEmojiRes();
        if (emojiRes == null) {
            throw new NullPointerException("Emoji RES should not be null");
        }
        String[][] emojiCode = config.getEmojiCode();
        if (emojiCode == null) {
            throw new NullPointerException("Emoji code should not be null");
        }
        if (emojiRes.length != emojiCode.length) {
            throw new IllegalArgumentException("The page count of code and RES should be equal");
        }
        for (int i = 0; i < emojiRes.length; i++) {
            if (emojiRes[i].length != emojiCode[i].length) {
                throw new IllegalArgumentException("The emoji count in one page of code and RES should be equal");
            }
        }
        if (config.getEmojiPattern() == null) {
            throw new NullPointerException("Pattern should not be null");
        }
        if (config.getColumnCountEachPage() <= 0) {
            throw new IllegalArgumentException("Column count should be greater than zero");
        }
    }

    private static void initEmojiList(IEmojiConfig config) {
        EMOJI_CODE_RES_MAP.clear();
        EMOJI_LIST.clear();
        int[][] res = config.getEmojiRes();
        String[][] code = config.getEmojiCode();
        for (int i = 0; i < res.length; i++) {
            List<EmojiBean> l = new ArrayList<>();
            for (int j = 0; j < res[i].length; j++) {
                int r = res[i][j];
                String c = code[i][j];
                EMOJI_CODE_RES_MAP.put(c, r);
                l.add(new EmojiBean(r, c));
            }
            l.add(new EmojiBean(R.drawable.selector_ip_btn_back, "del"));
            EMOJI_LIST.add(l);
        }
    }


    private static View createSingleEmojiPage(Context context, int pageIndex, OnEmojiClickListener listener) {
        View page = View.inflate(context, R.layout.item_emoji_page, null);
        RecyclerView rv = page.findViewById(R.id.rv_page_emoji);
        fillPageWithEmojiData(rv, pageIndex, listener);
        return page;
    }

    private static void fillPageWithEmojiData(final RecyclerView rv, int pageIndex, final OnEmojiClickListener listener) {
        EmojiGroupAdapter adapter = new EmojiGroupAdapter() {
            @Override
            public boolean onItemClick(int position, EmojiBean emojiBean) {
                if (listener != null) {
                    if (position == getItemCount() - 1) {
                        listener.onBackspaceClick();
                    } else {
                        int res = emojiBean.RES;
                        String code = emojiBean.CODE;
                        SpannableString emoji = decorateTextByEmoji(rv.getContext(), code);
                        listener.onEmojiClick(emoji, res, code);
                    }
                }
                return false;
            }
        };
        adapter.updateData(EMOJI_LIST.get(pageIndex));
        rv.setLayoutManager(new GridLayoutManager(rv.getContext(), mColumnCountEachPage));
        rv.setAdapter(adapter);
    }

    private static SpannableString decorateTextByEmoji(Context context, String text) {
        SpannableString ss = new SpannableString(text);
        Pattern pattern = Pattern.compile(mEmojiPattern);
        Matcher matcher = pattern.matcher(ss);
        while (matcher.find()){
            String code = matcher.group();
            int drawableRes = getDrawableResByCode(code);
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

    private static int getDrawableResByCode(String code) {
        return EMOJI_CODE_RES_MAP.get(code);
    }

}
