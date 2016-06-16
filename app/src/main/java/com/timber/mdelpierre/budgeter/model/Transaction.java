package com.timber.mdelpierre.budgeter.model;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Matthieu on 16/06/2016.
 */
public class Transaction extends RealmObject {
    @PrimaryKey
    private long id;
    private Date date;
    private double value;
    private String tag;

    public Transaction(Date date, double value, String tag) {
        this.date = date;
        this.value = value;
        this.tag = tag;
    }
}
