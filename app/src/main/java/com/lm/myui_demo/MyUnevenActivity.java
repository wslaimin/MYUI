package com.lm.myui_demo;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.lm.myui.widget.MyFlowLayout;
import com.lm.myui.widget.round.MyRoundTextView;
import java.util.Random;

public class MyUnevenActivity extends AppCompatActivity {
    String str="A fool thinks himself to be wise but a wise man knows himself to be a fool";
    String[] strings;
    MyFlowLayout unevenLayout;
    Random random;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_uneven);
        unevenLayout=findViewById(R.id.uneven);
        random=new Random();
        strings=str.split(" ");
        addView();
        addView();
        addView();
    }

    public void onClick(View v){
        addView();
    }

    private void addView(){
        MyRoundTextView textView=new MyRoundTextView(this);
        textView.setText(strings[random.nextInt(strings.length)]);
        textView.setPadding(16,16,16,16);
        textView.setRadius(20);
        textView.setTextColor(Color.GRAY);
        textView.setBackgroundColor(Color.LTGRAY);
        unevenLayout.addView(textView);
    }
}
