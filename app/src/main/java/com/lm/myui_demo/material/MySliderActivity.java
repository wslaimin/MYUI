package com.lm.myui_demo.material;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.slider.RangeSlider;
import com.lm.myui_demo.R;

public class MySliderActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_slider);
        RangeSlider rangeSlider=findViewById(R.id.range_slider);
        rangeSlider.setValues(10f,100f);
        rangeSlider.setValueFrom(10);
        rangeSlider.setValueTo(100);
    }
}
