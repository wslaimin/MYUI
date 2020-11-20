package com.lm.myui_demo;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import com.lm.myui.widget.recycler.MyBaseAdapter;
import com.lm.myui.widget.recycler.MyViewHolder;
import java.util.ArrayList;
import java.util.List;

//TODO: test AppBarLayout scroll flags,use CollapsingToolBarLayout
public class MaterialActivity extends AppCompatActivity {
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_material);
        recyclerView=findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<String> list=new ArrayList<>();
        for(int i=0;i<60;i++){
            list.add(""+i);
        }
        recyclerView.setAdapter(new RAdapter(list));
    }

    static class Adapter extends PagerAdapter{

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view==object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            RecyclerView recyclerView=new RecyclerView(container.getContext());
            recyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));
            List<String> list=new ArrayList<>();
            for(int i=0;i<60;i++){
                list.add(""+i);
            }
            recyclerView.setAdapter(new RAdapter(list));
            container.addView(recyclerView);
            return recyclerView;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((RecyclerView)object);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return "Title"+position;
        }
    }

    static class RAdapter extends MyBaseAdapter<List<String>>{
        public RAdapter(List<String> data) {
            super(data);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            TextView textView=holder.findViewById(R.id.text);
            textView.setText("text"+position);
        }

        @Override
        public int getLayoutId(int viewType) {
            return R.layout.item_big;
        }
    }
}
