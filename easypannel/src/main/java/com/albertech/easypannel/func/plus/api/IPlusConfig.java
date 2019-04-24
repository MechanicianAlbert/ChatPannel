package com.albertech.easypannel.func.plus.api;


/**
 * 加号配置接口, 用户自定义加号内容
 *
 * 加号一行显示4个项目, 不可自定义配置单行数量
 */
public interface IPlusConfig {

    /**
     * @return 加号项目点击回调
     */
    OnPlusItemClickListener getOnPlusItemClickListener();

    /**
     * @return 加号项目图标的二维数组 (int[页数][项目序号])
     */
    int[][] getPlusRes();

    /**
     * @return 加号项目名称的二维数组 (String[页数][项目序号])
     */
    String[][] getPlusName();

}
