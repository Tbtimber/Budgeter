package com.timber.mdelpierre.budgeter.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.timber.mdelpierre.budgeter.R;

/**
 * Created by Matthieu on 15/06/2016.
 */
public class HistoryFragment extends Fragment {
    public static Fragment newInstance() {
        return new HistoryFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_history, container, false);

        return v;
    }
}
