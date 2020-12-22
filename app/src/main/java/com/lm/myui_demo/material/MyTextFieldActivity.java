package com.lm.myui_demo.material;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputLayout;
import com.lm.myui_demo.R;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.CASE_INSENSITIVE;

public class MyTextFieldActivity extends AppCompatActivity {
    private TextInputLayout userEdit, pwdEdit;
    private int textInputStyle;
    private boolean userValid, pwdValid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            textInputStyle = savedInstanceState.getInt("textInputStyle", R.style.TextInputFilled);
        }
        getTheme().applyStyle(textInputStyle, true);
        setContentView(R.layout.act_text_field);

        userEdit = findViewById(R.id.user);
        pwdEdit = findViewById(R.id.password);

        userEdit.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Boolean bool = check(s.toString());
                if (bool == null) {
                    userEdit.setHelperText("Please input user name");
                    userValid = false;
                } else if (!bool) {
                    userEdit.setHelperText("Only letter and number");
                    userValid = false;
                } else {
                    userEdit.setHelperText(null);
                    userValid = true;
                }
            }
        });

        pwdEdit.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Boolean bool = check(s.toString());
                if (bool == null) {
                    pwdEdit.setHelperText("Please input password");
                    pwdValid = false;
                } else if (!bool) {
                    pwdEdit.setHelperText("Only letter and number");
                    pwdValid = false;
                } else {
                    pwdEdit.setHelperText(null);
                    pwdValid = true;
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.text_field_style, menu);
        return true;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("textInputStyle", textInputStyle);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.filled) {
            textInputStyle = R.style.TextInputFilled;
            recreate();
            return true;
        } else if (id == R.id.outlined) {
            textInputStyle = R.style.TextInputOutlined;
            recreate();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    public void onClick(View view) {
        if (!userValid) {
            userEdit.setHelperText("Please input user name");
        }
        if (!pwdValid) {
            pwdEdit.setHelperText("Please input password");
        }
        if (userValid && pwdValid) {
            finish();
        }
    }

    private Boolean check(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        Pattern pattern = Pattern.compile("^[a-z0-9]+", CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }


}
