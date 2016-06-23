package com.timber.mdelpierre.budgeter.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.timber.mdelpierre.budgeter.R;
import com.timber.mdelpierre.budgeter.model.Transaction;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Matthieu on 21/06/2016.
 */
public class HistoryListAdapter extends BaseAdapter {

    List<Transaction> mTransactions;
    private Context mContext;

    public HistoryListAdapter(List<Transaction> mTransactions, Context mContext) {
        this.mTransactions = mTransactions;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mTransactions != null ? mTransactions.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return mTransactions != null ? mTransactions.get(position) : null;
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
            convertView = inflater.inflate(R.layout.adapter_history_lv, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }

        if(position < mTransactions.size()) {
            Transaction tr = mTransactions.get(mTransactions.size()- 1 - position);
            holder.mTvalue.setText(String.valueOf(tr.value));
            holder.mTvTag.setText(tr.tag.name);

            DateTimeFormatter hourFormat = DateTimeFormat.forPattern("HH:mm");
            DateTimeFormatter dateFormat = DateTimeFormat.forPattern("dd/MM/yyyy");

            DateTime trDate = new DateTime(tr.date);
            holder.mTvDate.setText(trDate.toString(dateFormat));

        }

        return convertView;
    }

    public class ViewHolder {
        @Bind(R.id.tv_date_history)
        TextView mTvDate;

        @Bind(R.id.tv_tag_history)
        TextView mTvTag;

        @Bind(R.id.tv_value_history)
        TextView mTvalue;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
