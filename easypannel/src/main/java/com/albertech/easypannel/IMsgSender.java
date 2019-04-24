package com.albertech.easypannel;

import android.text.Editable;

import com.albertech.easypannel.func.voice.IVoiceMsgContract;

public interface IMsgSender extends IVoiceMsgContract.IVoiceHandler {

    void onTextSubmit(Editable text);

    void onImageSubmit(String... paths);

}
