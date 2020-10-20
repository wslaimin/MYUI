package com.lm.myui_demo;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.lm.myui.widget.recycler.MyBaseAdapter;
import com.lm.myui.widget.recycler.MyViewHolder;
import com.lm.myui.widget.recycler.MyRecyclerViewDivider;
import com.lm.myui.widget.recycler.hf.MyHFLayout;
import java.util.ArrayList;
import java.util.List;

public class MyHFActivity extends AppCompatActivity {
    private MyHFLayout hfLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_recycler_load);
        final RecyclerView recyclerView=findViewById(R.id.recycler);
        final List<String> list=new ArrayList<>();
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
        MyRecyclerViewDivider divider=new MyRecyclerViewDivider(Color.BLUE);
        divider.setHorizontalSize(20);
        divider.setVerticalSize(20);
        recyclerView.addItemDecoration(divider);

        hfLayout=findViewById(R.id.hf);
        View header= LayoutInflater.from(this).inflate(R.layout.item,recyclerView,false);
        header.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,120));
        header.setBackgroundColor(Color.YELLOW);
        hfLayout.setHeader(header);
        View footer=LayoutInflater.from(this).inflate(R.layout.item,recyclerView,false);
        footer.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,120));
        footer.setBackgroundColor(Color.YELLOW);
        hfLayout.setFooter(footer);

        hfLayout.setLoadListener(new MyHFLayout.HFListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(getApplication(),"refresh",Toast.LENGTH_LONG).show();
                recyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        hfLayout.completeRefresh();
                    }
                },2000);
            }

            @Override
            public void onLoadMore() {
                for(int i=0;i<10;i++){
                    list.add("test"+(list.size()+i));
                }
                recyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.getAdapter().notifyDataSetChanged();
                        hfLayout.completeLoadMore();
                    }
                },2000);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.refresh,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.refresh:
                hfLayout.callRefresh();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    static class Adapter extends MyBaseAdapter<List<String>> {

        Adapter(List<String> data) {
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
}
