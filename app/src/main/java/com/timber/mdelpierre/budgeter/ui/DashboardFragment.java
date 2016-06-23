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

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.realm.implementation.RealmLineDataSet;
import com.timber.mdelpierre.budgeter.R;
import com.timber.mdelpierre.budgeter.enumeration.TransactionEventEnum;
import com.timber.mdelpierre.budgeter.global.ApplicationSharedPreferences;
import com.timber.mdelpierre.budgeter.model.Account;
import com.timber.mdelpierre.budgeter.model.Transaction;
import com.timber.mdelpierre.budgeter.model.TransactionGroup;
import com.timber.mdelpierre.budgeter.persistance.RealmHelper;
import com.timber.mdelpierre.budgeter.ui.eventBus.TransactionEvent;
import com.timber.mdelpierre.budgeter.util.DashboardUtil;
import com.timber.mdelpierre.budgeter.util.GraphUtil;
import com.timber.mdelpierre.budgeter.util.TagUtil;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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

    @Bind(R.id.chart_dashoard)
    LineChart mLineChart;


    private TransactionGroup mTopGroup;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dashboard, container, false);


        ButterKnife.bind(this, v);

        refreshView();

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



    private void refreshView() {
        mTopGroup = DashboardUtil.getTopTag(getActivity());
        if(mTopGroup != null) {
            mTvDashTopTag.setText(mTopGroup.getTagName());
        } else {
            mTvDashTopTag.setText("N/A");
        }

        mTvDashName.setText(ApplicationSharedPreferences.getInstance(getActivity()).getCurrentAccount());
        mTvDashBalance.setText(String.valueOf(RealmHelper.getCurrentAccount(getActivity()).accountBalance));
        mTvDashDaySpending.setText(String.valueOf(DashboardUtil.getDaySpending(getActivity())));

        setupGraph();
    }

    private void setupGraph() {
        LineDataSet dataSet = new LineDataSet(GraphUtil.getTransactionsAsEntry(getActivity()), "Evolution of account balance");
        List<String> xVals = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM");
        List<Transaction> trs = RealmHelper.getTransactionsOfAccount(getActivity());


        for (int i = 0; i < trs.size(); i++) {
            DateTime date = new DateTime(trs.get(i).date);
            xVals.add(date.toString(formatter));
        }

        LineData lineData = new LineData(xVals, dataSet);


        mLineChart.getAxisRight().setEnabled(false);
        mLineChart.getLegend().setPosition(Legend.LegendPosition.ABOVE_CHART_CENTER);
        mLineChart.setData(lineData);
        mLineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        mLineChart.invalidate();
    }


    @Subscribe
    public void onEvent(TransactionEvent event) {
        if(event.getmType() == TransactionEventEnum.TRANSACTION_ADDED) {
            refreshView();
        }
    }

}
