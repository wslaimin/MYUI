package com.lm.myui_demo;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.lm.myui.widget.recyclerview.adapter.MyBaseAdapter;
import com.lm.myui.widget.recyclerview.adapter.MyViewHolder;
import java.util.ArrayList;
import java.util.List;

public class MyExampleAdapter extends MyBaseAdapter {
    private List<MyExampleItem> data;

    public MyExampleAdapter(List<MyExampleItem> data) {
        this.data = data;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final MyExampleItem exampleItem = data.get(position);
        ImageView imageView = holder.findViewById(R.id.image);
        imageView.setImageResource(exampleItem.drawable);
        TextView textView = holder.findViewById(R.id.text);
        textView.setText(exampleItem.text);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (exampleItem.children == null) {
                    try {
                        Intent intent = new Intent(context, Class.forName(exampleItem.activity));
                        context.startActivity(intent);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        final ArrayList<MyExampleItem> children = new ArrayList<>();
                        children.addAll(exampleItem.children);
                        Intent intent = new Intent(context, Class.forName(exampleItem.activity));
                        intent.putParcelableArrayListExtra("children", children);
                        context.startActivity(intent);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_example;
    }
}