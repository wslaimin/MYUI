package com.lm.myui_demo;

import android.os.Bundle;
import android.util.TypedValue;
import android.widget.SeekBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.lm.myui.widget.round.MyRoundButton;
import com.lm.myui.widget.round.MyRoundTextView;

public class MyRoundActivity extends AppCompatActivity {
    private MyRoundButton myRoundButton;
    private MyRoundTextView myRoundTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_round);
        myRoundButton = findViewById(R.id.round_button);
        myRoundTextView = findViewById(R.id.round_text);

        SeekBar seekBar1 = findViewById(R.id.set_radius);
        SeekBar seekBar2 = findViewById(R.id.set_stroke_width);

        seekBar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                myRoundButton.setRadius(dp2px(progress));
                myRoundTextView.setRadius(dp2px(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                myRoundButton.setStrokeWidth(dp2px(progress));
                myRoundTextView.setStrokeWidth(dp2px(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private float dp2px(float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }
}
