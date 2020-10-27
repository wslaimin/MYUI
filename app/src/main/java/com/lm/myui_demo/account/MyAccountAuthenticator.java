package com.lm.myui_demo.account;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import java.util.Random;

public class MyAccountAuthenticator extends AbstractAccountAuthenticator {
    private Context context;
    private AccountManager am;

    public static String getRandomString(int length) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    public MyAccountAuthenticator(Context context) {
        super(context);
        this.context = context;
        am = (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);
    }

    @Override
    public Bundle editProperties(AccountAuthenticatorResponse response, String accountType) {
        return null;
    }

    @Override
    public Bundle addAccount(AccountAuthenticatorResponse response, String accountType, String authTokenType, String[] requiredFeatures, Bundle options) throws NetworkErrorException {
        Bundle bundle = new Bundle();
        Intent intent = new Intent(context, MyLoginActivity.class);
        bundle.putParcelable(AccountManager.KEY_INTENT, intent);
        return bundle;
    }

    @Override
    public Bundle confirmCredentials(AccountAuthenticatorResponse response, Account account, Bundle options) throws NetworkErrorException {
        return null;
    }

    @Override
    public Bundle getAuthToken(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
        String password = am.getPassword(account);
        Bundle bundle = new Bundle();
        if (password != null) {
            bundle.putString(AccountManager.KEY_ACCOUNT_TYPE, account.type);
            bundle.putString(AccountManager.KEY_ACCOUNT_NAME, account.name);
            String token = getRandomString(16);
            bundle.putString(AccountManager.KEY_AUTHTOKEN, token);
            am.setAuthToken(account, authTokenType, token);
        }
        return bundle;
    }

    @Override
    public String getAuthTokenLabel(String authTokenType) {
        return null;
    }

    @Override
    public Bundle updateCredentials(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
        return null;
    }

    @Override
    public Bundle hasFeatures(AccountAuthenticatorResponse response, Account account, String[] features) throws NetworkErrorException {
        return null;
    }
}
