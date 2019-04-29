package com.albertech.easypannel;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.albertech.easypannel.func.image.ImagePickActivity;
import com.albertech.easypannel.func.image.ImagePicker;
import com.albertech.easypannel.func.stt.OnSttResultListener;
import com.albertech.easypannel.func.stt.SttFunc;
import com.albertech.editpanel.base.IFunc;
import com.albertech.editpanel.kernal.AbsIpView;
import com.albertech.editpanel.kernal.IpMgrBuilder;
import com.albertech.easypannel.R;
import com.albertech.easypannel.func.IFuncStatus;
import com.albertech.easypannel.func.emoji.EmojiFunc;
import com.albertech.easypannel.func.emoji.api.OnEmojiClickListener;
import com.albertech.easypannel.func.emoji.api.impl.DefaultEmojiConfig;
import com.albertech.easypannel.func.plus.PlusFunc;
import com.albertech.easypannel.func.plus.api.OnPlusItemClickListener;
import com.albertech.easypannel.func.plus.api.impl.DefaultPlusConfig;
import com.albertech.easypannel.func.voice.IVoiceMsgContract;
import com.albertech.easypannel.func.voice.VoiceIFunc;
import com.albertech.easypannel.func.voice.VoicePresenterImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * 聊天输入面板 View 的实现类
 * 具备短语音, 表情, 加号等默认功能
 * 可继承此类进行扩展或替换默认实现
 */
public class InputPannelView extends AbsIpView implements IFuncStatus {


    private final IVoiceMsgContract.IVoicePresenter VOICE_PRESENTER = new VoicePresenterImpl(
            getContext(),
            getVoiceRecordFilePath()
    );

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

    private final OnEmojiClickListener EMOJI_WATCHER = new OnEmojiClickListener() {

        @Override
        public void onEmojiClick(SpannableString emoji, int res, String code) {
            Log.e("AAA", "Emoji info: Res=" + res + ", Code=" + code);
            updateEdit(emoji);
        }

        @Override
        public void onBackspaceClick() {
            Log.e("AAA", "Delete Emoji");
            mEt.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
            mEt.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DEL));
        }
    };

    private final OnPlusItemClickListener PLUS_WATCHER = new OnPlusItemClickListener() {
        @Override
        public void onPlusItemClick(String name) {
            Log.e("AAA", name);
            if ("相册".equals(name)) {
                ImagePickActivity.start(getContext());
            } else if ("语音输入".equals(name)) {
                setFuncStatus(FUNC_STT);
            }
        }
    };

    private final ImagePicker.OnImagePickListener IMAGE_PICK_LISTENER = new ImagePicker.OnImagePickListener() {
        @Override
        public void onImagePick(List<String> picked) {
            if (mMsgSender != null && picked.size() > 0) {
                mMsgSender.onImageSubmit(picked.toArray(new String[0]));
            }
        }
    };

    private final OnSttResultListener STT_LISTENER = new OnSttResultListener() {
        @Override
        public void onSttResult(CharSequence result) {
            updateEdit(result);
        }
    };

    private final OnClickListener TEXT_SUBMITTER = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mMsgSender != null) {
                mMsgSender.onTextSubmit(mEt.getText());
                mEt.setText("");
            }
        }
    };


    private IMsgSender mMsgSender;

    private EditText mEt;
    private View mBtnTalk;
    private View mBtnPlus;
    private View mBtnSend;

    private int mCurrentStatus;


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
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        VOICE_PRESENTER.releaseView();
        ImagePicker.removeListener();
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
        if (useDefaultEmoji()) {
            funcs.add(EmojiFunc.newInstance(new DefaultEmojiConfig(), EMOJI_WATCHER));
        }
        if (useDefaultPlus()) {
            funcs.add(PlusFunc.newInstance(new DefaultPlusConfig(), PLUS_WATCHER));
        }
        funcs.add(new VoiceIFunc());
        funcs.add(SttFunc.newInstance(STT_LISTENER));
    }

    @Override
    protected void onInitComplete(View rootView) {
        initView(rootView);
        initListener();
    }

    @Override
    public void onFuncActivate(int status) {
        Log.e("InputPannel", IFuncStatus.Helper.name(status));
        mCurrentStatus = status;
        if (status == IFuncStatus.FUNC_HIDE) {
            toggleEditOrVoice(true);
        } else if (status == IFuncStatus.FUNC_INPUT) {
            toggleEditOrVoice(true);
        } else if (status == IFuncStatus.FUNC_VOICE) {
            toggleEditOrVoice(false);
        } else if (status == IFuncStatus.FUNC_EMOJI) {
            toggleEditOrVoice(true);
        } else if (status == IFuncStatus.FUNC_PLUS) {
            toggleEditOrVoice(true);
        }
    }

    @Override
    public void hide() {
        // 由于 VOICE 状态没有功能面版, 与 HIDE 状态等高, 故拦截 hide() 方法, 使点击外部时, 保持 VOICE 状态
        if (mCurrentStatus != IFuncStatus.FUNC_VOICE) {
            super.hide();
        }
    }

    /**
     * 为消息发送接口设置实现
     *
     * @param msgSender 外部发送消息接口的实现, 用于接收输入面板提交的文字/图片/短语音消息并发送
     */
    public final void setMsgSender(IMsgSender msgSender) {
        mMsgSender = msgSender;
        VOICE_PRESENTER.setVoiceHandler(msgSender);
    }


    /**
     * 为短语音消息录音文件设置存储路径
     *
     * @return 录音文件存储位置的相对路径, 其父路径为系统 SDCard 路径
     */
    protected String getVoiceRecordFilePath() {
        return "AAA";
    }

    /**
     * @return 使用默认表情面板
     */
    protected boolean useDefaultEmoji() {
        return true;
    }

    /**
     * @return 使用默认加号面板
     */
    protected boolean useDefaultPlus() {
        return true;
    }


    private void initView(View rootView) {
        mEt = rootView.findViewById(R.id.et);
        mBtnTalk = rootView.findViewById(R.id.btn_talk);
        mBtnPlus = rootView.findViewById(R.id.btn_plus);
        mBtnSend = rootView.findViewById(R.id.btn_send);
    }

    private void initListener() {
        VOICE_PRESENTER.bindView(mBtnTalk);
        mEt.addTextChangedListener(TEXT_WATCHER);
        mBtnSend.setOnClickListener(TEXT_SUBMITTER);

        ImagePicker.setListener(IMAGE_PICK_LISTENER);
    }

    private void toggleEditOrVoice(boolean showEdit) {
        ViewGroup.LayoutParams editLp = mEt.getLayoutParams();
        editLp.width = showEdit ? ViewGroup.LayoutParams.MATCH_PARENT : 0;
        editLp.height = showEdit ? ViewGroup.LayoutParams.WRAP_CONTENT : 0;
        mEt.setLayoutParams(editLp);
        ViewGroup.LayoutParams voiceLp = mBtnTalk.getLayoutParams();
        voiceLp.width = showEdit ? 0 : ViewGroup.LayoutParams.MATCH_PARENT;
        mBtnTalk.setLayoutParams(voiceLp);
    }

    private void togglePlusOrSend(boolean showPlus) {
        mBtnPlus.setVisibility(showPlus ? VISIBLE : INVISIBLE);
        mBtnSend.setVisibility(showPlus ? INVISIBLE : VISIBLE);
    }

    private void updateEdit(CharSequence c) {
        if (mEt != null) {
            int start = mEt.getSelectionStart();
            int end = mEt.getSelectionEnd();
            int newSelection = start + c.length();
            Editable newText = mEt.getText().replace(start, end, c);
            mEt.setText(newText);
            mEt.setSelection(newSelection);
            Log.e("AAA", "Text update info:"
                    + "\nSelection start: " + start
                    + "\nSelection end: " + end
                    + "\nNew selection: " + newSelection
                    + "\nNew text: " + newText
            );
        } else {
            Log.e("AAA", "EditText is null, updateEdit() would do nothing");
        }
    }

}
