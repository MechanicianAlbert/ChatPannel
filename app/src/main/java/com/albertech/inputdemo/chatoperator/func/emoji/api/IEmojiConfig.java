package com.albertech.inputdemo.chatoperator.func.emoji.api;


import java.io.Serializable;

/**
 * 表情配置接口, 用于扩展表情面板, 用户可实现此接口使用自定义表情替换默认表情
 */
public interface IEmojiConfig extends Serializable {

    /**
     * @return 单行展示表情数量
     */
    int getColumnCountEachPage();

    /**
     * @return 表情点击回调
     */
    OnEmojiClickListener getOnEmojiClickListener();

    /**
     * @return 表情图片资源二维数组 (int[表情页数][单页表情序号])
     */
    int[][] getEmojiRes();

    /**
     * @return 表情字符串代码的二维数组 (String[表情页数][单页表情序号])
     */
    String[][] getEmojiCode();

    /**
     * @return 表情字符串代码的正则表达式
     */
    String getEmojiPattern();
}
