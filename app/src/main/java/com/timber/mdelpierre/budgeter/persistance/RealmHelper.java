package com.timber.mdelpierre.budgeter.persistance;

import android.content.Context;
import android.util.Log;

import com.timber.mdelpierre.budgeter.enumeration.LoginEventEnum;
import com.timber.mdelpierre.budgeter.enumeration.TagEventTypeEnum;
import com.timber.mdelpierre.budgeter.global.ApplicationSharedPreferences;
import com.timber.mdelpierre.budgeter.model.Account;
import com.timber.mdelpierre.budgeter.model.Login;
import com.timber.mdelpierre.budgeter.model.Tag;
import com.timber.mdelpierre.budgeter.model.Transaction;
import com.timber.mdelpierre.budgeter.ui.eventBus.LoginEvent;
import com.timber.mdelpierre.budgeter.ui.eventBus.TagEvents;

import org.greenrobot.eventbus.EventBus;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * Created by Matthieu on 16/06/2016.
 */
public class RealmHelper {

    private static Realm realm;
    private static RealmConfiguration realmconfig;

    // Initialization
    // ---------------------------------------------------------------------------------------

    public static void initRealm(Context context) {
        realmconfig = new RealmConfiguration.Builder(context).build();
        realm = Realm.getInstance(realmconfig);
    }

    public static void attachListener(RealmChangeListener listener) {
        realm.addChangeListener(listener);
    }

    // Getter
    // ---------------------------------------------------------------------------------------
    /**
     * Return the Login RealmObject that corresponds to the CurrentLogin
     * @param context set to the current Context of the app
     * @return Login realmObject
     */
    public static Login getCurrentLogin(Context context) {
        return realm.where(Login.class).equalTo("login", ApplicationSharedPreferences.getInstance(context).getCurrentLogin()).findFirst();
    }

    /**
     * Return the Account RealmObject that is currently selected
     * @param context context of the app
     * @return Account RealmObject that should not be modified
     */
    public static Account getCurrentAccount(Context context) {
        return getCurrentLogin(context).accounts.where().equalTo("name", ApplicationSharedPreferences.getInstance(context).getCurrentAccount()).findFirst();
    }

    public static List<Account> getAccounts(Context context) {
        return getCurrentLogin(context).accounts;
    }


    /**
     * Return a List of all the transactions the current account has
     * @param context app context
     * @return List<Transactions> of the current account
     */
    public static List<Transaction> getTransactionsOfAccount(Context context) {
        return getCurrentAccount(context).transactions;
    }

    public static List<Tag> getTagsFromRealm(Context context) {
        return realm.where(Tag.class).findAll();
    }

    public static Tag getTagFromRealm(Context context, String tagName) {
        return realm.where(Tag.class).equalTo("name", tagName).findFirst();
    }

    public static double getBalanceOfAccount(Context context) {
        return getCurrentAccount(context).accountBalance;
    }

    // "Adder"
    // ---------------------------------------------------------------------------------------
    public static boolean addLoginToRealm(final Context context, final String login) {
        if(login == null) {
            return false;
        }
        Login log = new Login();
        log.login = login;
        return addLoginToRealm(context, log);
    }

    private static boolean addLoginToRealm(final Context context, final Login login) {
        if(login == null || login.login == null || isInRealm(context, login)) {
            return false;
        } else {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    Login log = realm.createObject(Login.class);
                    log.login = login.login;
                }
            });
        }
        EventBus.getDefault().post(new LoginEvent(LoginEventEnum.LOGGED));
        return true;

    }

    public static boolean addAccountToRealm(final Context context, final String name) {
        if(name == null) {
            return false;
        }
        Account ac = new Account();
        ac.name = name;
        return addAccountToRealm(context, ac);
    }

    private static boolean addAccountToRealm(final Context context, final Account account) {
        if(account == null || account.name == null || isInRealm(context, account)) {
            return false;
        } else {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    Account ac = realm.createObject(Account.class);
                    ac.name = account.name;
                    ac.accountBalance = 0;

                    Transaction tr = realm.createObject(Transaction.class);
                    tr.value = 0;
                    Tag tg = realm.createObject(Tag.class);
                    tg.name = " ";
                    tr.tag = tg;

                    ac.transactions.add(tr);
                    getCurrentLogin(context).accounts.add(ac);
                }
            });
        }
        return true;
    }


    public static boolean addTransactionToRealm(Context context, Date date, double value, String tagName) {
        if(date == null || tagName == null) {
            return false;
        }
        Transaction tr = new Transaction();
        tr.date = date;
        tr.value = value;
        tr.tag = getTagFromRealm(context, tagName);
        return addTransactionToRealm(context, tr);
    }

    private static boolean addTransactionToRealm(final Context context, final Transaction transaction) {
        if(transaction == null || transaction.tag == null || transaction.date == null) {
            return false;
        } else {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    Transaction tr = realm.createObject(Transaction.class);
                    tr.date = transaction.date;
                    tr.tag = transaction.tag;
                    tr.value = transaction.value;

                    getCurrentAccount(context).transactions.add(tr);
                }
            });
        }
        return true;
    }


    public static boolean addTagToRealm(Context context, String tagName) {
        if(tagName == null) {
            return false;
        }
        Tag tg = new Tag();
        tg.name = tagName;
        return addTagToRealm(context,tg);
    }

    private static boolean addTagToRealm(Context context, final Tag tag) {
        if (tag == null || tag.name == null || isInRealm(context, tag)) {
            return false;
        } else {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    Tag tg = realm.createObject(Tag.class);
                    tg.name = tag.name;
                }
            });
        }
        return true;
    }

    // Utils
    // ---------------------------------------------------------------------------------------
    public static boolean isInRealm(Context context, RealmObject obj) {
        if(obj instanceof Login) {
            return isLoginInRealm(context, (Login)obj);
        } else if (obj instanceof Account){
            return isAccountInRealm(context, (Account)obj);
        } else if (obj instanceof Tag) {
            return isTagInRealm(context, (Tag)obj);
        }
        return true;
    }

    private static boolean isLoginInRealm(Context context, Login login) {
        RealmResults<Login> logins = realm.where(Login.class).findAll();
        for (Login lg: logins) {
            if(lg.login.equalsIgnoreCase(login.login)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isAccountInRealm(Context context, Account account) {
        List<Account> accounts = getCurrentLogin(context).accounts;
        for (Account ac : accounts) {
            if(ac.name.equalsIgnoreCase(account.name)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isTagInRealm(Context context, Tag tag) {
        List<Tag> tags = getTagsFromRealm(context);
        for(Tag tg : tags) {
            if(tg.name.equalsIgnoreCase(tag.name)) {
                return true;
            }
        }
        return false;
    }


}
