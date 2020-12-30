package com.lm.myui.widget.round;

/**
 * Round interface
 *
 * @author TFly
 * @github https://github.com/wslaimin/MYUI.git
 */
public interface MyIRoundView {
    /**
     * set top left radius
     *
     * @param radius desired size in px
     */
    void setTopLeftRadius(float radius);

    float getTopLeftRadius();

    /**
     * set top right radius
     *
     * @param radius desired size in px
     */
    void setTopRightRadius(float radius);

    float getTopRightRadius();

    /**
     * set bottom right radius
     *
     * @param radius desired size in px
     */
    void setBottomRightRadius(float radius);

    float getBottomRightRadius();

    /**
     * set bottom left radius
     *
     * @param radius desired size in px
     */
    void setBottomLeftRadius(float radius);

    float getBottomLeftRadius();

    /**
     * set radius for all corners
     *
     * @param radius desired size in px
     */
    void setRadius(float radius);

    /**
     * stroke width
     *
     * @param width desired size in px
     */
    void setStrokeWidth(float width);

    float getStrokeWidth();

    /**
     * stroke color
     *
     * @param color a color value in the form 0xAARRGGBB
     */
    void setStrokeColor(int color);

    int getStrokeColor();
}
