package com.timber.mdelpierre.budgeter.global;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Matthieu on 16/06/2016.
 */
public class ApplicationSharedPreferences {

    public static final String CURRENT_LOGIN = "current_login";
    public static final String CURRENT_ACCOUNT = "current_account";
    private static final String PREFS_NAME = "BudgeterSharedPreferences";
    public static final String IS_CONNECTED = "isConnected";
    public static final String NB_LOGIN = "nbLogin";
    public static final String NB_ACCOUNT = "nbAccount";
    public static final String NB_TRANSACTION = "nbTransaction";
    public static final String FIRST_CONNECTION = "hasAccount";
    // Private fields
    // ---------------------------------------------------------------------------------------------
    private static ApplicationSharedPreferences instance;
    private static SharedPreferences applicationSharedPreferences;
    private static SharedPreferences.Editor editor;

    // Constructor
    // ---------------------------------------------------------------------------------------------
    private ApplicationSharedPreferences(Context context) {
        applicationSharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = applicationSharedPreferences.edit();
    }

    // Singleton
    // ---------------------------------------------------------------------------------------------
    public static ApplicationSharedPreferences getInstance(Context context) {
        if (instance == null) {
            instance = new ApplicationSharedPreferences(context);
        }
        return instance;
    }

    // Setter & Getter
    // ---------------------------------------------------------------------------------------------
    public String getCurrentLogin() {
        return applicationSharedPreferences.getString(CURRENT_LOGIN,null);
    }

    public void setCurrentLogin(String value) {
        editor.putString(CURRENT_LOGIN, value);
        editor.commit();
    }

    public String getCurrentAccount() {
        return applicationSharedPreferences.getString(CURRENT_ACCOUNT, null);
    }

    public void setCurrentAccount(String value) {
        editor.putString(CURRENT_ACCOUNT, value);
        editor.commit();
    }

    public boolean getIsConnected() {
        return applicationSharedPreferences.getBoolean(IS_CONNECTED, false);
    }

    public void setIsConnected(boolean value) {
        editor.putBoolean(IS_CONNECTED, value);
    }


    public boolean getFirstConnection() {
        return applicationSharedPreferences.getBoolean(FIRST_CONNECTION, true);
    }

    public void setFirstConenction(boolean value) {
        editor.putBoolean(FIRST_CONNECTION, value);
        editor.commit();
    }
}
