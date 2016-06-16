package com.timber.mdelpierre.budgeter.model;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by Matthieu on 16/06/2016.
 */
public class Account extends RealmObject {
    private RealmList<Transaction> transactions;
    private String name;


    public Account() {
    }

    public Account(String name) {
        transactions = new RealmList<>();
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
