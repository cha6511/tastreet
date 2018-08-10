package com.tastreet.FoodTruckPage;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tastreet.AsyncDone;
import com.tastreet.EventBus.Events;
import com.tastreet.R;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FindPasswordDialog extends Dialog {
    public FindPasswordDialog(@NonNull Context context) {
        super(context);
    }

    EditText input_id;
    TextView find_pw;
    TextView confirm;
    TextView close;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_password_dialog);
        input_id = findViewById(R.id.input_id);
        find_pw = findViewById(R.id.find_pw);
        confirm = findViewById(R.id.confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(input_id.getText().toString())) {
                    Toast.makeText(getContext(), "아이디를 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                FindPassword findPassword = new FindPassword(getContext(), new AsyncDone() {
                    @Override
                    public void getResult(String result) {

                    }
                });
                findPassword.execute(input_id.getText().toString());
            }
        });

        close = findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FindPasswordDialog.this.dismiss();
            }
        });
    }


    private class FindPassword extends AsyncTask<String, String, String> {

        Context context;
        AsyncDone asyncDone;
        ProgressDialog progressDialog;

        public FindPassword(Context context, AsyncDone asyncDone) {
            this.context = context;
            this.asyncDone = asyncDone;
            this.progressDialog = new ProgressDialog(context);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("등록중입니다...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setCancelable(false);
            if (context != null) {
                progressDialog.show();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            OkHttpClient client = new OkHttpClient();
            Log.d("ft_id", strings[0]);
            RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("ft_id", strings[0]).build();

            Request.Builder builder = new Request.Builder();
            builder.post(body).url(Events.baseUrl + "get_password.php");
            Request request = builder.build();
            try {
                Response response = client.newCall(request).execute();
                String myResponse = response.body().string();
                return myResponse;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("get pw response", s);
            if (TextUtils.isEmpty(s)) {
                asyncDone.getResult("FAILED");
            } else {

                try {
                    JSONArray resultArray = new JSONArray(s);
                    JSONObject res = resultArray.getJSONObject(0);
                    String result = res.getString("result");
                    if ("SUCCESS".equals(result)) {
                        asyncDone.getResult("SUCCESS");
                        find_pw.setText("비밀번호는 " + res.getString("ft_pw") + " 입니다.");
                    } else {
                        asyncDone.getResult("FAILED");
                        find_pw.setText("아이디를 확인해주세요.");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    asyncDone.getResult("FAILED");
                }


            }
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    }
}
