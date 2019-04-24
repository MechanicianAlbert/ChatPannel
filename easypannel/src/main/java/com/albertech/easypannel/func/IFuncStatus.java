package com.albertech.easypannel.func;

import com.albertech.editpanel.base.IDefaultFuncStatus;



public interface IFuncStatus extends IDefaultFuncStatus {

    class Helper {
        public static String name(int status) {
            switch (status) {
                case FUNC_HIDE:
                    return "收起";
                case FUNC_INPUT:
                    return "文字";
                case FUNC_EMOJI:
                    return "表情";
                case FUNC_PLUS:
                    return "加号";
                case FUNC_VOICE:
                    return "短语音";
                case FUNC_STT:
                    return "语音识别";
                default:
                    return "未知功能";
            }
        }
    }


    int FUNC_EMOJI = 0;
    int FUNC_PLUS = 1;
    int FUNC_VOICE = 2;
    int FUNC_ALBUM = 3;
    int FUNC_CAMERA = 4;
    int FUNC_VIDEO = 5;
    int FUNC_STT = 6;

}
