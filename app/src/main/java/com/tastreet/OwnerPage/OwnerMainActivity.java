package com.tastreet.OwnerPage;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.tastreet.EventBus.Events;
import com.tastreet.EventBus.GlobalBus;
import com.tastreet.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OwnerMainActivity extends AppCompatActivity {

    @BindView(R.id.appBar)
    AppBarLayout appBar;
    @BindView(R.id.main_panel)
    FrameLayout mainPanel;

    SearchMethodFragment searchMethodFragment = SearchMethodFragment.getInstance();
    DirectMatchingFragment directMatchingFragment = DirectMatchingFragment.getInstance();
    InquireMatchingFragment inquireMatchingFragment = InquireMatchingFragment.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_main);
        ButterKnife.bind(this);

        appBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                ViewCompat.setElevation(appBarLayout, 20);
            }
        });

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_panel, searchMethodFragment).commitAllowingStateLoss();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getMessage(Events.Msg msg){
        if(Events.SEARCH_METHOD_FRAGMENT.equals(msg.getMsg())){
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.main_panel, searchMethodFragment).commitAllowingStateLoss();
        } else if(Events.DIRECT_MATCHING_FRAGMENT.equals(msg.getMsg())){
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.main_panel, directMatchingFragment).commitAllowingStateLoss();
        } else if(Events.INQUIRY_MATCHING_FRAGMENT.equals(msg.getMsg())){
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.main_panel, inquireMatchingFragment).commitAllowingStateLoss();
        } else if(Events.FINISH_MATCHING.equals(msg.getMsg())){
            finish();
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
        if(Events.CURRENT_PAGE.equals(Events.SEARCH_METHOD_FRAGMENT)){
            finish();
        } else if(Events.CURRENT_PAGE.equals(Events.DIRECT_MATCHING_FRAGMENT) ||
                Events.CURRENT_PAGE.equals(Events.INQUIRY_MATCHING_FRAGMENT)){
            Events.Msg msg = new Events.Msg(Events.SEARCH_METHOD_FRAGMENT);
            GlobalBus.getBus().post(msg);
        }
    }
}
