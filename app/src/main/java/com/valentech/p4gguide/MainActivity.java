package com.valentech.p4gguide;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.valentech.p4gguide.fragment.DayFragment;
import com.valentech.p4gguide.fragment.DayPickerFragment;
import com.valentech.p4gguide.fragment.HomeFragment;
import com.valentech.p4gguide.fragment.MonthPickerFragment;
import com.valentech.p4gguide.fragment.SocialLinkListFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if(savedInstanceState == null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, new HomeFragment()).commit();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switchContentFragment(id);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void switchContentFragment(int id) {
        FragmentManager fragmentManager = getFragmentManager();
        Fragment fragment;
        switch (id) {
            case R.id.nav_social_links:
                fragment = fragmentManager.findFragmentByTag("social link fragment");
                if(fragment == null) {
                    fragment = new SocialLinkListFragment();
                }
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment, "social link fragment").
                        addToBackStack("social links").commit();
                break;
            case R.id.nav_walkthrough:
                fragment = fragmentManager.findFragmentByTag("walkthrough fragment month picker");
                if(fragment == null) {
                    fragment = new MonthPickerFragment();
                }
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment, "walkthrough fragment month picker").
                        addToBackStack("walkthrough picker").commit();
                break;
            case R.id.nav_home:
            default:
                fragmentManager.beginTransaction().replace(R.id.content_frame, new HomeFragment()).
                        addToBackStack("home").commit();
                break;
        }
    }
}