package com.tastreet.FoodTruckPage;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.RequestBuilder;
import com.tastreet.AsyncDone;
import com.tastreet.BaseActivity;
import com.tastreet.Common;
import com.tastreet.EventBus.Events;
import com.tastreet.R;
import com.tastreet.SharedPref;

import org.json.JSONArray;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FT_LoginActivity extends BaseActivity {

    SharedPref sharedPref;

    @BindView(R.id.input_id)
    EditText inputId;
    @BindView(R.id.input_pw)
    EditText inputPw;
    @BindView(R.id.save_id)
    CheckBox saveId;
    @BindView(R.id.auto_login)
    CheckBox autoLogin;
    @BindView(R.id.login_btn)
    TextView loginBtn;
    @BindView(R.id.register)
    LinearLayout register;
    @BindView(R.id.find_id)
    LinearLayout findId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ft__login);
        ButterKnife.bind(this);

        //오토로그인 버튼 체크 리스너
        autoLogin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                sharedPref.setAutoLogin(b);
            }
        });
        //아이디 저장 버튼 체크 리스너
        saveId.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                sharedPref.setSaveId(b);
            }
        });

        //sharedPreference에서 오토로그인, 아이디저장 체크값 불러와서 세팅
        sharedPref = new SharedPref(this);
        if (sharedPref.getAutoLogin()) { //오토로그인 체크했던경우 : 자동로그인, 아이디저장 다 체크된 상태로 이전에 입력했던 id, pw 불러와서 세팅
            inputId.setText(sharedPref.getLoginId());
            inputPw.setText(sharedPref.getLoginPw());
            autoLogin.setChecked(true);
            saveId.setChecked(true);
        } else if (sharedPref.getSaveId()) { //아이디 저장 체크했던 경우 : 아이디 저장만 체크된 상태로 이전에 입력했던 id만 불러와서 세팅
            inputId.setText(sharedPref.getLoginId());
            autoLogin.setChecked(false);
            saveId.setChecked(true);
        }

        //위 로직에서 오토로그인 체크되어있으면 로그인버튼 강제 클릭
//        if (autoLogin.isChecked()) {
//            loginBtn.performClick();
//        }
    }

    @OnClick({R.id.login_btn, R.id.register, R.id.find_id})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.login_btn:
                //로그인 프로세스
                if (TextUtils.isEmpty(inputId.getText().toString())) { //아이디 입력 안했으면
                    Toast.makeText(FT_LoginActivity.this, "아이디를 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    if (TextUtils.isEmpty(inputPw.getText().toString())) { //비번 입력 안했으면
                        Toast.makeText(FT_LoginActivity.this, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                        return;
                    } else { //둘다 입력한 경우 서버에 값을 보내서 결과값에 따라 처리
                        //서버 갔다오기
                        LoginProcess loginProcess = new LoginProcess(FT_LoginActivity.this, new AsyncDone() {
                            @Override
                            public void getResult(String result) {
                                if (Common.FAILED.equals(result)) { //결과값이 FAILED 인 경우
                                    Toast.makeText(FT_LoginActivity.this, "로그인 실패\n아이디와 비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show();
                                } else if (Common.SUCCESS.equals(result)) { //결과값이 SUCCESS 인 경우
                                    //페이지 이동
                                    Intent intent = new Intent(FT_LoginActivity.this, FT_MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });
                        loginProcess.execute(inputId.getText().toString(), inputPw.getText().toString());
                    }
                }
                break;
            case R.id.register:
                Intent intent = new Intent(FT_LoginActivity.this, FT_RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.find_id:
                break;
        }
    }


    public class LoginProcess extends AsyncTask<String, String, String> {

        Context context;
        AsyncDone asyncDone;

        public LoginProcess(Context context, AsyncDone asyncDone) {
            this.context = context;
            this.asyncDone = asyncDone;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("처리중입니다...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setCancelable(false);
            progressDialog.show();

        }

        @Override
        protected String doInBackground(String... strings) {

            OkHttpClient client = new OkHttpClient();
            RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("id", strings[0])
                    .addFormDataPart("pw", strings[1])
                    .build();

            Request request = new Request.Builder().url(Events.baseUrl + "ft_login.php").post(requestBody).build();
            Response response = null;
            String myResponse = "";
            try{
                response = client.newCall(request).execute();
                myResponse = response.body().string();
                return myResponse;
            } catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("Login result", s);
            try {
                JSONArray resultArray = new JSONArray(s);
                JSONObject jsonObject = resultArray.getJSONObject(0);
                String result = jsonObject.getString("result");
                if (Common.FAILED.equals(result)) {
                    asyncDone.getResult(result);
                } else if (Common.SUCCESS.equals(result)) {
                    //정보 다 받아와서 저장
                    if(autoLogin.isChecked()){
                        SharedPref sharedPref = new SharedPref(context);
                        sharedPref.setLoginId(inputId.getText().toString());
                        sharedPref.setLoginPw(inputPw.getText().toString());
                        sharedPref.setAutoLogin(true);
                    }
                    asyncDone.getResult(result);
                }
            } catch (Exception e) {
                e.printStackTrace();
                asyncDone.getResult(Common.FAILED);
            }

            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    }
}
