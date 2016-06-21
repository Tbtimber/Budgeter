package com.timber.mdelpierre.budgeter.util;

import com.timber.mdelpierre.budgeter.model.Tag;
import com.timber.mdelpierre.budgeter.model.Transaction;
import com.timber.mdelpierre.budgeter.model.TransactionGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matthieu on 21/06/2016.
 */
public class GraphUtil {
    public static List<TransactionGroup> getTransactionGroups(List<Transaction> transactions) {
        List<Tag> bankTagName = new ArrayList<>();
        List<TransactionGroup> transactionGroups = new ArrayList<>();

        for(Transaction tr: transactions) {
            if(tr == null || tr.tag == null || tr.tag.name == null) {
                return transactionGroups;
            }
            int indice = findTagIndice(transactionGroups, tr.tag.name);
            if(indice == transactionGroups.size()) {
                transactionGroups.add(new TransactionGroup(tr.tag.name, tr.value));
            } else {
                transactionGroups.get(indice).addToTotal(tr.value);
            }
        }
        return transactionGroups;
    }

    public static int findTagIndice(List<TransactionGroup> transactionGroups, String curTag) {
        int returnValue = transactionGroups.size();
        for(int i=0;i< transactionGroups.size(); i++) {
            if(transactionGroups.get(i).getTagName().equalsIgnoreCase(curTag)) {
                returnValue = i;
            }
        }
        return returnValue;
    }
}
