package com.lm.myui_demo;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.lm.myui.widget.MyRoundLayout;

import java.util.Random;

public class MyRoundButtonActivity extends AppCompatActivity {
    private MyRoundLayout roundButton;
    private Random random=new Random();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_round);
        roundButton=findViewById(R.id.btn);
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.set_radius:
                roundButton.setRadius(random.nextInt(100));
            break;
            case R.id.set_top_left_radius:
                roundButton.setTopLeftRadius(random.nextInt(60));
                break;
            case R.id.set_stroke_width:
                roundButton.setStrokeWidth(random.nextInt(60));
                break;
            case R.id.set_stroke_color:
                roundButton.setStrokeColor(Color.YELLOW);
                break;
        }
    }
}
