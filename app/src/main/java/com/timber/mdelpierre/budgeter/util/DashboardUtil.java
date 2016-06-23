package com.timber.mdelpierre.budgeter.util;

import android.content.Context;

import com.timber.mdelpierre.budgeter.model.Transaction;
import com.timber.mdelpierre.budgeter.model.TransactionGroup;
import com.timber.mdelpierre.budgeter.persistance.RealmHelper;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by Matthieu on 23/06/2016.
 */
public class DashboardUtil {
    public static TransactionGroup getTopTag(Context context) {
        return TagUtil.getTopTag(context);
    }

    public static double getDaySpending(Context context) {
        List<Transaction> transactions = getTransactionOfTheDay(RealmHelper.getTransactionsOfAccount(context));
        double total = 0;
        for (Transaction tr : transactions) {
            total += Math.abs(tr.getValue());
        }
        return total;
    }

    public static List<Transaction> getTransactionOfTheDay(List<Transaction> transactions) {
        List<Transaction> trs = new ArrayList<>();
        DateTime now = new DateTime(DateTime.now());
        DateTime yesterday = now.minusDays(1);
        for (Transaction tr : transactions) {
            if(tr.date.before(now.toDate()) && tr.date.after(yesterday.toDate()) && tr.value < 0) {
                trs.add(tr);
            }
        }
        return trs;
    }
}
