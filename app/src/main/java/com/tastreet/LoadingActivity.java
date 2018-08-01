package com.tastreet;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class LoadingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        Handler handler = new Handler();
        //postDelayed 를 사용하여 n초 후에 실행될 액션을 설정해준다.
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // <2>    로그인(축제 주최자 또는 푸드트럭 선택) 페이지로 이동한다.
                Intent intent = new Intent(LoadingActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
            // <1>    1.5초 후에
        }, 1500);
    }
}
