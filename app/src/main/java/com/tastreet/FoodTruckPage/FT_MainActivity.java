package com.tastreet.FoodTruckPage;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.tastreet.R;

public class FT_MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    ActionBar actionBar;

    public static FestivalFragment festivalFragment = FestivalFragment.getInstance();

    DrawerLayout drawerLayout;
    NavigationView navigationView;

    FrameLayout main_panel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ft__main);

        main_panel = findViewById(R.id.main_panel);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        actionBar.setDisplayHomeAsUpEnabled(true);


        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                switch(item.getItemId()){
                    case R.id.navigation_item_my_foodtruck:
                        break;

                    case R.id.navigation_item_festival:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        setFestivalFragment();
                        break;

                    case R.id.navigation_item_notification:
                        break;

                    case R.id.navigation_item_settings:
                        break;

                    case R.id.navigation_item_logout:
                        break;
                }
                return true;
            }
        });
    }


    private void setFestivalFragment(){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction().replace(R.id.main_panel, festivalFragment);
        fragmentTransaction.commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
