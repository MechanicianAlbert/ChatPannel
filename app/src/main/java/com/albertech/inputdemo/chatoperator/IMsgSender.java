package com.albertech.inputdemo.chatoperator;

import android.text.Editable;

import com.albertech.inputdemo.chatoperator.func.voice.IVoiceMsgContract;

public interface IMsgSender extends IVoiceMsgContract.IVoiceHandler {

    void onTextSubmit(Editable text);

    void onImageSubmit(String... paths);

}
