package com.timber.mdelpierre.budgeter.model;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Matthieu on 16/06/2016.
 */
public class Account extends RealmObject {
    @PrimaryKey
    private long id;
    private RealmList<Transaction> transactions;
    private String name;
    private double accountBalance;


    public Account() {
    }

    public Account(String name) {
        transactions = new RealmList<>();
        this.name = name;
        accountBalance = 0;
    }

    public String getName() {
        return name;
    }

    public void addTransaction(Transaction transaction) {
        accountBalance += transaction.getValue();
        transactions.add(transaction);
    }
}
