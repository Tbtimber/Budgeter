package com.timber.mdelpierre.budgeter.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.timber.mdelpierre.budgeter.MainActivity;
import com.timber.mdelpierre.budgeter.R;
import com.timber.mdelpierre.budgeter.global.ApplicationSharedPreferences;

/**
 * Created by Matthieu on 16/06/2016.
 */
public class SplashActivity extends AppCompatActivity {
    private final int SPLASHSCREEN_DELAY = 2000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        verifiyPref();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if(ApplicationSharedPreferences.getInstance(getApplicationContext()).getIsConnected()) {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                } else {
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    finish();
                }
            }
        }, SPLASHSCREEN_DELAY);

    }

    public void verifiyPref() {
        if(ApplicationSharedPreferences.getInstance(this).getNbAccount() == -1) {
            ApplicationSharedPreferences.getInstance(this).setNbAccount(0);
        }
        if(ApplicationSharedPreferences.getInstance(this).getNbLogin() == -1) {
            ApplicationSharedPreferences.getInstance(this).setNbLogin(0);
        }
    }
}
