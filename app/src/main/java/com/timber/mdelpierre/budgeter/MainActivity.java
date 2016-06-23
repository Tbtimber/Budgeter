package com.timber.mdelpierre.budgeter;

import android.app.DialogFragment;
import android.app.Fragment;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.DatabaseErrorHandler;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabClickListener;
import com.timber.mdelpierre.budgeter.enumeration.AccountEventTypeEnum;
import com.timber.mdelpierre.budgeter.global.ApplicationSharedPreferences;
import com.timber.mdelpierre.budgeter.model.Account;
import com.timber.mdelpierre.budgeter.model.Transaction;
import com.timber.mdelpierre.budgeter.persistance.RealmHelper;
import com.timber.mdelpierre.budgeter.ui.DashboardFragment;
import com.timber.mdelpierre.budgeter.ui.DialogAddAccount;
import com.timber.mdelpierre.budgeter.ui.DialogAddTransaction;
import com.timber.mdelpierre.budgeter.ui.GraphFragment;
import com.timber.mdelpierre.budgeter.ui.HistoryFragment;
import com.timber.mdelpierre.budgeter.ui.LoginActivity;
import com.timber.mdelpierre.budgeter.ui.adapter.NavDrawerAdapter;
import com.timber.mdelpierre.budgeter.ui.eventBus.AccountEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;


public class MainActivity extends AppCompatActivity implements OnTabClickListener{

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.content_frame)
    FrameLayout mContentLayout;

    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @Bind(R.id.left_drawer)
    ListView mDrawerList;

    @Bind(R.id.tv_nav_header_name)
    TextView mTvHeaderName;

    @Bind(R.id.ll_main)
    LinearLayout mLlMain;

    //Private field
    private ActionBarDrawerToggle mDrawerToggle;
    private BottomBar mBottomNav; //https://github.com/roughike/BottomBar
    private NavDrawerAdapter mNavAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        RealmHelper.initRealm(this);
        EventBus.getDefault().register(this);


        mTvHeaderName.setText(ApplicationSharedPreferences.getInstance(this).getCurrentLogin());

        //Set Bottom Nav Bar
        mBottomNav = BottomBar.attach(mLlMain, savedInstanceState);
        mBottomNav.setMaxFixedTabs(2);
        mBottomNav.setItems(R.menu.bottom_nav_bar);
        mBottomNav.setOnTabClickListener(this);

        //Setup Drawer Layout
        setSupportActionBar(mToolbar);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {

            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mNavAdapter = new NavDrawerAdapter(RealmHelper.getAccounts(this) ,this);


        mDrawerList.setAdapter(mNavAdapter);
        mDrawerList.setOnItemClickListener(mDrawerListViewListener);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,  mToolbar, R.string.drawer_open, R.string.drawer_closed) {
            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerToggle.syncState();
        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.addDrawerListener(mDrawerToggle);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mBottomNav.onSaveInstanceState(outState);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        DialogFragment d = new DialogAddTransaction();
        d.show(getFragmentManager(), "");
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(int position) {

        switch (position) {
            case 0:
                FragmentManager fm0 = getFragmentManager();
                fm0.beginTransaction().replace(R.id.content_frame, DashboardFragment.newInstance()).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
                break;
            case 1:
                FragmentManager fm1 = getFragmentManager();
                fm1.beginTransaction().replace(R.id.content_frame, GraphFragment.newInstance()).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
                break;
            case 2:
                FragmentManager fm2 = getFragmentManager();
                fm2.beginTransaction().replace(R.id.content_frame, HistoryFragment.newInstance()).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
                break;
            default:

                break;
        }
    }

    @Override
    public void onTabReSelected(int position) {

    }

    @OnClick(R.id.tv_disonnect_drawer)
    void disconnect() {
        ApplicationSharedPreferences.getInstance(this).setIsConnected(false);
        ApplicationSharedPreferences.getInstance(this).setCurrentLogin("");
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @OnClick(R.id.tv_add_account)
    void addAccount() {
        DialogFragment addAccount = new DialogAddAccount();
        addAccount.show(getFragmentManager(), "");
    }

    // Drawer Listener
    // -----------------------------------------------------------------------------------------
    private AdapterView.OnItemClickListener mDrawerListViewListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Account ac = (Account)mNavAdapter.getItem(position);
            ApplicationSharedPreferences.getInstance(getApplicationContext()).setCurrentAccount(ac.name);
            onTabSelected(mBottomNav.getCurrentTabPosition());
            mDrawerLayout.closeDrawers();
        }
    };


    @Subscribe
    public void onEvent(AccountEvent event) {
        if(event.getType() == AccountEventTypeEnum.ACCOUNT_ADDED) {
            onTabSelected(mBottomNav.getCurrentTabPosition());
            mDrawerLayout.closeDrawers();
        }
    }
}
