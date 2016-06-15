package com.timber.mdelpierre.budgeter;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.roughike.bottombar.BottomBar;

import butterknife.Bind;
import butterknife.ButterKnife;



public class MainActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @Bind(R.id.left_drawer)
    ListView mDrawerList;

    //Private field
    private String[] mPlanetTiles = {"Earth","Neptune","URANUS"};
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private BottomBar mBottomNav; //https://github.com/roughike/BottomBar



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        //Setup Drawer Layout
        mDrawerTitle = mTitle = getTitle();

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

        //Set Bottom Nav Bar
        mBottomNav = BottomBar.attach(this, savedInstanceState);
        mBottomNav.setItems(R.menu.bottom_nav_bar);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mBottomNav.onSaveInstanceState(outState);
    }
}
