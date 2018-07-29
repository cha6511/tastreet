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

import com.tastreet.EventBus.Events;
import com.tastreet.EventBus.GlobalBus;
import com.tastreet.Festival.FestivalListData;
import com.tastreet.MonthlyFestival.MonthlyFestivalListData;
import com.tastreet.R;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import static com.tastreet.EventBus.Events.CURRENT_PAGE;
import static com.tastreet.EventBus.Events.FT_FESTIVAL_DETAIL_FRAGMENT;
import static com.tastreet.EventBus.Events.FT_FESTIVAL_FRAGMENT;
import static com.tastreet.EventBus.Events.FT_MAIN_FRAGMENT;
import static com.tastreet.EventBus.Events.FT_MONTHLY_FESTIVAL_DETAIL_FRAGMENT;

public class FT_MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    ActionBar actionBar;

    public static FT_FestivalFragment FTFestivalFragment = FT_FestivalFragment.getInstance();
    public static FT_MainFragment ft_mainFragment = FT_MainFragment.getInstance();

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

        setMainFragment();
    }


    private void setFestivalFragment(){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction().replace(R.id.main_panel, FTFestivalFragment);
        fragmentTransaction.commit();
    }

    private void setMainFragment(){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction().replace(R.id.main_panel, ft_mainFragment);
        fragmentTransaction.commit();
    }

    private void setMonthlyFestivalDetailFragment(MonthlyFestivalListData data){

        Bundle bundle = new Bundle();
        bundle.putSerializable("monthlyDetailData", data);

        FT_MonthlyFestivalDetailFragment ft_monthlyFestivalDetailFragment = new FT_MonthlyFestivalDetailFragment();
        ft_monthlyFestivalDetailFragment.setArguments(bundle);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction().replace(R.id.main_panel, ft_monthlyFestivalDetailFragment);
        fragmentTransaction.commit();
    }

    private void setFestivalDetailFragment(FestivalListData data){
        Bundle bundle = new Bundle();
        bundle.putSerializable("festivalDetailData", data);

        FT_FestivalDetailFragment ft_festivalDetailFragment = new FT_FestivalDetailFragment();
        ft_festivalDetailFragment.setArguments(bundle);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction().replace(R.id.main_panel, ft_festivalDetailFragment);
        fragmentTransaction.commit();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getMonthlyFestivalData(Events.SendMonthlyFestivalData sendMonthlyFestivalData){
        setMonthlyFestivalDetailFragment(sendMonthlyFestivalData.getData());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getFestivalData(Events.SendFestivalData sendFestivalData){
        setFestivalDetailFragment(sendFestivalData.getData());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getMessage(Events.Msg msg){
        if(Events.BACK_BUTTON_PRESS.equals(msg.getMsg())){
            onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        GlobalBus.getBus().getDefault().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        GlobalBus.getBus().getDefault().unregister(this);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        if(CURRENT_PAGE.equals(FT_MAIN_FRAGMENT)){
            finish();
        } else if(CURRENT_PAGE.equals(FT_MONTHLY_FESTIVAL_DETAIL_FRAGMENT)){
            setMainFragment();
        } else if(CURRENT_PAGE.equals(FT_FESTIVAL_DETAIL_FRAGMENT)){
            setFestivalFragment();
        } else if(CURRENT_PAGE.equals(FT_FESTIVAL_FRAGMENT)){
            setMainFragment();
        }
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
