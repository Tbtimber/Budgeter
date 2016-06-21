package com.timber.mdelpierre.budgeter.persistance;

import android.content.Context;
import android.util.Log;

import com.timber.mdelpierre.budgeter.enumeration.TagEventTypeEnum;
import com.timber.mdelpierre.budgeter.global.ApplicationSharedPreferences;
import com.timber.mdelpierre.budgeter.model.Account;
import com.timber.mdelpierre.budgeter.model.Login;
import com.timber.mdelpierre.budgeter.model.Tag;
import com.timber.mdelpierre.budgeter.model.Transaction;
import com.timber.mdelpierre.budgeter.ui.eventBus.TagEvents;

import org.greenrobot.eventbus.EventBus;

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
                    ApplicationSharedPreferences.getInstance(context).setFirstConenction(false);
                }
            }
        });
    }

    public static List<Account> getAccountsForLogin(String login) {
        RealmResults<Login> logins = realm.where(Login.class).equalTo("login", login).findAll();

        return logins.get(0).getAccounts();
    }

    public static void addTransactionToAccount(final Context context, final String login, final String account, final double value, final String tagName) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                ApplicationSharedPreferences.getInstance(context).incrementNbLogin();
                Transaction tr = realm.createObject(Transaction.class);
                tr.id = ApplicationSharedPreferences.getInstance(context).getNbLogin();
                tr.value = value;

                Tag tg = realm.where(Tag.class).equalTo("name", tagName).findFirst();
                if(tg == null) {
                    addTagToRealmNoTransaction(context, tagName);
                }

                tr.tag = tg;
                Login lg = realm.where(Login.class).equalTo("login", login).findFirst();
                for(Account ac:lg.getAccounts()) {
                    if(ac.name.equalsIgnoreCase(account)) {
                        ac.transactions.add(tr);
                        ac.accountBalance = ac.transactions.sum("value").doubleValue();
                    }
                }
            }
        });
    }

    public static void addTransactionToAccount(final Context context, final String login, final String account, final double value) {
        addTransactionToAccount(context, login, account, value, "default");
    }

    public static Account getTransactionsForAccount(final Context context) {
        return realm.where(Login.class).equalTo("login", ApplicationSharedPreferences.getInstance(context).getCurrentLogin()).findFirst()
                .accounts.where().equalTo("name", ApplicationSharedPreferences.getInstance(context).getCurrentAccount()).findFirst();
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
    public static void addTagToRealmNoTransaction(final Context context, final String name) {
        if(isTagUnique(name)) {
            ApplicationSharedPreferences.getInstance(context).incrementNbLogin();
            Tag tg = realm.createObject(Tag.class);
            tg.id = ApplicationSharedPreferences.getInstance(context).getNbLogin();
            tg.name = name;
        }
    }

    public static void addTagToRealm(final Context context, final String name) {
        if(isTagUnique(name)) {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    ApplicationSharedPreferences.getInstance(context).incrementNbLogin();
                    Tag tg = realm.createObject(Tag.class);
                    tg.id = ApplicationSharedPreferences.getInstance(context).getNbLogin();
                    tg.name = name;
                }
            });
        } else {
            return;
        }
    }

    private static boolean isTagUnique(String name) {
        List<Tag> tags = realm.where(Tag.class).findAll();
        for(Tag tg : tags) {
            if(tg.name.equalsIgnoreCase(name)) {
                EventBus.getDefault().post(new TagEvents(TagEventTypeEnum.ALREADY_EXISTS));
                return false;
            }
        }
        return true;
    }

    public static List<Tag> getTags() {
        return realm.where(Tag.class).findAll();
    }
}
