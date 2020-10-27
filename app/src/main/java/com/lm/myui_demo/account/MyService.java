package com.lm.myui_demo.account;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import androidx.annotation.Nullable;

public class MyService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MyAccountAuthenticator(this).getIBinder();
    }
}
