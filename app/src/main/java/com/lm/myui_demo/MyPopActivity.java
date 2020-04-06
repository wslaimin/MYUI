package com.lm.myui_demo;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.lm.myui.widget.MyPopupWindow;

public class MyPopActivity extends AppCompatActivity {
    MyPopupWindow pop;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_pop);
        View v= LayoutInflater.from(this).inflate(R.layout.pop_item,null);
        v.findViewById(R.id.text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),"click",Toast.LENGTH_LONG).show();
            }
        });

        pop=new MyPopupWindow(v);
        pop.setFocusable(true);
        pop.setOutsideTouchable(true);
        pop.setTouchable(true);
        pop.setTouchModal(false);
        pop.setRestDrawable(new ColorDrawable(Color.parseColor("#991B1B1B")));
    }

    public void onClick(View v){
        pop.showAsDropDown(v);
    }
}
