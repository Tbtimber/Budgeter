package com.timber.mdelpierre.budgeter.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.timber.mdelpierre.budgeter.MainActivity;
import com.timber.mdelpierre.budgeter.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Matthieu on 15/06/2016.
 */
public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);
    }


    @OnClick(R.id.bt_login)
    void login() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }


}
