package com.lm.myui_demo;

import android.widget.TextView;
import androidx.annotation.NonNull;
import com.lm.myui.widget.recycler.MyBaseAdapter;
import com.lm.myui.widget.recycler.MyViewHolder;
import java.util.List;

public class MyAdapter extends MyBaseAdapter<List<String>> {

    public MyAdapter(List<String> data) {
        super(data);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        TextView textView=holder.findViewById(R.id.text);
        textView.setText(data.get(position));
    }

    @Override
    public int getLayoutId(int viewType) {
        if(viewType==0){
            return R.layout.item;
        }
        return R.layout.item_big;
    }

    @Override
    public int getItemViewType(int position) {
        if(position==3){
            return 1;
        }
        return 0;
    }
}
