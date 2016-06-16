package com.timber.mdelpierre.budgeter;

import android.app.Fragment;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.database.DatabaseErrorHandler;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabClickListener;
import com.timber.mdelpierre.budgeter.ui.DashboardFragment;
import com.timber.mdelpierre.budgeter.ui.GraphFragment;
import com.timber.mdelpierre.budgeter.ui.HistoryFragment;

import butterknife.Bind;
import butterknife.ButterKnife;



public class MainActivity extends AppCompatActivity implements OnTabClickListener{

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.content_frame)
    FrameLayout mContentLayout;

    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @Bind(R.id.left_drawer)
    ListView mDrawerList;

    //Private field
    private String[] mPlanetTiles = {"Earth","Neptune","URANUS"};
    private ActionBarDrawerToggle mDrawerToggle;
    private BottomBar mBottomNav; //https://github.com/roughike/BottomBar



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        //Set Bottom Nav Bar
        mBottomNav = BottomBar.attach(mContentLayout, savedInstanceState);
        mBottomNav.setMaxFixedTabs(2);
        mBottomNav.setItems(R.menu.bottom_nav_bar);
        mBottomNav.setOnTabClickListener(this);

        //Setup Drawer Layout
        setSupportActionBar(mToolbar);
        mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_tv, mPlanetTiles));

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
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mBottomNav.onSaveInstanceState(outState);
    }

    @Override
    public void onTabSelected(int position) {
        Log.e("Test","Tab selected : " + position);
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
}
