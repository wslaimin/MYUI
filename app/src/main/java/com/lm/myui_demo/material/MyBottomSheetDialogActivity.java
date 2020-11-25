package com.lm.myui_demo.material;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.lm.myui.widget.recycler.MyRecyclerViewDivider;
import com.lm.myui_demo.MyAdapter;
import com.lm.myui_demo.R;
import java.util.ArrayList;
import java.util.List;

public class MyBottomSheetDialogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_frg_bottom_sheet);
        BottomSheetFragment bottomSheet = (BottomSheetFragment) getSupportFragmentManager().findFragmentByTag("sheet");
        if (bottomSheet == null) {
            bottomSheet = new BottomSheetFragment();
            bottomSheet.show(getSupportFragmentManager(), "sheet");
        }
    }

    public static class BottomSheetFragment extends BottomSheetDialogFragment {
        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            List<String> data = new ArrayList<>();
            for (int i = 0; i < 60; i++) {
                data.add("test" + i);
            }
            RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.frg_bottom_sheet, container, false);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(new MyAdapter(data));
            MyRecyclerViewDivider divider = new MyRecyclerViewDivider(Color.BLUE);
            divider.setHorizontalSize(16);
            recyclerView.addItemDecoration(divider);
            return recyclerView;
        }
    }
}
