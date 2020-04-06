package com.lm.myui.widget.recycler;

import android.view.View;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 减少模板代码，避免重复创建RecyclerView.ViewHolder子类
 *
 * @author TFly
 * @github https://github.com/wslaimin/MYUI.git
 */
public class MyBaseVH extends RecyclerView.ViewHolder {
    MyBaseVH(@NonNull View itemView) {
        super(itemView);
    }

    /**
     * 获取view
     * @param id view id
     * @return view
     */
    public <T extends View> T findViewById(int id){
        return itemView.findViewById(id);
    }
}
