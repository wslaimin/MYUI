package com.lm.myui_demo;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.airbnb.lottie.LottieAnimationView;
import com.lm.myui.widget.recyclerview.adapter.MyRecyclerViewDivider;
import com.lm.myui.widget.recyclerview.hf.MyBaseHFLayout;
import com.lm.myui.widget.recyclerview.hf.MyHFRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MyHFActivity extends AppCompatActivity {
    private MyHFRecyclerView hfLayout;
    private List<String> list;
    private MyAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_recycler_load);
        hfLayout=findViewById(R.id.hf);

        LayoutInflater inflater=LayoutInflater.from(this);
        final LottieAnimationView header=(LottieAnimationView) inflater.inflate(R.layout.header,hfLayout,false);
        hfLayout.setHeader(header);
        final LottieAnimationView footer=(LottieAnimationView) inflater.inflate(R.layout.footer,hfLayout,false);
        hfLayout.setFooter(footer);

        hfLayout.setMaxHeaderScrollExtent(500);

        hfLayout.setHFListener(new MyBaseHFLayout.MyHFListener() {
            @Override
            public void onRefresh() {
                if(!header.isAnimating()){
                    header.playAnimation();
                }
                hfLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        header.cancelAnimation();
                        hfLayout.completeRefresh();
                    }
                },2000);
            }

            @Override
            public void onLoadMore() {
                if(!footer.isAnimating()){
                    footer.playAnimation();
                }
                hfLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        for(int i=0;i<6;i++){
                            list.add("test"+(i+6));
                            adapter.notifyDataSetChanged();
                        }
                        footer.cancelAnimation();
                        hfLayout.completeLoadMore();
                    }
                },2000);
            }
        });

        hfLayout.setPullListener(new MyBaseHFLayout.MyPullListener() {
            @Override
            public void pullHeader() {
                float progress=hfLayout.getHeaderVisibleHeight()*1f/hfLayout.getMaxHeaderScrollExtent();
                header.setProgress(progress);
            }

            @Override
            public void pullFooter() {

            }
        });
        final RecyclerView recyclerView=hfLayout.getNestedView();
        list=new ArrayList<>();
        for(int i=0;i<60;i++){
            list.add("test "+i);
        }
        adapter=new MyAdapter(list);
        recyclerView.setAdapter(adapter);
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.hf,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.call_refresh:
                hfLayout.callRefresh();
                return true;
            case R.id.enable_refresh:
                hfLayout.enableHeader(!hfLayout.isHeaderEnable());
                item.setTitle(hfLayout.isHeaderEnable() ? "disable refresh" : "enable refresh");
                return true;
            case R.id.enable_more:
                hfLayout.enableFooter(!hfLayout.isFooterEnable());
                item.setTitle(hfLayout.isFooterEnable() ? "disable more" : "enable more");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
