package com.lm.myui_demo.material;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialog;
import androidx.fragment.app.DialogFragment;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.lm.myui_demo.R;

public class MyDialogActivity extends AppCompatActivity {
    private int checkedItem;
    private int oldCheckedItem;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_dialog);
        checkedItem = oldCheckedItem = 0;
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.alert:
                new MaterialAlertDialogBuilder(this).setTitle("Reset Settings")
                        .setMessage("This will reset your device to its default factory settings.").setPositiveButton("ACCEPT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MyDialogActivity.this, "click positive button", Toast.LENGTH_LONG).show();
                    }
                }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MyDialogActivity.this, "click negative button", Toast.LENGTH_LONG).show();
                    }
                }).show();
                break;
            case R.id.simple:
                String[] items = new String[10];
                for (int i = 0; i < items.length; i++) {
                    items[i] = "item" + i;
                }
                new MaterialAlertDialogBuilder(this).setTitle("Title")
                        .setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(MyDialogActivity.this, "click item" + which, Toast.LENGTH_LONG).show();
                            }
                        }).show();
                break;
            case R.id.confirm:
                items = new String[10];
                for (int i = 0; i < items.length; i++) {
                    items[i] = "item" + i;
                }
                new MaterialAlertDialogBuilder(this).setTitle("Title")
                        .setSingleChoiceItems(items, checkedItem, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                checkedItem = which;
                                Toast.makeText(MyDialogActivity.this, "click item" + which, Toast.LENGTH_LONG).show();
                            }
                        }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        oldCheckedItem = checkedItem;
                        Toast.makeText(MyDialogActivity.this, "click positive button", Toast.LENGTH_LONG).show();
                    }
                }).setNeutralButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        checkedItem = oldCheckedItem;
                        Toast.makeText(MyDialogActivity.this, "click neutral button", Toast.LENGTH_LONG).show();
                    }
                }).show();
                break;
            case R.id.full:
                new MyDialog().show(getSupportFragmentManager(), "myDialog");
                break;
        }
    }

    public static class MyDialog extends DialogFragment {

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.dlg_full, container, false);
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
            return new MyFullScreenDialog(getContext(), 0);
        }
    }

    public static class MyFullScreenDialog extends AppCompatDialog {
        public MyFullScreenDialog(Context context, int theme) {
            super(context, theme);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            Window window = getWindow();
            if (window != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                }
                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                window.setBackgroundDrawable(null);
            }
        }
    }
}
