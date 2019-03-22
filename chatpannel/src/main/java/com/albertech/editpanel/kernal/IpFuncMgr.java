package com.albertech.editpanel.kernal;

import com.albertech.editpanel.base.IFunc;

import java.util.Map;


/**
 * 输入面板管理接口, 用于输入面板根布局对管理器的操作, 包内可见
 *
 * @author albert
 * 20181225
 */
interface IpFuncMgr {

    /**
     * 向管理器注册扩展功能
     *
     * @param func
     */
    void registerFunc(IFunc func);

    /**
     * 所有扩展功能注册完成, 此方法可以将Fragment预加载
     */
    void onFuncRegisterComplete();

    /**
     * 获取扩展功能注册表, 要返回一个注册表副本, 以免注册表被其他生命周期更长的实例长期持有导致内存泄漏
     *
     * @return 扩展功能注册表副本
     */
    Map<Integer, IFunc> getRegisteredFuncs();

    /**
     * 主动切换输入状态
     */
    void setFuncStatus(int status);

    /**
     * 收起输入面板, 即状态切换为隐藏
     */
    void hide();
}
