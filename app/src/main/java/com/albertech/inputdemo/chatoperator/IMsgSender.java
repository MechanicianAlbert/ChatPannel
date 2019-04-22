package com.albertech.inputdemo.chatoperator;

import android.text.Editable;

public interface IMsgSender {

    void onTextSubmit(Editable text);

    void onVoiceSubmit(String path);

    void onImageSubmit(String... paths);

}
