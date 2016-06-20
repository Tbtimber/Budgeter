package com.timber.mdelpierre.budgeter.util;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.timber.mdelpierre.budgeter.R;

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
        return tv;
    }

    public static View createSmallView(Context context) {
        View v = new View(context);
        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        v.setLayoutParams(llp);
        v.requestLayout();
        v.setMinimumWidth(5);
        return v;
    }
}
