package com.albertech.inputdemo.chatoperator.func.voice;


import android.media.MediaRecorder;

import com.albertech.inputdemo.chatoperator.func.voice.IVoiceMsgContract.RecordInterceptByPermissionException;


public class VoiceModelImpl implements IVoiceMsgContract.IVoiceModel {

    /**
     * 系统录音引擎实例
     */
    private MediaRecorder mMediaRecorder;


    public VoiceModelImpl() {

    }


    @Override
    public void init() {
        mMediaRecorder = new MediaRecorder();
    }

    @Override
    public void release() {
        if (mMediaRecorder != null) {
            mMediaRecorder.release();
            mMediaRecorder = null;
        }
    }

    @Override
    public synchronized void startRecord(String filePath) throws RecordInterceptByPermissionException {
        try {
            if (mMediaRecorder != null) {
                // 音源
                mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                // 格式
                mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
                // 编码
                mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                // 采样率
                mMediaRecorder.setAudioSamplingRate(SAMPLE_RATE_IN_HZ);
                // 输出路径
                mMediaRecorder.setOutputFile(filePath);
                // 准备
                mMediaRecorder.prepare();
                // 启动
                mMediaRecorder.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RecordInterceptByPermissionException();
        }
    }

    @Override
    public synchronized void stopRecord() {
        try {
            if (mMediaRecorder != null) {
                // 停止
                mMediaRecorder.stop();
                // 重置
                mMediaRecorder.reset();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getAmplitude() {
        try {
            if (mMediaRecorder != null) {
                // 获得录音振幅, 即音量)
                return mMediaRecorder.getMaxAmplitude();
            } else {
                return 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

}