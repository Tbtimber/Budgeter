package com.timber.mdelpierre.budgeter.ui;

import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.timber.mdelpierre.budgeter.R;
import com.timber.mdelpierre.budgeter.enumeration.TransactionEventEnum;
import com.timber.mdelpierre.budgeter.global.ApplicationSharedPreferences;
import com.timber.mdelpierre.budgeter.model.Account;
import com.timber.mdelpierre.budgeter.persistance.RealmHelper;
import com.timber.mdelpierre.budgeter.ui.eventBus.TransactionEvent;
import com.timber.mdelpierre.budgeter.util.DashboardUtil;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

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

    @Bind(R.id.tv_dash_day_spending)
    TextView mTvDashDaySpending;

    @Bind(R.id.tv_dash_top_tag)
    TextView mTvDashTopTag;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dashboard, container, false);

        ButterKnife.bind(this, v);

        mTvDashName.setText(ApplicationSharedPreferences.getInstance(getActivity()).getCurrentAccount());
        mTvDashBalance.setText(String.valueOf(RealmHelper.getCurrentAccount(getActivity()).accountBalance));
        mTvDashDaySpending.setText(String.valueOf(DashboardUtil.getDaySpending(getActivity())));
        mTvDashTopTag.setText(DashboardUtil.getTopTag(getActivity()));


        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        EventBus.getDefault().unregister(this);
    }

    @OnClick(R.id.bt_dash_add_transaction)
    void addTransaction() {
        DialogFragment df = new DialogAddTransaction();
        df.show(getFragmentManager(),"");
    }


    private void refreshView() {
        mTvDashName.setText(ApplicationSharedPreferences.getInstance(getActivity()).getCurrentAccount());
        mTvDashBalance.setText(String.valueOf(RealmHelper.getCurrentAccount(getActivity()).accountBalance));
        mTvDashDaySpending.setText(String.valueOf(DashboardUtil.getDaySpending(getActivity())));
        mTvDashTopTag.setText(DashboardUtil.getTopTag(getActivity()));
    }

    @Subscribe
    public void onEvent(TransactionEvent event) {
        if(event.getmType() == TransactionEventEnum.TRANSACTION_ADDED) {
            refreshView();
        }
    }

}
