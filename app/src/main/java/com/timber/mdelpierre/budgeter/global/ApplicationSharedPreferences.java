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

}
