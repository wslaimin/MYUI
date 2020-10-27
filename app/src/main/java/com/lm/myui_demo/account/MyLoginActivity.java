package com.lm.myui_demo.account;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.lm.myui_demo.R;

public class MyLoginActivity extends AppCompatActivity {
    private EditText account, password;
    private Button confirm;
    private AccountManager am;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_login);
        account = findViewById(R.id.account);
        password = findViewById(R.id.password);
        confirm = findViewById(R.id.confirm);

        am = (AccountManager) getSystemService(Context.ACCOUNT_SERVICE);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ac=account.getText().toString();
                String pwd=password.getText().toString();
                if(!TextUtils.isEmpty(ac)&&!TextUtils.isEmpty(pwd)) {
                    Bundle bundle=new Bundle();
                    bundle.putString("time",System.currentTimeMillis()+"");
                    if(!am.addAccountExplicitly(new Account(ac,getPackageName()),pwd,bundle)){
                        am.setUserData(new Account(ac,getPackageName()),"time",System.currentTimeMillis()+"");
                    }
                    setResult(RESULT_OK);
                    finish();
                }
            }
        });
    }
}
