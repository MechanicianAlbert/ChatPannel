package com.albertech.easypannel.func.emoji.api.impl;

import com.albertech.easypannel.func.emoji.api.IEmojiConfig;
import com.albertech.easypannel.func.emoji.api.OnEmojiClickListener;

public class DefaultEmojiConfig implements IEmojiConfig, DefaultEmojiConstant {

    @Override
    public int getColumnCountEachPage() {
        return 7;
    }

    @Override
    public int[][] getEmojiRes() {
        return ALL_RES;
    }

    @Override
    public String[][] getEmojiCode() {
        return ALL_CODE;
    }

    @Override
    public String getEmojiPattern() {
        return EMOJI_PATTERN;
    }

}
