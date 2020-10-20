package com.lm.myui_demo;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.lm.myui.widget.recycler.MyBaseAdapter;
import com.lm.myui.widget.recycler.MyViewHolder;
import com.lm.myui.widget.recycler.MyRecyclerViewDivider;
import java.util.ArrayList;
import java.util.List;

public class MyRecyclerActivity extends AppCompatActivity {
    LinearLayoutManager layoutManager;
    Adapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_recycler);
        final RecyclerView recyclerView=findViewById(R.id.recycler);
        final List<String> list=new ArrayList<>();
        for(int i=0;i<60;i++){
            list.add("test"+i);
        }
        adapter=new Adapter(list);
        recyclerView.setAdapter(adapter);
        //layoutManager=new GridLayoutManager(this,3);
        /*layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if(position==3){
                    return 2;
                }
                return 1;
            }
        });*/
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        MyRecyclerViewDivider divider=new MyRecyclerViewDivider(Color.BLUE);
        divider.setHorizontalSize(30);
        divider.setVerticalSize(20);
        recyclerView.addItemDecoration(divider);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.orientation,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.vertical:
                adapter.setOrientation(RecyclerView.VERTICAL);
                layoutManager.setOrientation(RecyclerView.VERTICAL);
                adapter.notifyDataSetChanged();
                return true;
            case R.id.horizontal:
                adapter.setOrientation(RecyclerView.HORIZONTAL);
                layoutManager.setOrientation(RecyclerView.HORIZONTAL);
                adapter.notifyDataSetChanged();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    static class Adapter extends MyBaseAdapter<List<String>> {
        int orientation=RecyclerView.VERTICAL;

        Adapter(List<String> data) {
            super(data);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            TextView textView=holder.findViewById(R.id.text);
            textView.setText(data.get(position));
            if(orientation==RecyclerView.HORIZONTAL){
                textView.setLayoutParams(new ViewGroup.LayoutParams(120, ViewGroup.LayoutParams.MATCH_PARENT));
            }else{
                textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,120));
            }
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

        void setOrientation(int orientation){
            this.orientation=orientation;
        }
    }
}
