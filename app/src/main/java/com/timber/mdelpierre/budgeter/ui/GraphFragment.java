package com.timber.mdelpierre.budgeter.ui;

import android.app.Fragment;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.realm.implementation.RealmPieData;
import com.github.mikephil.charting.data.realm.implementation.RealmPieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.timber.mdelpierre.budgeter.R;
import com.timber.mdelpierre.budgeter.model.Transaction;
import com.timber.mdelpierre.budgeter.model.TransactionGroup;
import com.timber.mdelpierre.budgeter.persistance.RealmHelper;
import com.timber.mdelpierre.budgeter.util.GraphUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.RealmResults;

/**
 * Created by Matthieu on 15/06/2016.
 */
public class GraphFragment extends Fragment{

    public static Fragment newInstance() {
        return new GraphFragment();
    }

    @Bind(R.id.pie_chart)
    PieChart mPieChart;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_graphs, container, false);

        ButterKnife.bind(this, v);

        mPieChart.setDescription("");

        mPieChart.setHoleRadius(45f);
        mPieChart.setTransparentCircleRadius(50f);

        Legend l = mPieChart.getLegend();
        l.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);

        mPieChart.setData(generatePieData());

        return v;
    }

    protected PieData generatePieData() {
        ArrayList<Entry> entries1 = new ArrayList<>();
        ArrayList<String> xVals = new ArrayList<>();

        List<TransactionGroup> transactionGroups = GraphUtil.getTransactionGroups(RealmHelper.getTransactionsOfAccount(getActivity()));

        int i=0;
        for(TransactionGroup tg : transactionGroups) {
            xVals.add(tg.getTagName());
            entries1.add(new Entry((float)tg.getValue(), i));
            i++;
        }

        PieDataSet ds1 = new PieDataSet(entries1, "THUNE !");
        ds1.setColors(ColorTemplate.VORDIPLOM_COLORS);
        ds1.setSliceSpace(2f);
        ds1.setValueTextColor(Color.WHITE);
        ds1.setValueTextSize(12f);

        PieData d = new PieData(xVals, ds1);

        return d;
    }
}
