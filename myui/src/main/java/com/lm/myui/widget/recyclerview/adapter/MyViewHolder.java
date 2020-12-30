package com.lm.myui.widget.recyclerview.adapter;

import android.view.View;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.HashMap;
import java.util.Map;

/**
 * powerful subclass of {@link RecyclerView.ViewHolder}
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
     * finds the first descendant view with the given ID
     *
     * @param id the ID to search for
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
