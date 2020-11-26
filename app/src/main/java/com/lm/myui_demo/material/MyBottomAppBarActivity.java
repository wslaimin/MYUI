package com.lm.myui_demo.material;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.MenuItem;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.ShapeAppearanceModel;
import com.google.android.material.shape.TriangleEdgeTreatment;
import com.lm.myui.widget.recycler.MyRecyclerViewDivider;
import com.lm.myui_demo.MyAdapter;
import com.lm.myui_demo.R;
import java.util.ArrayList;
import java.util.List;

public class MyBottomAppBarActivity extends AppCompatActivity {
    private ShapeAppearanceModel alternativeShapeModel;
    private Drawable alternativeBg;
    private float alternativeEvaluation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_bottom_bar);
        List<String> data = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            data.add("test" + i);
        }
        RecyclerView recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new MyAdapter(data));
        MyRecyclerViewDivider divider = new MyRecyclerViewDivider(Color.BLUE);
        divider.setHorizontalSize(16);
        recyclerView.addItemDecoration(divider);

        final BottomAppBar bottomAppBar = findViewById(R.id.bottom_bar);
        final FloatingActionButton button = findViewById(R.id.button);
        alternativeEvaluation = 0;
        bottomAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.display:
                        bottomAppBar.setHideOnScroll(!bottomAppBar.getHideOnScroll());
                        item.setTitle(bottomAppBar.getHideOnScroll() ? "fix on scroll" : "hide on scroll");
                        return true;
                    case R.id.style:
                        if (alternativeShapeModel == null) {
                            MyPyramidEdgeTreatment pyramidEdgeTreatment1 = new MyPyramidEdgeTreatment();
                            pyramidEdgeTreatment1.setSize(button.getHeight());

                            MyPyramidEdgeTreatment pyramidEdgeTreatment2 = new MyPyramidEdgeTreatment();
                            pyramidEdgeTreatment2.setSize(button.getHeight());
                            alternativeShapeModel = new ShapeAppearanceModel.Builder().setTopEdge(pyramidEdgeTreatment1).setBottomEdge(pyramidEdgeTreatment2).build();
                        }

                        ShapeAppearanceModel tempModel = button.getShapeAppearanceModel();
                        button.setShapeAppearanceModel(alternativeShapeModel);
                        alternativeShapeModel = tempModel;

                        float tempEvaluation = ViewCompat.getElevation(button);
                        ViewCompat.setElevation(button, alternativeEvaluation);
                        alternativeEvaluation = tempEvaluation;

                        if (alternativeBg == null) {
                            float size = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 32, getResources().getDisplayMetrics());
                            TriangleEdgeTreatment triangleEdgeTreatment = new TriangleEdgeTreatment(size, true);
                            ShapeAppearanceModel shapeModel = new ShapeAppearanceModel.Builder().setTopEdge(triangleEdgeTreatment).build();
                            MaterialShapeDrawable bottomBarBg = new MaterialShapeDrawable(shapeModel);
                            bottomBarBg.setTint(Color.BLUE);
                            alternativeBg = bottomBarBg;
                        }
                        Drawable tempBg = bottomAppBar.getBackground();
                        bottomAppBar.setBackground(alternativeBg);
                        alternativeBg = tempBg;
                        return true;
                    default:
                        return false;
                }
            }
        });
    }
}
