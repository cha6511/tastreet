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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import java.util.HashMap;
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

    EditText contact;
    EditText facebook;
    EditText instagram;

    Button register;

    File main_img;

    ByteArrayOutputStream byteArrayOutputStream;
    byte[] byteArray;
    String ConvertImage;
    HttpURLConnection httpURLConnection;
    URL url;
    OutputStream outputStream;
    BufferedWriter bufferedWriter;
    int RC;
    BufferedReader bufferedReader;
    StringBuilder stringBuilder;
    boolean check = true;


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
//                Glide.with(FT_RegisterActivity.this)
//                        .load(new File(menuImagePath))
//                        .into(profile_img);
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
                registerFT.execute(
                        mainImagePath,
                        ft_name.getText().toString(),
                        "한국",
                        contact.getText().toString(),
                        _id.getText().toString(),
                        pw.getText().toString(),
                        description.getText().toString(),
                        menuImagePath,
                        facebook.getText().toString(),
                        instagram.getText().toString(),
                        "음료"
                );
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
                    JSONArray resultArray = new JSONArray(s);
                    JSONObject resultObj = resultArray.getJSONObject(0);
                    String result = resultObj.getString("result");
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

        public Request setPostBody(String ft_main_img, String ft_name, String origin, String ft_num, String ft_id, String ft_pw, String ft_intro, String ft_menu_img, String ft_sns_f, String ft_sns_i) {
            try {
                final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/*");
                File mainFile = new File(ft_main_img);
                String main_filename = ft_main_img.substring(ft_main_img.lastIndexOf("/") + 1);

                File menuFile = new File(ft_menu_img);
                String menu_filename = ft_menu_img.substring(ft_menu_img.lastIndexOf("/") + 1);

                RequestBody req = new MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart("ft_main_img", main_filename, RequestBody.create(MEDIA_TYPE_PNG, mainFile))
                        .addFormDataPart("ft_name", ft_name)
                        .addFormDataPart("origin", origin)
                        .addFormDataPart("ft_num", ft_num)
                        .addFormDataPart("ft_id", ft_id)
                        .addFormDataPart("ft_pw", ft_pw)
                        .addFormDataPart("ft_intro", ft_intro)
                        .addFormDataPart("ft_menu_img", menu_filename, RequestBody.create(MEDIA_TYPE_PNG, menuFile))
                        .addFormDataPart("ft_sns_f", ft_sns_f)
                        .addFormDataPart("ft_sns_i", ft_sns_i)
                        .build();

                Request request = new Request.Builder()
                        .url(Events.baseUrl + "register.php")
                        .post(req)
                        .build();
                return request;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }


        }
    }


    public void UploadImageToServer(Bitmap bitmap) {
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byteArray = byteArrayOutputStream.toByteArray();
        ConvertImage = Base64.encodeToString(byteArray, Base64.DEFAULT);

        class AsyncTaskUploadClass extends AsyncTask<Void, Void, String> {

            @Override
            protected String doInBackground(Void... voids) {
                ImageProcessClass imageProcessClass = new ImageProcessClass();
                HashMap<String, String> HashMapParams = new HashMap<String, String>();
                HashMapParams.put("ft_main_img", "profile");
                HashMapParams.put("test.png", ConvertImage);
                String FinalData = imageProcessClass.ImageHttpRequest(Events.baseUrl + "register.php", HashMapParams);

                return FinalData;
            }
        }

        AsyncTaskUploadClass AsyncTaskUploadClassOBJ = new AsyncTaskUploadClass();
        AsyncTaskUploadClassOBJ.execute();
    }

    public class ImageProcessClass {
        public String ImageHttpRequest(String requestURL, HashMap<String, String> PData) {
            StringBuilder stringBuilder = new StringBuilder();
            try {
                url = new URL(requestURL);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setReadTimeout(20000);
                httpURLConnection.setConnectTimeout(20000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                outputStream = httpURLConnection.getOutputStream();
                bufferedWriter = new BufferedWriter(
                        new OutputStreamWriter(outputStream, "UTF-8"));
                bufferedWriter.write(bufferedWriterDataFN(PData));
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                RC = httpURLConnection.getResponseCode();
                if (RC == httpURLConnection.HTTP_OK) {
                    bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                    stringBuilder = new StringBuilder();
                    String RC2;
                    while ((RC2 = bufferedReader.readLine()) != null) {
                        stringBuilder.append(RC2);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return stringBuilder.toString();
        }

        private String bufferedWriterDataFN(HashMap<String, String> HashMapParams) throws UnsupportedEncodingException {
            stringBuilder = new StringBuilder();
            for (Map.Entry<String, String> KEY : HashMapParams.entrySet()) {
                if (check)
                    check = false;
                else
                    stringBuilder.append("&amp;");
                stringBuilder.append(URLEncoder.encode(KEY.getKey(), "UTF-8"));
                stringBuilder.append("=");
                stringBuilder.append(URLEncoder.encode(KEY.getValue(), "UTF-8"));
            }
            return stringBuilder.toString();
        }

    }
}
