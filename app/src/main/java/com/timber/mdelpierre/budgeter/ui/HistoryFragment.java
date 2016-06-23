package com.timber.mdelpierre.budgeter.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.timber.mdelpierre.budgeter.R;
import com.timber.mdelpierre.budgeter.model.Transaction;
import com.timber.mdelpierre.budgeter.persistance.RealmHelper;
import com.timber.mdelpierre.budgeter.ui.adapter.HistoryListAdapter;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Matthieu on 15/06/2016.
 */
public class HistoryFragment extends Fragment {
    @Bind(R.id.lv_history_fragment)
    ListView mLlHistory;


    public static Fragment newInstance() {
        return new HistoryFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_history, container, false);

        ButterKnife.bind(this, v);
        List<Transaction> transactions = RealmHelper.getTransactionsOfAccount(getActivity());

        mLlHistory.setAdapter(new HistoryListAdapter(transactions, getActivity()));


        return v;
    }
}
