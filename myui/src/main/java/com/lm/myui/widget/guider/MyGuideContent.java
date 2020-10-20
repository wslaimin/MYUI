package com.lm.myui.widget.guider;

import android.view.View;

public class MyGuideContent {
    public static final int LEFT_TO_LEFT = 0x00000000;
    public static final int LEFT_TO_RIGHT = 0x00000001;
    public static final int RIGHT_TO_LEFT = 0x00000002;
    public static final int RIGHT_TO_RIGHT = 0x00000003;
    public static final int HORIZONTAL_CENTER = 0x00000004;
    public static final int TOP_TO_TOP = LEFT_TO_LEFT << 4;
    public static final int TOP_TO_BOTTOM = LEFT_TO_RIGHT << 4;
    public static final int BOTTOM_TO_TOP = RIGHT_TO_LEFT << 4;
    public static final int BOTTOM_TO_BOTTOM = RIGHT_TO_RIGHT << 4;
    public static final int VERTICAL_CENTER = HORIZONTAL_CENTER << 4;
    private View content;
    private int position;
    private MyOffset offset = new MyOffset();

    public MyGuideContent(View content, int position) {
        this.content = content;
        this.position = position;
    }

    public View getContent() {
        return content;
    }

    public void setContent(View view) {
        content = view;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public MyOffset getOffset() {
        return offset;
    }

    public void setOffset(MyOffset offset) {
        this.offset = new MyOffset(offset);
    }

    int getHorizontalPosition() {
        return position & 0x0000000F;
    }

    int getVerticalPosition() {
        return position & 0x000000F0;
    }
}
