package com.lm.myui.widget.guider;

import android.view.View;
import java.lang.ref.WeakReference;
import java.util.List;

class AlphaObject {
    float alpha;
    WeakReference<List<View>> viewsRef;

    public AlphaObject(List<View> views) {
        viewsRef=new WeakReference<>(views);
    }

    public float getAlpha() {
        return alpha;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
        List<View> views=viewsRef.get();
        if(views!=null&&views.size()>0){
            for(int i=0;i<views.size();i++){
                views.get(i).setAlpha(alpha);
            }
        }
    }
}
