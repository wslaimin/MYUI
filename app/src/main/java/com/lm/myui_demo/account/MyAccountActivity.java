package com.lm.myui_demo.account;

import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.lm.myui_demo.R;
import com.lm.account.annotations.MyAutoToken;
import com.lm.account.annotations.MyToken;

public class MyAccountActivity extends AppCompatActivity {
    private TextView text;
    AccountManager am;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_account);
        text = findViewById(R.id.token);
        am = (AccountManager) getSystemService(Context.ACCOUNT_SERVICE);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.invalidate:
                am.invalidateAuthToken(getPackageName(),text.getText().toString());
                text.setText(null);
                break;
            case R.id.get_token:
                MyAccountActivityWithAutoToken.getData(this);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        MyAccountActivityWithAutoToken.getDataWithToken(this,requestCode,resultCode);
    }

    @MyAutoToken(receiveResult = true)
    public void getData(@MyToken String token) {
        text.setText(token);
    }
}
