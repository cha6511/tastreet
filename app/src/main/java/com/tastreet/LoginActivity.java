package com.tastreet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.tastreet.FoodTruckPage.FT_LoginActivity;
import com.tastreet.OwnerPage.OwnerMainActivity;

import java.security.acl.Owner;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    TextView owner;
    TextView ft;

    long backKeyPressedTime = 0;
    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        if (System.currentTimeMillis() > backKeyPressedTime + 3000) {
            backKeyPressedTime = System.currentTimeMillis();
//                showGuide(); return;
            Toast.makeText(getApplicationContext(), "\'뒤로\'버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
            return;
        } else if (System.currentTimeMillis() <= backKeyPressedTime + 3000) {
            finish();
//                toast.cancel();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        owner = findViewById(R.id.owner);
        ft = findViewById(R.id.ft);

        owner.setOnClickListener(this);
        ft.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.owner:
                //축제 주최자 메인 페이지로 이동
                Intent owner = new Intent(LoginActivity.this, OwnerMainActivity.class);
                startActivity(owner);
//                finish();
                break;

            case R.id.ft:
                //푸드트럭 메인 페이지로 이동
                Intent intent = new Intent(LoginActivity.this, FT_LoginActivity.class);
                startActivity(intent);
//                finish();
                break;
        }
    }

}
