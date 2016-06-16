package com.timber.mdelpierre.budgeter.model;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by Matthieu on 16/06/2016.
 */
public class Login extends RealmObject {
    private String login;
    private RealmList<Account> accounts;

    public Login() {
    }

    public Login(String login) {
        this.login = login;
        accounts = new RealmList<>();
    }

    public void addAccount(String name) {
        if(accounts != null) {
            accounts.add(new Account(name));
        }
    }

}
