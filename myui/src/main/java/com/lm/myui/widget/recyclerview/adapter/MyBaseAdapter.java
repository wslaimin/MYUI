package com.lm.myui.widget.recyclerview.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * powerful subclass of {@link RecyclerView.Adapter<MyViewHolder>}
 *
 * @author TFly
 * @github https://github.com/wslaimin/MYUI.git
 */
public abstract class MyBaseAdapter extends RecyclerView.Adapter<MyViewHolder> {
    protected LayoutInflater inflater;
    protected Context context;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(context==null){
            context=parent.getContext();
            inflater=LayoutInflater.from(context);
        }
        return new MyViewHolder(inflater.inflate(getLayoutId(viewType),parent,false));
    }

    @Override
    public abstract void onBindViewHolder(@NonNull MyViewHolder holder, int position);

    /**
     * get layout id for specific view type
     * @param viewType view类型
     * @return layout id
     */
    public abstract int getLayoutId(int viewType);
}
