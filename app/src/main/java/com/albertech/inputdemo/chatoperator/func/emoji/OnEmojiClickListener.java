package com.albertech.inputdemo.chatoperator.func.emoji;

import java.io.Serializable;

public interface OnEmojiClickListener extends Serializable {

    void onEmojiClick(int res, String code);

    void onBackspaceClick();

}