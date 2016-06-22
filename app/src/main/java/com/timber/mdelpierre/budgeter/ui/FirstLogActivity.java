package com.timber.mdelpierre.budgeter.ui;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.timber.mdelpierre.budgeter.MainActivity;
import com.timber.mdelpierre.budgeter.R;
import com.timber.mdelpierre.budgeter.global.ApplicationSharedPreferences;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Matthieu on 22/06/2016.
 */
public class FirstLogActivity extends AppCompatActivity {



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_log);
        ButterKnife.bind(this);


        DialogFragment addAccount = new DialogAddAccount();
        addAccount.show(getFragmentManager(), "");
        ApplicationSharedPreferences.getInstance(this).setFirstConenction(false);
    }

    @OnClick(R.id.bt_go_main)
    void goMain() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
