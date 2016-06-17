package com.timber.mdelpierre.budgeter.persistance;

import android.content.Context;
import android.util.Log;

import com.timber.mdelpierre.budgeter.global.ApplicationSharedPreferences;
import com.timber.mdelpierre.budgeter.model.Account;
import com.timber.mdelpierre.budgeter.model.Login;
import com.timber.mdelpierre.budgeter.model.Transaction;

import java.util.List;

import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmChangeListener;
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

    public static void addLogin(final Context context, final String login) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                ApplicationSharedPreferences.getInstance(context).incrementNbLogin();
                Login log = realm.createObject(Login.class);
                log.login = login;
                log.id = ApplicationSharedPreferences.getInstance(context).getNbLogin();
            }
        });
    }

    public static void addAccount(final Context context, final String login, final String accountName) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<Login> logins = realm.where(Login.class).equalTo("login", login).findAll();
                if(logins != null && !logins.isEmpty()) {
                    ApplicationSharedPreferences.getInstance(context).incrementNbLogin();
                    Account ac = realm.createObject(Account.class);
                    ac.name = accountName;
                    ac.id = ApplicationSharedPreferences.getInstance(context).getNbLogin();
                    logins.get(0).accounts.add(ac);
                }
            }
        });
    }

    public static List<Account> getAccountsForLogin(String login) {
        RealmResults<Login> logins = realm.where(Login.class).equalTo("login", login).findAll();

        return logins.get(0).getAccounts();
    }

    public static void addTransactionToAccount(final Context context, final String login, final String account, final double value) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                ApplicationSharedPreferences.getInstance(context).incrementNbLogin();
                Transaction tr = realm.createObject(Transaction.class);
                tr.id = ApplicationSharedPreferences.getInstance(context).getNbLogin();
                tr.value = value;

                Login lg = realm.where(Login.class).equalTo("login", login).findFirst();
                for(Account ac:lg.getAccounts()) {
                    if(ac.name.equalsIgnoreCase(account)) {
                        ac.transactions.add(tr);
                        ac.accountBalance = ac.transactions.sum("value").doubleValue();
                    }
                }
            }
        });

        Log.e("REALMHELPER", "Transaction added of : " + value);
    }

    public static void attachListener(RealmChangeListener listener) {
        realm.addChangeListener(listener);
    }

    public static double getBalanceForAccount(final String login, final String accountName) {
        double balance = 0;
        Login lg = realm.where(Login.class).equalTo("login", login).findFirst();
        for(Account ac: lg.getAccounts()) {
            if(ac.name.equalsIgnoreCase(accountName)) {
                balance = ac.accountBalance;
            }
        }
        return balance;
    }
}
