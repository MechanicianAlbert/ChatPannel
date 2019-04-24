package com.albertech.inputdemo.chatoperator.func.voice;


import android.Manifest;
import android.view.View;


/**
 * 短语音录制MVP模型接口
 */
public interface IVoiceMsgContract {

    /**
     * 非正常权限拦截导致录制失败的异常
     */
    class RecordInterceptByPermissionException extends Exception {

    }

    /**
     * 处理录音文件回调接口
     */
    interface IVoiceHandler {

        /**
         * 处理录音文件回调方法
         * @param path 录音文件路径
         */
        void onVoiceSubmit(String path);
    }

    /**
     * 最大录制时长
     */
    int MAX_DURATION = 60;


    interface IVoiceModel {

        /**
         * 默认采样率
         */
        int SAMPLE_RATE_IN_HZ = 8000;


        /**
         * 初始化
         */
        void init();

        /**
         * 释放资源
         */
        void release();

        /**
         * 开启录音
         *
         * @param filePath 录音输出文件路径
         * @throws RecordInterceptByPermissionException 因权限拦截导致录音失败时抛出的异常
         */
        void startRecord(String filePath) throws RecordInterceptByPermissionException;

        /**
         * 停止录音
         */
        void stopRecord();

        /**
         * 获得录音振幅
         */
        int getAmplitude();

    }


    interface IVoiceView {

        /**
         * 录音开始
         */
        void onRecordStart();

        /**
         * 录音结束
         */
        void onRecordStop();

        /**
         * 触点在正区域 (提示松开发送)
         */
        void onTouchPositiveArea();

        /**
         * 触点在负区域 (提示松开取消)
         */
        void onTouchNegativeArea();

        /**
         * 录制音量刷新
         * @param value 录制音量
         */
        void onVolume(int value);

        /**
         * 录制时长刷新
         * @param second 录制时长
         */
        void onTimeUpdate(int second);

    }

    interface IVoicePresenter extends BtnTouchTransformer.Listener {

        /**
         * 录音所需的权限
         */
        String[] PERMISSIONS = {
                // 写存储 (允许写即自动允许读)
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                // 录音
                Manifest.permission.RECORD_AUDIO,
                // 通话状态监听
//                Manifest.permission.READ_PHONE_STATE
        };

        /**
         * 绑定视图
         * @param btn 录音按钮
         */
        void bindView(View btn);

        /**
         * 释放资源
         */
        void releaseView();

        /**
         * 设置录音文件处理接口
         * @param voiceHandler 录音文件处理接口
         */
        void setVoiceHandler(IVoiceHandler voiceHandler);

    }

}
