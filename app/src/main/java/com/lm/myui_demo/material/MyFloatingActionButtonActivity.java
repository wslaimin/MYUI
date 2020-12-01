package com.lm.myui_demo.material;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.lm.myui.widget.recycler.MyRecyclerViewDivider;
import com.lm.myui_demo.MyAdapter;
import com.lm.myui_demo.R;
import java.util.ArrayList;
import java.util.List;

public class MyFloatingActionButtonActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_fab);

        final RecyclerView recyclerView = findViewById(R.id.recycler);
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            list.add("test" + i);
        }
        MyAdapter adapter = new MyAdapter(list);
        recyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        MyRecyclerViewDivider divider = new MyRecyclerViewDivider(Color.BLUE);
        divider.setHorizontalSize(30);
        divider.setVerticalSize(20);
        recyclerView.addItemDecoration(divider);

        FloatingActionButton button = findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(recyclerView, "Hello!", Snackbar.LENGTH_LONG).show();
            }
        });
    }
}
