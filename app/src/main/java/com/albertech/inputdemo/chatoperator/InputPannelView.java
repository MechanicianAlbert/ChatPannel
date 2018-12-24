package com.albertech.inputdemo.chatoperator;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.GridView;

import com.albertech.editpanel.base.IFunc;
import com.albertech.editpanel.kernal.AbsIpView;
import com.albertech.editpanel.kernal.IpMgrBuilder;
import com.albertech.inputdemo.R;
import com.albertech.inputdemo.chatoperator.func.IFuncStatus;
import com.albertech.inputdemo.chatoperator.func.album.AlbumFunc;
import com.albertech.inputdemo.chatoperator.func.emoji.EmojiFunc;
import com.albertech.inputdemo.chatoperator.func.plus.PlusFunc;
import com.albertech.inputdemo.chatoperator.func.voice.VoiceIFunc;
import com.albertech.inputdemo.chatoperator.shortcut.ShortcutAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class InputPannelView extends AbsIpView implements IFuncStatus {

    private View mBtnTalk;
    private View mEt;

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
        funcs.add(new AlbumFunc());
    }

    @Override
    protected void onInitComplete(View rootView) {
        mBtnTalk = rootView.findViewById(R.id.btn_talk);
        mEt = rootView.findViewById(R.id.et);
        GridView gv = rootView.findViewById(R.id.gv);
        ShortcutAdapter adapter = new ShortcutAdapter(this);
        gv.setAdapter(adapter);
        gv.setOnItemClickListener(adapter);
        List<Integer> data = new ArrayList<>();
        data.add(R.drawable.ip_trigger_album);
        data.add(R.drawable.ip_trigger_album);
        data.add(R.drawable.ip_trigger_album);
        data.add(R.drawable.ip_trigger_album);
        data.add(R.drawable.ip_trigger_album);
        adapter.updateData(data);
    }

    @Override
    public void onFuncActivate(int status) {
        Log.e("InputPannel", IFuncStatus.Helper.name(status));
        if (status == IFuncStatus.FUNC_INPUT) {
            mEt.setVisibility(VISIBLE);
            mBtnTalk.setVisibility(GONE);
        } else if (status == IFuncStatus.FUNC_VOICE) {
            mEt.setVisibility(GONE);
            mBtnTalk.setVisibility(VISIBLE);
        }
    }

}
