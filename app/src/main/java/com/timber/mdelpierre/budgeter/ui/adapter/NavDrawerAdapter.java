package com.timber.mdelpierre.budgeter.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.timber.mdelpierre.budgeter.R;
import com.timber.mdelpierre.budgeter.model.Account;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Matthieu on 16/06/2016.
 */
public class NavDrawerAdapter extends BaseAdapter {
    private List<Account> mAccounts;
    private Context mContext;

    public NavDrawerAdapter(List<Account> mAccounts, Context mContext) {
        this.mAccounts = mAccounts;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return (mAccounts != null) ? mAccounts.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return (mAccounts != null) ? mAccounts.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewHolder holder;
        if(convertView != null) {
            holder = (ViewHolder)convertView.getTag();
        } else {
            convertView = inflater.inflate(R.layout.adapter_drawer, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        if(position < mAccounts.size()) {
            holder.getmTvNavAdapter().setText(mAccounts.get(position).getName());
        }


        return convertView;
    }

    public class ViewHolder {
        @Bind(R.id.tv_nav_bar_adapter)
        TextView mTvNavAdapter;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        public TextView getmTvNavAdapter() {
            return mTvNavAdapter;
        }
    }
}
