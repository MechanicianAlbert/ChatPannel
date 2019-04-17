package com.albertech.editpanel.kernal;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.albertech.editpanel.base.IFunc;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;


/**
 * 输入面板自定义控件, 包外可见, 抽象
 *
 * @author albert
 * 20181225
 */
public abstract class AbsIpView extends FrameLayout implements IpFuncMgrImpl.OnFuncStatusActivateListener {

    private IpFuncMgr mIPFM;
    private View mRootView;


    public AbsIpView(@NonNull Context context) {
        super(context);
    }

    public AbsIpView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AbsIpView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        init();
    }

    private void init() {
        initView();
        initManager();
        onInitComplete(mRootView);
    }

    private void initView() {
        mRootView = View.inflate(getContext(), layoutRes(), null);
        addView(mRootView);
    }

    private void initManager() {
        IpMgrBuilder builder = onCreateManager(new IpMgrBuilder());
        mIPFM = builder.build();
        Set<IFunc> funcs = new HashSet<>();
        onRegisterFunc(funcs);
        for (IFunc func : funcs) {
            mIPFM.registerFunc(func);
        }
        mIPFM.onFuncRegisterComplete();
    }


    protected void onInitComplete(View rootView) {

    }


    public final void hide() {
        if (mIPFM != null) {
            mIPFM.hide();
        }
    }

    public final void setFuncStatus(int status) {
        if (mIPFM != null) {
            mIPFM.setFuncStatus(status);
        }
    }

    public final Map<Integer, IFunc> getRegisteredFuncs() {
        if (mIPFM != null) {
            return mIPFM.getRegisteredFuncs();
        } else {
            return null;
        }
    }


    protected abstract int layoutRes();

    protected abstract IpMgrBuilder onCreateManager(IpMgrBuilder builder);

    protected abstract void onRegisterFunc(Set<IFunc> funcs);

}
