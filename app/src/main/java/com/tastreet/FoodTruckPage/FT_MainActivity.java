package com.tastreet.FoodTruckPage;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.tastreet.AsyncDone;
import com.tastreet.EventBus.Events;
import com.tastreet.EventBus.GlobalBus;
import com.tastreet.FoodTruckPage.Festival.FestivalListData;
import com.tastreet.FoodTruckPage.MonthlyFestival.MonthlyFestivalListData;
import com.tastreet.OwnerPage.FoodListData;
import com.tastreet.R;
import com.tastreet.SharedPref;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;

import okhttp3.OkHttpClient;

import static com.tastreet.EventBus.Events.CURRENT_PAGE;
import static com.tastreet.EventBus.Events.FT_ABOUT_FRAGMENT;
import static com.tastreet.EventBus.Events.FT_CHARGE_RATE_FRAGMENT;
import static com.tastreet.EventBus.Events.FT_CLAUSE_FRAGMENT;
import static com.tastreet.EventBus.Events.FT_CS_FRAGMENT;
import static com.tastreet.EventBus.Events.FT_FESTIVAL_DETAIL_FRAGMENT;
import static com.tastreet.EventBus.Events.FT_FESTIVAL_FRAGMENT;
import static com.tastreet.EventBus.Events.FT_MAIN_FRAGMENT;
import static com.tastreet.EventBus.Events.FT_MONTHLY_FESTIVAL_DETAIL_FRAGMENT;
import static com.tastreet.EventBus.Events.MYFT_FRAGMENT;

public class FT_MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    ActionBar actionBar;

    public static FT_FestivalFragment FTFestivalFragment = FT_FestivalFragment.getInstance();
    public static FT_MainFragment ft_mainFragment = FT_MainFragment.getInstance();

    DrawerLayout drawerLayout;
    NavigationView navigationView;

    FrameLayout main_panel;

    int reqCode = 9991;

    String mainImgPath = "";
    String menuImgPath = "";


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
        TextView header_id = navigationView.getHeaderView(0).findViewById(R.id._id);
        header_id.setText(FT_LoginActivity.loginData.getFt_name());
        ImageView profile_img = navigationView.getHeaderView(0).findViewById(R.id.profile_img);
        Glide.with(this).load(FT_LoginActivity.loginData.getFt_main_img()).apply(new RequestOptions().circleCrop().error(R.drawable.ic_launcher_foreground)).into(profile_img);


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                switch(item.getItemId()){
                    case R.id.navigation_item_my_foodtruck:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        setMyFoodtruckFragment();
                        break;

                    case R.id.navigation_item_festival:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        setFestivalFragment();
                        break;

                    case R.id.navigation_item_settings: //설정 아니고 About으로 변경됐음
                        drawerLayout.closeDrawer(GravityCompat.START);
                        setAboutFragment();
                        break;

                    case R.id.navigation_item_logout:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        SharedPref sharedPref = new SharedPref(FT_MainActivity.this);
                        sharedPref.setAutoLogin(false);
                        sharedPref.setSaveId(false);
                        sharedPref.setLoginId("");
                        sharedPref.setLoginPw("");
                        finish();
                        break;
                }
                return true;
            }
        });

        setMainFragment();
    }


    public String getPath(Context context, Uri uri) {
        String filePath = "";
        String fileId = DocumentsContract.getDocumentId(uri);
        // Split at colon, use second item in the array
        String id = fileId.split(":")[1];
        String[] column = {MediaStore.Images.Media.DATA};
        String selector = MediaStore.Images.Media._ID + "=?";
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                column, selector, new String[]{id}, null);
        int columnIndex = cursor.getColumnIndex(column[0]);
        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }
        cursor.close();
        return filePath;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                final Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
//                galleryIntent.setType("*/*");
//                startActivityForResult(galleryIntent, 1);
            } else {
                Toast.makeText(FT_MainActivity.this, "권한을 허가하지 않아 이미지를 불러올 수 없습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(FT_MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(FT_MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Toast.makeText(FT_MainActivity.this, "이미지를 불러오기 위해 권한이 필요합니다.", Toast.LENGTH_SHORT).show();
        } else {
            ActivityCompat.requestPermissions(FT_MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Uri selectedMainImgUri = data.getData();
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(selectedMainImgUri, filePathColumn, null, null, null);
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        Log.d("requestCode", requestCode + "");
        Log.d("getPath", getPath(this, selectedMainImgUri));
        if (requestCode == 3 && resultCode == RESULT_OK) {
            reqCode = 3;
            if (cursor.moveToFirst()) {
                mainImgPath = getPath(this, selectedMainImgUri);
//                Events.ImageFileSelected imageFileSelected = new Events.ImageFileSelected(getPath(this, selectedMainImgUri), requestCode);
//                GlobalBus.getBus().post(imageFileSelected);
                cursor.close();
            }
        } else if (requestCode == 4 && resultCode == RESULT_OK) {
            reqCode = 4;
            if (cursor.moveToFirst()) {
                menuImgPath = getPath(this, selectedMainImgUri);
//                Events.ImageFileSelected imageFileSelected = new Events.ImageFileSelected(getPath(this, selectedMainImgUri), requestCode);
//                GlobalBus.getBus().post(imageFileSelected);
                cursor.close();
            }
        } else {
            Toast.makeText(FT_MainActivity.this, "파일 로드 실패", Toast.LENGTH_SHORT).show();
        }
    }

    private void setMyFoodtruckFragment(){
        MyFT_Fragment myFT_fragment = new MyFT_Fragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", FT_LoginActivity.loginData);
        myFT_fragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction().replace(R.id.main_panel, myFT_fragment);
        fragmentTransaction.commit();
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

    private void setAboutFragment(){
        FT_AboutFragment ft_aboutFragment = new FT_AboutFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction().replace(R.id.main_panel, ft_aboutFragment);
        fragmentTransaction.commit();
    }

    private void setClauseFragment(){
        FT_ClauseFragment ft_clauseFragment = new FT_ClauseFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction().replace(R.id.main_panel, ft_clauseFragment);
        fragmentTransaction.commit();
    }

    private void setChargeRageFragment(){
        FT_ChargeRateFragment ft_chargeRateFragment = new FT_ChargeRateFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction().replace(R.id.main_panel, ft_chargeRateFragment);
        fragmentTransaction.commit();
    }

    private void setCsFragment(){
        FT_CsFragment ft_csFragment = new FT_CsFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction().replace(R.id.main_panel, ft_csFragment);
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
        } else if("3".equals(msg.getMsg())){
            if (Build.VERSION.SDK_INT >= 23) {
                if (checkPermission()) {
                    final Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    galleryIntent.setType("*/*");
                    startActivityForResult(galleryIntent, 3);
                } else {
                    requestPermission();
                }
            } else {
                final Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("*/*");
                startActivityForResult(galleryIntent, 3);
            }
        } else if("4".equals(msg.getMsg())){
            if (Build.VERSION.SDK_INT >= 23) {
                if (checkPermission()) {
                    final Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    galleryIntent.setType("*/*");
                    startActivityForResult(galleryIntent, 4);
                } else {
                    requestPermission();
                }
            } else {
                final Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("*/*");
                startActivityForResult(galleryIntent, 4);
            }
        } else if("onResume".equals(msg.getMsg())){
            if(reqCode == 3){
                Events.ImageFileSelected mainImg = new Events.ImageFileSelected(mainImgPath, 3);
                GlobalBus.getBus().post(mainImg);
            } else if(reqCode == 4){
                Events.ImageFileSelected menuImg = new Events.ImageFileSelected(menuImgPath, 4);
                GlobalBus.getBus().post(menuImg);
            }
        } else if(Events.FT_CLAUSE_FRAGMENT.equals(msg.getMsg())){
            setClauseFragment();
        } else if(Events.FT_CHARGE_RATE_FRAGMENT.equals(msg.getMsg())){
            setChargeRageFragment();
        } else if(Events.FT_CS_FRAGMENT.equals(msg.getMsg())){
            setCsFragment();
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
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(Gravity.START);
        } else {
            if (CURRENT_PAGE.equals(FT_MAIN_FRAGMENT)) {
                finish();
            } else if (CURRENT_PAGE.equals(FT_MONTHLY_FESTIVAL_DETAIL_FRAGMENT)) {
                setMainFragment();
            } else if (CURRENT_PAGE.equals(FT_FESTIVAL_DETAIL_FRAGMENT)) {
                setFestivalFragment();
            } else if (CURRENT_PAGE.equals(FT_FESTIVAL_FRAGMENT)) {
                setMainFragment();
            } else if(CURRENT_PAGE.equals(MYFT_FRAGMENT)){
                setMainFragment();
                TextView header_id = navigationView.getHeaderView(0).findViewById(R.id._id);
                header_id.setText(FT_LoginActivity.loginData.getFt_name());
                ImageView profile_img = navigationView.getHeaderView(0).findViewById(R.id.profile_img);
                Glide.with(this).load(FT_LoginActivity.loginData.getFt_main_img()).apply(new RequestOptions().circleCrop().error(R.drawable.ic_launcher_foreground)).into(profile_img);
            } else if(CURRENT_PAGE.equals(FT_ABOUT_FRAGMENT)){
                setMainFragment();
            } else if(CURRENT_PAGE.equals(FT_CLAUSE_FRAGMENT) ||
                    CURRENT_PAGE.equals(FT_CHARGE_RATE_FRAGMENT) ||
                    CURRENT_PAGE.equals(FT_CS_FRAGMENT)){
                setAboutFragment();
            }
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
