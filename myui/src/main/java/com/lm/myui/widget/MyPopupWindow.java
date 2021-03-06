package com.lm.myui.widget;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

/**
 * 自动调整大小，保证始终显示在anchor下
 *
 * @author TFly
 * @github https://github.com/wslaimin/MYUI.git
 */
public class MyPopupWindow extends PopupWindow {
    private View restView;

    public MyPopupWindow(View contentView) {
        // height will be set when show
        super(contentView, ViewGroup.LayoutParams.MATCH_PARENT, 0);
    }

    @Override
    public void setContentView(View contentView) {
        LinearLayout ll = new LinearLayout(contentView.getContext());
        ll.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        if (contentView.getLayoutParams() == null) {
            contentView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        } else {
            contentView.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
        }
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.addView(contentView);

        restView = new View(contentView.getContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
        params.weight = 1;
        restView.setLayoutParams(params);
        if(isOutsideTouchable()){
            restView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        }else{
            restView.setOnClickListener(null);
        }
        ll.addView(restView);
        super.setContentView(ll);
    }

    @Override
    public void setOutsideTouchable(boolean touchable) {
        super.setOutsideTouchable(touchable);
        if(isOutsideTouchable()){
            restView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        }else{
            restView.setOnClickListener(null);
        }
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff, int gravity) {
        Rect r = new Rect();
        anchor.getWindowVisibleDisplayFrame(r);
        int displayHeight=r.bottom;
        anchor.getGlobalVisibleRect(r);
        setHeight(displayHeight - r.bottom - yoff);
        super.showAsDropDown(anchor, xoff, yoff, gravity);
    }
}
