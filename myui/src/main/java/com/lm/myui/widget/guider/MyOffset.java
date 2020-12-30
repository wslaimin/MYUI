package com.lm.myui.widget.guider;

/**
 * delta in x axis or y axis
 * positive dx is offset to right,negative otherwise
 * positive dy is offset to bottom,negative otherwise
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
