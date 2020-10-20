package com.lm.myui.widget.guider;

/**
 * 偏移量
 * dx正值，向右偏移
 * dy正值，向下偏移
 */
public class MyOffset {
    int dx, dy;

    public MyOffset() {

    }

    public MyOffset(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public MyOffset(MyOffset offset) {
        dx = offset.dx;
        dy = offset.dy;
    }

    public int getDx() {
        return dx;
    }

    public void setDx(int dx) {
        this.dx = dx;
    }

    public int getDy() {
        return dy;
    }

    public void setDy(int dy) {
        this.dy = dy;
    }
}
