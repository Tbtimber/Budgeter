package com.timber.mdelpierre.budgeter.ui;

import android.app.DialogFragment;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
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

    public static Fragment newInstance() {
        return new DashboardFragment();
    }

    @Bind(R.id.tv_dash_name)
    TextView mTvDashName;

    @Bind(R.id.tv_dash_balance)
    TextView mTvDashBalance;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dashboard, container, false);

        ButterKnife.bind(this, v);

        mTvDashName.setText(ApplicationSharedPreferences.getInstance(getActivity()).getCurrentAccount());
        mTvDashBalance.setText(String.valueOf(RealmHelper.getCurrentAccount(getActivity()).accountBalance));

        return v;
    }


    @OnClick(R.id.bt_dash_add_transaction)
    void addTransaction() {
        DialogFragment df = new DialogAddTransaction();
        df.show(getFragmentManager(),"");
    }
}
