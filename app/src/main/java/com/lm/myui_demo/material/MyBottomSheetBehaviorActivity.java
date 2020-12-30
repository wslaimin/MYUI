package com.lm.myui_demo.material;

import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.lm.myui.widget.recyclerview.adapter.MyRecyclerViewDivider;
import com.lm.myui_demo.MyAdapter;
import com.lm.myui_demo.R;
import java.util.ArrayList;
import java.util.List;

public class MyBottomSheetBehaviorActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private BottomSheetBehavior<RecyclerView> behavior;
    private int alternativeHeight;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_bottom_sheet);
        List<String> data = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            data.add("test" + i);
        }
        recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new MyAdapter(data));
        MyRecyclerViewDivider divider = new MyRecyclerViewDivider(Color.BLUE);
        divider.setHorizontalSize(16);
        recyclerView.addItemDecoration(divider);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        behavior = (BottomSheetBehavior<RecyclerView>) ((CoordinatorLayout.LayoutParams) recyclerView.getLayoutParams()).getBehavior();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.bottom_sheet, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.hideable:
                behavior.setHideable(!behavior.isHideable());
                item.setTitle(behavior.isHideable() ? "hideable:false" : "hideable:true");
                return true;
            case R.id.peek_height:
                if (alternativeHeight == 0) {
                    alternativeHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 120, getResources().getDisplayMetrics());
                }
                int tempHeight = behavior.getPeekHeight();
                behavior.setPeekHeight(alternativeHeight);
                alternativeHeight = tempHeight;
                item.setTitle(alternativeHeight==-1 ? "peekHeight:120dp" : "peekHeight:AUTO");
                return true;
            case R.id.skip_collapsed:
                behavior.setSkipCollapsed(!behavior.getSkipCollapsed());
                item.setTitle(behavior.getSkipCollapsed() ? "skipCollapsed:false" : "skipCollapsed:true");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
