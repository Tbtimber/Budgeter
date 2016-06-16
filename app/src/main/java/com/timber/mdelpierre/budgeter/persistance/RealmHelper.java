package com.timber.mdelpierre.budgeter.persistance;

import android.content.Context;

import com.timber.mdelpierre.budgeter.global.ApplicationSharedPreferences;
import com.timber.mdelpierre.budgeter.model.Account;
import com.timber.mdelpierre.budgeter.model.Login;

import java.util.List;

import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

/**
 * Created by Matthieu on 16/06/2016.
 */
public class RealmHelper {

    private static Realm realm;
    private static RealmConfiguration realmconfig;

    public static void initRealm(Context context) {
        realmconfig = new RealmConfiguration.Builder(context).build();
        realm = Realm.getInstance(realmconfig);
    }

    public static void addLogin(Context context, final String login) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(new Login(login));
            }
        });
    }

    public static void addAccount(Context context, final String login, final String accountName) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<Login> logins = realm.where(Login.class).equalTo("login", login).findAll();
                if(logins != null && !logins.isEmpty()) {
                    logins.get(0).addAccount(accountName);
                }
            }
        });
    }

    public static Account getAccountForLogin(String login, String accountName) {
        RealmResults<Login> logins = realm.where(Login.class).equalTo("login", login).equalTo("accounts.name", accountName).findAll();
        for (Account ac: logins.get(0).getAccounts()) {
            if(ac.getName().equalsIgnoreCase(accountName)) {
                return ac;
            }
        }
        return null;
    }

    public static List<Account> getAccountsForLogin(String login) {
        RealmResults<Login> logins = realm.where(Login.class).equalTo("login", login).findAll();

        return logins.get(0).getAccounts();
    }
}
