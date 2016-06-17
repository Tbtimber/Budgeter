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

    public int getNbAccount() {
        return applicationSharedPreferences.getInt(NB_ACCOUNT, -1);
    }

    public void setNbAccount(int value) {
        editor.putInt(NB_ACCOUNT, value);
        editor.commit();
    }

    public int getNbLogin() {
        return applicationSharedPreferences.getInt(NB_LOGIN, -1);
    }

    public void setNbLogin(int value) {
        editor.putInt(NB_LOGIN, value);
        editor.commit();
    }

    public void incrementNbLogin() {
        editor.putInt(NB_LOGIN, getNbLogin() + 1);
        editor.commit();
    }

    public void incrementNbAccount() {
        editor.putInt(NB_ACCOUNT, getNbAccount() + 1);
        editor.commit();
    }

    public long getNbTransaction() {
        return applicationSharedPreferences.getLong(NB_TRANSACTION, -1);
    }

    public void setNbTransaction(long value) {
        editor.putLong(NB_TRANSACTION, value);
        editor.commit();
    }

    public void incrementNbTransaction() {
        editor.putLong(NB_TRANSACTION, getNbTransaction() + 1);
        editor.commit();
    }
}
