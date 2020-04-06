package com.lm.myui_demo;

import android.os.Bundle;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.lm.myui.widget.recycler.MyBaseAdapter;
import com.lm.myui.widget.recycler.MyBaseVH;
import com.lm.myui.widget.recycler.MyRecyclerViewDivider;
import java.util.ArrayList;
import java.util.List;

public class MyRecyclerActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_recycler);
        RecyclerView recyclerView=findViewById(R.id.recycler);
        List<String> list=new ArrayList<>();
        for(int i=0;i<60;i++){
            list.add("test "+i);
        }
        recyclerView.setAdapter(new Adapter(list));
        GridLayoutManager layoutManager=new GridLayoutManager(this,3);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if(position==3){
                    return 2;
                }
                return 1;
            }
        });
        recyclerView.setLayoutManager(layoutManager);
        MyRecyclerViewDivider divider=new MyRecyclerViewDivider(0xff000000);
        divider.setHorizontalSize(20);
        divider.setVerticalSize(20);
        recyclerView.addItemDecoration(divider);
    }

    static class Adapter extends MyBaseAdapter<List<String>> {

        Adapter(List<String> data) {
            super(data);
        }

        @Override
        public void onBindViewHolder(@NonNull MyBaseVH holder, int position) {
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
}
