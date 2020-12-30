package com.lm.myui_demo.material;

import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.lm.myui.widget.recyclerview.adapter.MyRecyclerViewDivider;
import com.lm.myui_demo.MyAdapter;
import com.lm.myui_demo.R;
import java.util.ArrayList;
import java.util.List;

public class MyTopAppBarActivity extends AppCompatActivity {
    private int scrollFlags, collapseMode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_top_app_bar);

        final CollapsingToolbarLayout toolbarLayout = findViewById(R.id.toolbar_layout);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
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

        scrollFlags = AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL;
        collapseMode = CollapsingToolbarLayout.LayoutParams.COLLAPSE_MODE_OFF;

        final MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                item.setChecked(!item.isChecked());
                int itemId = item.getItemId();
                if (item.isChecked()) {
                    if (itemId == R.id.scroll) {
                        scrollFlags |= AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL;
                    } else if (itemId == R.id.enter_always) {
                        scrollFlags |= AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS;
                    } else if (itemId == R.id.enter_always_collapsed) {
                        scrollFlags |= AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS_COLLAPSED;
                    } else if (itemId == R.id.exit_until_collapsed) {
                        scrollFlags |= AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED;
                    } else if (itemId == R.id.none) {
                        collapseMode = CollapsingToolbarLayout.LayoutParams.COLLAPSE_MODE_OFF;
                    } else if (itemId == R.id.pin) {
                        collapseMode = CollapsingToolbarLayout.LayoutParams.COLLAPSE_MODE_PIN;
                    } else if (itemId == R.id.parallax) {
                        collapseMode = CollapsingToolbarLayout.LayoutParams.COLLAPSE_MODE_PARALLAX;
                    }
                } else {
                    if (itemId == R.id.scroll) {
                        scrollFlags ^= AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL;
                    } else if (itemId == R.id.enter_always) {
                        scrollFlags ^= AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS;
                    } else if (itemId == R.id.enter_always_collapsed) {
                        scrollFlags ^= AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS_COLLAPSED;
                    } else if (itemId == R.id.exit_until_collapsed) {
                        scrollFlags ^= AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED;
                    } else if (itemId == R.id.none) {
                        collapseMode ^= CollapsingToolbarLayout.LayoutParams.COLLAPSE_MODE_OFF;
                    } else if (itemId == R.id.pin) {
                        collapseMode ^= CollapsingToolbarLayout.LayoutParams.COLLAPSE_MODE_PIN;
                    } else if (itemId == R.id.parallax) {
                        collapseMode ^= CollapsingToolbarLayout.LayoutParams.COLLAPSE_MODE_PARALLAX;
                    }
                }
                ((AppBarLayout.LayoutParams) toolbarLayout.getLayoutParams()).setScrollFlags(scrollFlags);
                toolbarLayout.setLayoutParams(toolbarLayout.getLayoutParams());

                ((CollapsingToolbarLayout.LayoutParams) toolbar.getLayoutParams()).setCollapseMode(collapseMode);
                toolbar.setLayoutParams(toolbar.getLayoutParams());
                return true;
            }
        });
    }
}
