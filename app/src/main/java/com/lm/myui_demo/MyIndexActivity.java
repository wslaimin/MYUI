package com.lm.myui_demo;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.lm.myui.widget.indexer.MyBubbleView;
import com.lm.myui.widget.indexer.MyIndexBar;

public class MyIndexActivity extends AppCompatActivity {
    private MyIndexBar bar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_index);
        bar=findViewById(R.id.bar);
        final MyBubbleView bubbleView=new MyBubbleView(this);
        bar.setBubbleView(bubbleView);
        bar.setIndexListener(new MyIndexBar.IndexListener() {
            @Override
            public void touchIndex(int position) {
                bubbleView.setText(bar.getIndexStr(position));
            }

            @Override
            public void touchOver() {

            }
        });
    }
}
