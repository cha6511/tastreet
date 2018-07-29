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

    Bitmap main;
    Bitmap menu;

    String mainImageTag = "ft_main_img";
    String menuImageTag = "ft_menu_img";

    String mainImageData = "main_image_data";
    String menuImageData = "menu_image_data";
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Uri selectedMainImgUri = data.getData();
            main_img = new File(getPath(selectedMainImgUri));
            try {
                main = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedMainImgUri);
                UploadImageToServer(main);
            } catch (Exception e) {
                e.printStackTrace();
            }
            getPath(selectedMainImgUri);
            Glide.with(FT_RegisterActivity.this).load(main).into(profile_img);
            no_profile.setVisibility(View.GONE);
        }
    }

    public String getPath(Uri uri) {
        if (uri.getPath().startsWith("/storage")) {
//            main = BitmapFactory.decodeFile(uri.getPath());
            return uri.getPath();
        }

        String id = DocumentsContract.getDocumentId(uri).split(":")[1];
        String[] columns = {MediaStore.Images.Media.DATA};
        String selection = MediaStore.Images.ImageColumns._ID + "=" + id;
        Cursor cursor = getContentResolver().query(
                MediaStore.Images.Media.getContentUri("external"),
                columns,
                selection,
                null,
                null);
        try {
            int columnIndex = cursor.getColumnIndex(columns[0]);
            if (cursor.moveToFirst()) {
//                main = BitmapFactory.decodeFile(cursor.getString(columnIndex));
                return cursor.getString(columnIndex);
            }
        } finally {
            cursor.close();
        }
        return null;

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
                UploadImageToServer(main);
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
                registerFT.execute(registerFT.setPostBody(
                        main_img,
                        ft_name.getText().toString(),
                        "",
                        contact.getText().toString(),
                        _id.getText().toString(),
                        pw.getText().toString(),
                        description.getText().toString(),
                        main_img,
                        facebook.getText().toString(),
                        instagram.getText().toString()
                ));
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

    public class RegisterFT extends AsyncTask<Request, String, String> {

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
        protected String doInBackground(Request... reqs) {
            OkHttpClient client = new OkHttpClient();

            try {
                Response response = client.newCall(reqs[0]).execute();
                String myResponse = response.body().string();
                return myResponse;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (TextUtils.isEmpty(s)) {
                asyncDone.getResult("FAILED");
            } else {
                asyncDone.getResult("SUCCESS");
            }
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }

        public Request setPostBody(File ft_main_img, String ft_name, String origin, String ft_num, String ft_id, String ft_pw, String ft_intro, File ft_menu_img, String ft_sns_f, String ft_sns_i) {
            try {
                final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");

                RequestBody req = new MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart("ft_main_img", "profile.png", RequestBody.create(MEDIA_TYPE_PNG, ft_main_img))
                        .addFormDataPart("ft_name", ft_name)
                        .addFormDataPart("origin", origin)
                        .addFormDataPart("ft_num", ft_num)
                        .addFormDataPart("ft_id", ft_id)
                        .addFormDataPart("ft_pw", ft_pw)
                        .addFormDataPart("ft_intro", ft_intro)
                        .addFormDataPart("ft_menu_img", "menu_img.png", RequestBody.create(MEDIA_TYPE_PNG, ft_menu_img))
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
