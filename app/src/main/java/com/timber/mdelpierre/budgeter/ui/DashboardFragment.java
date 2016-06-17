package com.timber.mdelpierre.budgeter.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.timber.mdelpierre.budgeter.R;
import com.timber.mdelpierre.budgeter.global.ApplicationSharedPreferences;
import com.timber.mdelpierre.budgeter.model.Account;
import com.timber.mdelpierre.budgeter.persistance.RealmHelper;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * Created by Matthieu on 15/06/2016.
 */
public class DashboardFragment extends Fragment {


    @Bind(R.id.et_value_transaction)
    EditText mEtTrValue;
    @Bind(R.id.cb_isIncome)
    CheckBox mCbIsIncome;
    @Bind(R.id.tv_show_Balance)
    TextView mTbShowBalance;
    @Bind(R.id.mainTVDASHBOARD)
    TextView mTvDashboard;
    public static Fragment newInstance() {
        return new DashboardFragment();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dashboard, container, false);

        ButterKnife.bind(this, v);
        mTvDashboard.setText(ApplicationSharedPreferences.getInstance(getActivity()).getCurrentAccount());
        mTbShowBalance.setText("Current Balance : " + RealmHelper.getBalanceForAccount(ApplicationSharedPreferences.getInstance(getActivity()).getCurrentLogin(), ApplicationSharedPreferences.getInstance(getActivity()).getCurrentAccount()));

        RealmHelper.attachListener(new RealmChangeListener() {
            @Override
            public void onChange(Object element) {
                mTbShowBalance.setText("Current Balance : " + RealmHelper.getBalanceForAccount(ApplicationSharedPreferences.getInstance(getActivity()).getCurrentLogin(), ApplicationSharedPreferences.getInstance(getActivity()).getCurrentAccount()));
            }
        });

        return v;
    }


    @OnClick(R.id.bt_registerTransaction)
    void registerTransaction() {
        try {
            double trValue = Double.parseDouble(mEtTrValue.getText().toString());
            if(!mCbIsIncome.isChecked()) {
                trValue *= -1;
            }

            RealmHelper.addTransactionToAccount(getActivity(), ApplicationSharedPreferences.getInstance(getActivity()).getCurrentLogin(),ApplicationSharedPreferences.getInstance(getActivity()).getCurrentAccount(), trValue);
        } catch (NumberFormatException e) {
            return;
        }

    }
}
