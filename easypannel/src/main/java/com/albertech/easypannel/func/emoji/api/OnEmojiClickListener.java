package com.albertech.easypannel.func.emoji.api;

import android.text.SpannableString;

import java.io.Serializable;

public interface OnEmojiClickListener extends Serializable {

    void onEmojiClick(SpannableString emoji, int res, String code);

    void onBackspaceClick();

}
