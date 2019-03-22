package com.albertech.inputdemo.chatoperator;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;




import com.albertech.editpanel.base.IFunc;
import com.albertech.editpanel.kernal.AbsIpView;
import com.albertech.editpanel.kernal.IpMgrBuilder;
import com.albertech.inputdemo.R;
import com.albertech.inputdemo.chatoperator.func.IFuncStatus;
import com.albertech.inputdemo.chatoperator.func.emoji.EmojiFunc;
import com.albertech.inputdemo.chatoperator.func.plus.PlusFunc;
import com.albertech.inputdemo.chatoperator.func.voice.VoiceIFunc;

import java.util.Set;

public class InputPannelView extends AbsIpView implements IFuncStatus {

    private final TextWatcher TEXT_WATCHER = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            togglePlusOrSend(TextUtils.isEmpty(charSequence));
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };


    private EditText mEt;
    private View mBtnTalk;
    private View mBtnPlus;
    private View mBtnSend;


    public InputPannelView(@NonNull Context context) {
        super(context);
    }

    public InputPannelView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public InputPannelView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected int layoutRes() {
        return R.layout.layout_input_pannel;
    }

    @Override
    protected IpMgrBuilder onCreateManager(IpMgrBuilder builder) {
        return builder
                .fragmentManager(((AppCompatActivity) getContext()).getSupportFragmentManager())
                .rootView(this)
                .editId(R.id.et)
                .containerId(R.id.container)
                .setListener(this)
                .setDefaultContainerHeight(500)
                .setContainerHeightCoordinate(true)
                .setMaxContainerHeight(800)
                .setContainerReplaceMode(false);
    }

    @Override
    protected void onRegisterFunc(Set<IFunc> funcs) {
        funcs.add(new EmojiFunc());
        funcs.add(new PlusFunc());
        funcs.add(new VoiceIFunc());
    }

    @Override
    protected void onInitComplete(View rootView) {
        initView(rootView);
        initListener();
    }

    @Override
    public void onFuncActivate(int status) {
        Log.e("InputPannel", IFuncStatus.Helper.name(status));
        if (status == IFuncStatus.FUNC_INPUT) {
            toggleEditOrVoice(true);
        } else if (status == IFuncStatus.FUNC_VOICE) {
            toggleEditOrVoice(false);
        } else if (status == IFuncStatus.FUNC_EMOJI) {
            toggleEditOrVoice(true);
        } else if (status == IFuncStatus.FUNC_PLUS) {
            toggleEditOrVoice(true);
        }
    }


    private void initView(View rootView) {
        mEt = rootView.findViewById(R.id.et);
        mBtnTalk = rootView.findViewById(R.id.btn_talk);
        mBtnPlus = rootView.findViewById(R.id.btn_plus);
        mBtnSend = rootView.findViewById(R.id.btn_send);
    }

    private void initListener() {
        mEt.addTextChangedListener(TEXT_WATCHER);
    }

    private void toggleEditOrVoice(boolean showEdit) {
        ViewGroup.LayoutParams editLp = mEt.getLayoutParams();
        editLp.width = showEdit ? ViewGroup.LayoutParams.MATCH_PARENT : 0;
        mEt.setLayoutParams(editLp);
        ViewGroup.LayoutParams voiceLp = mBtnTalk.getLayoutParams();
        voiceLp.width = showEdit ? 0 : ViewGroup.LayoutParams.MATCH_PARENT;
        mBtnTalk.setLayoutParams(voiceLp);
    }

    private void togglePlusOrSend(boolean showPlus) {
        mBtnPlus.setVisibility(showPlus ? VISIBLE : INVISIBLE);
        mBtnSend.setVisibility(showPlus ? INVISIBLE : VISIBLE);
    }

}
