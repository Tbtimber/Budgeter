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

                final Date now = new Date(System.currentTimeMillis());

                tr.date = now;

                Login lg = realm.where(Login.class).equalTo("login", login).findFirst();
                for(Account ac:lg.getAccounts()) {
                    if(ac.name.equalsIgnoreCase(account)) {
                        ac.transactions.add(tr);
                        ac.accountBalance = ac.transactions.sum("value").doubleValue();
                        Collections.sort(ac.transactions, new Comparator<Transaction>() {
                            @Override
                            public int compare(Transaction lhs, Transaction rhs) {
                                return -lhs.date.compareTo(rhs.date);
                            }
                        });
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

    public static void addTagToRealm2(final Context context, final String name) {
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

    // Start of recode
    // ---------------------------------------------------------------------------------------


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

    public static boolean addLoginToRealm(final Context context, final Login login) {
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

    public static boolean addAccountToRealm(final Context context, final Account account) {
        if(account == null || account.name == null || isInRealm(context, account)) {
            return false;
        } else {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    Account ac = realm.createObject(Account.class);
                    ac.name = account.name;
                    ac.accountBalance = 0;

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

    public static boolean addTransactionToRealm(final Context context, final Transaction transaction) {
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
    }


    public static boolean addTagToRealm(Context context, String tagName) {
        if(tagName == null) {
            return false;
        }
        Tag tg = new Tag();
        tg.name = tagName;
        return addTagToRealm(context,tg);
    }

    public static boolean addTagToRealm(Context context, final Tag tag) {
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
