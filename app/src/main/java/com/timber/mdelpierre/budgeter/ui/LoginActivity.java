package com.timber.mdelpierre.budgeter.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.timber.mdelpierre.budgeter.MainActivity;
import com.timber.mdelpierre.budgeter.R;
import com.timber.mdelpierre.budgeter.global.ApplicationSharedPreferences;
import com.timber.mdelpierre.budgeter.persistance.RealmHelper;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Matthieu on 15/06/2016.
 */
public class LoginActivity extends AppCompatActivity {

    @Bind(R.id.et_login_login)
    EditText mEtLogin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);
        RealmHelper.initRealm(this);


    }


    @OnClick(R.id.bt_login)
    void login() {
        if(mEtLogin.getText().length() < 2) {
            Toast.makeText(this, getResources().getText(R.string.toast_login_invalid), Toast.LENGTH_LONG ).show();
        } else {
            ApplicationSharedPreferences.getInstance(this).setIsConnected(true);
            ApplicationSharedPreferences.getInstance(this).setCurrentLogin(mEtLogin.getText().toString());
            if(RealmHelper.addLoginToRealm(this,mEtLogin.getText().toString())) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            } else {
                Toast.makeText(this,getResources().getString(R.string.toast_wrong_login), Toast.LENGTH_LONG).show();
            }
        }
    }


}
