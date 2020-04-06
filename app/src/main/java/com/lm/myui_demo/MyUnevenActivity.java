package com.lm.myui_demo;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.lm.myui.widget.MyUnevenLayout;

public class MyUnevenActivity extends AppCompatActivity {
    MyUnevenLayout unevenLayout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_uneven);
        unevenLayout=findViewById(R.id.uneven);
    }

    public void onClick(View v){
        TextView textView=new TextView(this);
        textView.setText("text");
        textView.setBackgroundResource(R.color.colorPrimary);
        unevenLayout.addView(textView);
    }
}
