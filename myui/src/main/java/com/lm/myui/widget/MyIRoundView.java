package com.lm.myui.widget;

/**
 * 圆角功能接口
 *
 * @author TFly
 * @github https://github.com/wslaimin/MYUI.git
 */
public interface MyIRoundView {
    /**
     * 设置左上角圆角半径
     *
     * @param radius 圆角半径
     */
    void setTopLeftRadius(float radius);

    float getTopLeftRadius();

    /**
     * 设置右上角圆角半径
     *
     * @param radius 圆角半径
     */
    void setTopRightRadius(float radius);

    float getTopRightRadius();

    /**
     * 设置右下角圆角半径
     *
     * @param radius 圆角半径
     */
    void setBottomRightRadius(float radius);

    float getBottomRightRadius();

    /**
     * 设置左下角圆角半径
     *
     * @param radius 圆角半径
     */
    void setBottomLeftRadius(float radius);

    float getBottomLeftRadius();

    /**
     * 设置角圆角半径
     *
     * @param radius 圆角半径
     */
    void setRadius(float radius);

    /**
     * 设置描边宽度
     *
     * @param width 宽度
     */
    void setStrokeWidth(float width);

    float getStrokeWidth();

    /**
     * 设置描边颜色
     *
     * @param color 颜色
     */
    void setStrokeColor(int color);

    int getStrokeColor();
}
