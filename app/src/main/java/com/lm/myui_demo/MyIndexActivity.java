package com.lm.myui_demo;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.lm.myui.widget.MyIndexBar;

public class MyIndexActivity extends AppCompatActivity {
    private MyIndexBar bar;
    private TextView text;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_index);
        text=findViewById(R.id.text);
        text.setVisibility(View.INVISIBLE);
        bar=findViewById(R.id.bar);
        bar.setIndexListener(new MyIndexBar.IndexListener() {
            @Override
            public void index(int position, float touchX, float touchY) {
                text.setVisibility(View.VISIBLE);
                text.setText(bar.getIndexStr(position));
                text.setTranslationY(touchY-(text.getTop()+text.getBottom())/2f);
            }

            @Override
            public void over() {
                text.setVisibility(View.INVISIBLE);
            }
        });
    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.set_index_color:
                bar.setIndexColor(0xFFFF0000);
                break;
            case R.id.set_size:
                bar.setTextSize(16);
                break;
            case R.id.set_space:
                bar.setVerticalSpace(10);
                break;
            case R.id.set_text_color:
                bar.setTextColor(0xFF0000FF);
                break;
        }
    }
}
