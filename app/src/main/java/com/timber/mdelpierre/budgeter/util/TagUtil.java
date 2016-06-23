package com.timber.mdelpierre.budgeter.util;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.timber.mdelpierre.budgeter.R;
import com.timber.mdelpierre.budgeter.enumeration.TagEventTypeEnum;
import com.timber.mdelpierre.budgeter.model.TransactionGroup;
import com.timber.mdelpierre.budgeter.persistance.RealmHelper;
import com.timber.mdelpierre.budgeter.ui.eventBus.TagEvents;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by Matthieu on 20/06/2016.
 */
public class TagUtil {
    public static TextView createTVForTag(Context context, String name) {
        TextView tv = new TextView(context);
        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        llp.setMargins(10,10,10,10);
        tv.setLayoutParams(llp);
        tv.requestLayout();
        tv.setText(name);
        tv.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
        tv.setAlpha(0.35f);
        tv.setId(name.hashCode());
        return tv;
    }

    public static TextView createEmptyTVForTag(Context context) {
        TextView tv = new TextView(context);
        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        llp.setMargins(10,10,10,10);
        tv.setLayoutParams(llp);
        tv.requestLayout();
        tv.setText(" ");
        tv.setBackgroundColor(context.getResources().getColor(R.color.transparentwhite));
        tv.setAlpha(0.0f);
        return tv;
    }

    public static LinearLayout createLLforTag(Context context, String name) {
        final LinearLayout ll = new LinearLayout(context);
        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ll.setLayoutParams(llp);
        ll.setOrientation(LinearLayout.HORIZONTAL);
        ll.addView(createTVForTag(context, name));
        ll.addView(createEmptyTVForTag(context));
        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new TagEvents(ll));
            }
        });
        return ll;
    }

    public static void setTagLayouToNormal(LinearLayout ll) {
        TextView tv = (TextView)ll.getChildAt(0);
        tv.setAlpha(0.35f);
    }

    public static void setTagLayoutToSelect(LinearLayout ll) {
        TextView tv = (TextView)ll.getChildAt(0);
        tv.setAlpha(1.0f);
    }

    public static TransactionGroup getTopTag(Context context) {
        List<TransactionGroup> groups = GraphUtil.getTransactionGroups(RealmHelper.getTransactionsOfAccount(context));
        double value = 0;
        TransactionGroup biggest = null;
        for (TransactionGroup tg : groups) {
            if(tg.getValue() >= value) {
                biggest = tg;
            }
        }
        return biggest;
    }
}
