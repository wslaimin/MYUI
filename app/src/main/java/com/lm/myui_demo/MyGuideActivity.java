package com.lm.myui_demo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.lm.myui.widget.guider.MyGuideContent;
import com.lm.myui.widget.guider.MyGuideFocus;
import com.lm.myui.widget.guider.MyGuider;
import com.lm.myui.widget.guider.MyOffset;

public class MyGuideActivity extends AppCompatActivity {
    private Button button1,button2,button3;
    private  MyGuider myGuider;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_guide);
        button1=findViewById(R.id.button1);
        button2=findViewById(R.id.button2);
        button3=findViewById(R.id.button3);
        showGuide();
    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.button1:
            case R.id.button2:
            case R.id.button3:
                myGuider.showNext();
            break;
        }
    }

    private void showGuide(){
        LayoutInflater inflater=LayoutInflater.from(this);
        View view;
        view=inflater.inflate(R.layout.guide_one,null,false);

        myGuider=new MyGuider(this);
        myGuider.setMaskColor(0x80000000);
        MyGuideFocus guideFocus=new MyGuideFocus(this);
        guideFocus.setFocusView(button1);
        guideFocus.addGuideContent(new MyGuideContent(view,MyGuideContent.LEFT_TO_RIGHT));
        myGuider.addGuideFocus(guideFocus);

        view=inflater.inflate(R.layout.guide_one,null,false);
        guideFocus=new MyGuideFocus(this);
        guideFocus.addGuideContent(new MyGuideContent(view, MyGuideContent.RIGHT_TO_LEFT));
        guideFocus.setFocusView(button2);
        myGuider.addGuideFocus(guideFocus);

        view=inflater.inflate(R.layout.guide_one,null,false);
        guideFocus=new MyGuideFocus(this);
        MyGuideContent content=new MyGuideContent(view, MyGuideContent.HORIZONTAL_CENTER|MyGuideContent.BOTTOM_TO_TOP);
        guideFocus.addGuideContent(content);
        guideFocus.setFocusView(button3);
        guideFocus.setPadding(20,20,20,20);
        guideFocus.setTouchable(true);
        guideFocus.setShape(MyGuideFocus.Shape.Circle);
        myGuider.addGuideFocus(guideFocus);
        myGuider.show();
    }
}
