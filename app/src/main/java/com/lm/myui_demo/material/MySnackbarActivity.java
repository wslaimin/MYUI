package com.lm.myui_demo.material;

import android.os.Bundle;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.lm.myui_demo.R;

public class MySnackbarActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_snackbar);

        final FloatingActionButton button=findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(button, "Retrieve action", Snackbar.LENGTH_LONG).setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).show();
            }
        });
    }
}
