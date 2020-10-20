package com.lm.myui.widget.recycler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

/**
 * 减少模板代码，子类只需重写onBindViewHolder(MyBaseVH,int)
 *
 * @author TFly
 * @github https://github.com/wslaimin/MYUI.git
 */
public abstract class MyBaseAdapter<T extends List> extends RecyclerView.Adapter<MyViewHolder> {
    protected LayoutInflater inflater;
    protected T data;
    protected Context context;

    public MyBaseAdapter(T data){
        this.data=data;
    }

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

    @Override
    public int getItemCount() {
        return data.size();
    }

    /**
     * 子类实现，获取layout id
     * @param viewType view类型
     * @return layout id
     */
    public abstract int getLayoutId(int viewType);
}
