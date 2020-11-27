package com.lm.myui_demo.material;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.lm.myui_demo.R;

public class MyButtonActivity extends AppCompatActivity {
    private CheckBox checkBox;
    private MaterialButtonToggleGroup group;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_button);
        group=findViewById(R.id.group);
        checkBox=findViewById(R.id.checkbox);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                group.setSingleSelection(isChecked);
            }
        });
    }
}
