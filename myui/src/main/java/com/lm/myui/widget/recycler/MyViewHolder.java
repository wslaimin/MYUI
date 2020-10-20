package com.lm.myui.widget.recycler;

import android.view.View;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.HashMap;
import java.util.Map;

/**
 * 减少模板代码，避免重复创建RecyclerView.ViewHolder子类
 *
 * @author TFly
 * @github https://github.com/wslaimin/MYUI.git
 */
public class MyViewHolder extends RecyclerView.ViewHolder {
    private Map<Integer, View> views;

    MyViewHolder(@NonNull View itemView) {
        super(itemView);
        views = new HashMap<>();
    }

    /**
     * 获取view
     *
     * @param id view id
     * @return view
     */
    public <T extends View> T findViewById(int id) {
        View view;
        view = views.get(id);
        if (view == null) {
            view = itemView.findViewById(id);
            views.put(id, view);
        }
        return (T)view;
    }
}
