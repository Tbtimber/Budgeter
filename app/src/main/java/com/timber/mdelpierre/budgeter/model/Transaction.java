package com.timber.mdelpierre.budgeter.model;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by Matthieu on 16/06/2016.
 */
public class Transaction extends RealmObject {
    @PrimaryKey
    public long id;

    public Date date;

    public double value;

    public Tag tag;

    public Transaction() {
    }

    public Transaction(Date date, double value, Tag tag) {
        this.date = date;
        this.value = value;
        this.tag = tag;
    }

    public double getValue() {
        return value;
    }
}
