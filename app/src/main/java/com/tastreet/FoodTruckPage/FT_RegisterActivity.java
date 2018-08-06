package com.tastreet.FoodTruckPage;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.tastreet.AsyncDone;
import com.tastreet.EventBus.Events;
import com.tastreet.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FT_RegisterActivity extends AppCompatActivity {

    TextView no_profile;
    ImageView profile_img;

    EditText _id;
    EditText pw;
    EditText ft_name;
    EditText description;

    Button add_picture;
    Spinner categorySpinner;
    CategorySpinnerAdapter adapter;
    ImageView menu_img;
    String category = "";
    LinearLayout menu_img_view;

    EditText origin;

    EditText contact;
    EditText facebook;
    EditText instagram;

    Button register;

    String mainImagePath = "";
    String menuImagePath = "";



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Uri selectedMainImgUri = data.getData();
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(selectedMainImgUri, filePathColumn, null, null, null);
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            if (cursor.moveToFirst()) {
                mainImagePath = getPath(this, selectedMainImgUri);
                Log.d("mainImagePath", mainImagePath);
                Glide.with(FT_RegisterActivity.this)
                        .load(new File(mainImagePath))
                        .into(profile_img);
                no_profile.setVisibility(View.GONE);
                cursor.close();
            }
        } else if (requestCode == 2 && resultCode == RESULT_OK) {
            if (cursor.moveToFirst()) {
                menuImagePath = getPath(this, selectedMainImgUri);
                Log.d("menuImagePath", menuImagePath);
                Glide.with(FT_RegisterActivity.this)
                        .load(new File(menuImagePath))
                        .into(menu_img);
                menu_img_view.setVisibility(View.VISIBLE);
                add_picture.setText("사진 변경");
                cursor.close();
            }
        } else {
            Toast.makeText(FT_RegisterActivity.this, "파일 로드 실패", Toast.LENGTH_SHORT).show();
        }
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ft__register);

        no_profile = findViewById(R.id.no_profile);
        profile_img = findViewById(R.id.profile_img);
        no_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= 23) {
                    if (checkPermission()) {
                        final Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                        galleryIntent.setType("*/*");
                        startActivityForResult(galleryIntent, 1);
                    } else {
                        requestPermission();
                    }
                } else {
                    final Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    galleryIntent.setType("*/*");
                    startActivityForResult(galleryIntent, 1);
                }

            }
        });

        _id = findViewById(R.id.ft_id);
        pw = findViewById(R.id.ft_pw);
        ft_name = findViewById(R.id.ft_name);
        description = findViewById(R.id.description);

        menu_img = findViewById(R.id.menu_img);
        categorySpinner = findViewById(R.id.category);
        List<String> data = new ArrayList<>();
        data.add("식사류"); data.add("간식류"); data.add("디저트");  data.add("음료");
        adapter = new CategorySpinnerAdapter(this, data);
        categorySpinner.setAdapter(adapter);
        category = "식사류";
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                category = String.valueOf(adapterView.getItemAtPosition(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        menu_img_view = findViewById(R.id.menu_img_view);
        origin = findViewById(R.id.origin);

        add_picture = findViewById(R.id.add_picture);
        add_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= 23) {
                    if (checkPermission()) {
                        final Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                        galleryIntent.setType("*/*");
                        startActivityForResult(galleryIntent, 2);
                    } else {
                        requestPermission();
                    }
                } else {
                    final Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    galleryIntent.setType("*/*");
                    startActivityForResult(galleryIntent, 2);
                }
            }
        });

        contact = findViewById(R.id.contact);
        facebook = findViewById(R.id.facebook);
        instagram = findViewById(R.id.instagram);

        register = findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegisterFT registerFT = new RegisterFT(FT_RegisterActivity.this, new AsyncDone() {
                    @Override
                    public void getResult(String result) {
                        if ("SUCCESS".equals(result)) {
                            Toast.makeText(FT_RegisterActivity.this, "회원 등록에 성공하였습니다.\n등록한 아이디로 로그인해주세요.", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(FT_RegisterActivity.this, "회원 등록에 실패하였습니다.\n관리자에게 문의해주세요.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                if(TextUtils.isEmpty(mainImagePath)){
                    Toast.makeText(FT_RegisterActivity.this, "메인 사진을 선택해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    if (TextUtils.isEmpty(_id.getText().toString())) {
                        Toast.makeText(FT_RegisterActivity.this, "아이디를 입력해주세요.", Toast.LENGTH_SHORT).show();
                    } else {
                        if (TextUtils.isEmpty(pw.getText().toString())) {
                            Toast.makeText(FT_RegisterActivity.this, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                        } else {
                            if (TextUtils.isEmpty(ft_name.getText().toString())) {
                                Toast.makeText(FT_RegisterActivity.this, "푸드트럭명을 입력해주세요.", Toast.LENGTH_SHORT).show();
                            } else {
                                if (TextUtils.isEmpty(description.getText().toString())) {
                                    Toast.makeText(FT_RegisterActivity.this, "한 줄 소개를 입력해주세요.", Toast.LENGTH_SHORT).show();
                                } else {
                                    if (TextUtils.isEmpty(menuImagePath)) {
                                        Toast.makeText(FT_RegisterActivity.this, "메뉴 사진을 선택해주세요.", Toast.LENGTH_SHORT).show();
                                    } else{
                                        if(TextUtils.isEmpty(category)){
                                            Toast.makeText(FT_RegisterActivity.this, "카테고리를 선택해주세요.", Toast.LENGTH_SHORT).show();
                                        } else{
                                            if(TextUtils.isEmpty(origin.getText().toString())){
                                                Toast.makeText(FT_RegisterActivity.this, "원산지를 입력해주세요.", Toast.LENGTH_SHORT).show();
                                            } else{
                                                if(TextUtils.isEmpty(contact.getText().toString())){
                                                    Toast.makeText(FT_RegisterActivity.this, "연락처를 입력해주세요.", Toast.LENGTH_SHORT).show();
                                                } else{
                                                    registerFT.execute(
                                                            mainImagePath,
                                                            ft_name.getText().toString(),
                                                            origin.getText().toString(),
                                                            contact.getText().toString(),
                                                            _id.getText().toString(),
                                                            pw.getText().toString(),
                                                            description.getText().toString(),
                                                            menuImagePath,
                                                            facebook.getText().toString(),
                                                            instagram.getText().toString(),
                                                            category
                                                    );
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        });
    }


    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(FT_RegisterActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(FT_RegisterActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Toast.makeText(FT_RegisterActivity.this, "이미지를 불러오기 위해 권한이 필요합니다.", Toast.LENGTH_SHORT).show();
        } else {
            ActivityCompat.requestPermissions(FT_RegisterActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                final Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("*/*");
                startActivityForResult(galleryIntent, 1);
            } else {
                Toast.makeText(FT_RegisterActivity.this, "권한을 허가하지 않아 이미지를 불러올 수 없습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class RegisterFT extends AsyncTask<String, String, String> {

        Context context;
        AsyncDone asyncDone;
        ProgressDialog progressDialog;

        public RegisterFT(Context context, AsyncDone asyncDone) {
            this.context = context;
            this.asyncDone = asyncDone;
            progressDialog = new ProgressDialog(context);
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

            try {
                final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/*");
                File mainFile = new File(strings[0]);
                String main_filename = strings[0].substring(strings[0].lastIndexOf("/") + 1);

                File menuFile = new File(strings[7]);
                String menu_filename = strings[7].substring(strings[7].lastIndexOf("/") + 1);

                Log.d("register ft_main_img", main_filename);
                Log.d("register ft_name", strings[1]);
                Log.d("register origin", strings[2]);
                Log.d("register ft_num", strings[3]);
                Log.d("register ft_id", strings[4]);
                Log.d("register ft_pw", strings[5]);
                Log.d("register ft_intro", strings[6]);
                Log.d("register ft_menu_img", menu_filename);
                Log.d("register ft_sns_f", strings[8]);
                Log.d("register ft_sns_i", strings[9]);
                Log.d("register category", strings[10]);

                RequestBody req = new MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart("ft_main_img", main_filename, RequestBody.create(MEDIA_TYPE_PNG, mainFile))
                        .addFormDataPart("ft_name", strings[1])
                        .addFormDataPart("origin", strings[2])
                        .addFormDataPart("ft_num", strings[3])
                        .addFormDataPart("ft_id", strings[4])
                        .addFormDataPart("ft_pw", strings[5])
                        .addFormDataPart("ft_intro", strings[6])
                        .addFormDataPart("ft_menu_img", menu_filename, RequestBody.create(MEDIA_TYPE_PNG, menuFile))
                        .addFormDataPart("ft_sns_f", strings[8])
                        .addFormDataPart("ft_sns_i", strings[9])
                        .addFormDataPart("category", strings[10])
                        .build();

                Request.Builder builder = new Request.Builder();
                builder.post(req).url(Events.baseUrl + "register.php");
                Request request = builder.build();

                Response response = client.newCall(request).execute();
                String myResponse = response.body().string();
                Log.d("register response body", myResponse);
                return myResponse;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("register response", s);
            if (TextUtils.isEmpty(s)) {
                asyncDone.getResult("FAILED");
            } else {

                try {
                    JSONObject res = new JSONObject(s);
                    String result = res.getString("result");
                    if ("SUCCESS".equals(result)) {
                        asyncDone.getResult("SUCCESS");
                    } else {
                        asyncDone.getResult("FAILED");
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
