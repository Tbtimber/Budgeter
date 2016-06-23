package com.timber.mdelpierre.budgeter.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Matthieu on 20/06/2016.
 */
public class Tag extends RealmObject {
    //@PrimaryKey
    public long id;
    public String name;
}
