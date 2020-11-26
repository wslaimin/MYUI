package com.lm.myui_demo;

import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.lm.myui.widget.recycler.MyRecyclerViewDivider;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);
        recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        MyRecyclerViewDivider divider = new MyRecyclerViewDivider(Color.TRANSPARENT);
        divider.setHorizontalSize(16);
        divider.setVerticalSize(16);
        recyclerView.addItemDecoration(divider);
        MyExampleAdapter adapter=null;
        ArrayList<MyExampleItem> list=getIntent().getParcelableArrayListExtra("children");
        if(list!=null){
            adapter=new MyExampleAdapter(list);
        }else{
            try {
                MyExampleItem root=MyXmlParser.parse(this);
                adapter=new MyExampleAdapter(root.children);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        recyclerView.setAdapter(adapter);
    }
}