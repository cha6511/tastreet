package com.tastreet.FoodTruckPage;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.tastreet.AsyncDone;
import com.tastreet.EventBus.Events;
import com.tastreet.EventBus.GlobalBus;
import com.tastreet.OwnerPage.FoodListData;
import com.tastreet.R;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.tastreet.EventBus.Events.CURRENT_PAGE;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyFT_Fragment extends Fragment {


    @BindView(R.id._id)
    TextView Id;
    @BindView(R.id.profile_img)
    ImageView profileImg;
    @BindView(R.id.ft_name)
    EditText ftName;
    @BindView(R.id.description)
    EditText description;
    @BindView(R.id.add_picture)
    Button addPicture;
    @BindView(R.id.menu_img)
    ImageView menuImg;
    @BindView(R.id.origin)
    EditText origin;
    @BindView(R.id.category)
    Spinner category;
    @BindView(R.id.menu_img_view)
    LinearLayout menuImgView;
    @BindView(R.id.contact)
    EditText contact;
    @BindView(R.id.facebook)
    EditText facebook;
    @BindView(R.id.instagram)
    EditText instagram;
    @BindView(R.id.register)
    Button register;

    CategorySpinnerAdapter adapter;
    String categoryString;

    boolean editorMode = false;

    Unbinder unbinder;

    String mainImagePath = "";
    String menuImagePath = "";

    public MyFT_Fragment() {
        // Required empty public constructor
    }

    View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_my_ft_, container, false);
        // Inflate the layout for this fragment


        //      _id.setText();    서버에서 받아온 아이디 입력

        unbinder = ButterKnife.bind(this, v);
        if (getArguments().isEmpty()) {
            Toast.makeText(getContext(), "데이터가 없습니다.", Toast.LENGTH_SHORT).show();
            Events.Msg msg = new Events.Msg(Events.BACK_BUTTON_PRESS);
            GlobalBus.getBus().post(msg);
        }

        FoodListData data = (FoodListData) getArguments().getSerializable("data");

        Id.setText(data.getFt_name() + " 님");
        Glide.with(getContext()).load(data.getFt_main_img()).apply(new RequestOptions().error(R.drawable.ic_launcher_foreground)).into(profileImg);
        profileImg.setClickable(false);

        ftName.setText(data.getFt_name());
        ftName.setEnabled(false);

        description.setText(data.getFt_intro());
        description.setEnabled(false);

        addPicture.setClickable(false);
        Glide.with(getContext()).load(data.getFt_menu_img()).apply(new RequestOptions().error(R.drawable.ic_launcher_foreground)).into(menuImg);
        menuImg.setClickable(false);

        origin.setText(data.getOrigin());
        origin.setEnabled(false);

        List<String> adapterData = new ArrayList<>();
        adapterData.add("식사류");
        adapterData.add("간식류");
        adapterData.add("디저트");
        adapterData.add("음료");
        adapter = new CategorySpinnerAdapter(getContext(), adapterData);
        category.setAdapter(adapter);
        int adapterPos = 0;
        for (int i = 0; i < adapterData.size(); i++) {
            if (data.getCategory().equals(adapterData.get(i))) {
                adapterPos = i;
            }
        }
        category.setSelection(adapterPos);
        categoryString = adapterData.get(adapterPos);
        category.setEnabled(false);
        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                categoryString = String.valueOf(adapterView.getItemAtPosition(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        contact.setText(data.getFt_num());
        contact.setEnabled(false);

        facebook.setText(data.getFt_sns_f());
        facebook.setEnabled(false);

        instagram.setText(data.getFt_sns_i());
        instagram.setEnabled(false);


        CURRENT_PAGE = Events.MYFT_FRAGMENT;
        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Toast.makeText(getContext(), "이미지를 불러오기 위해 권한이 필요합니다.", Toast.LENGTH_SHORT).show();
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
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
                Toast.makeText(getContext(), "권한을 허가하지 않아 이미지를 불러올 수 없습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @OnClick({R.id.profile_img, R.id.add_picture, R.id.register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.profile_img:
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
                break;
            case R.id.add_picture:
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
                break;
            case R.id.register:
                if (editorMode) {
                    //변경내용으로 update
                    UpdateUserInfo updateUserInfo = new UpdateUserInfo(getContext(), new AsyncDone() {
                        @Override
                        public void getResult(String result) {

                        }
                    });
                    updateUserInfo.execute(
                            mainImagePath,
                            ftName.getText().toString(),
                            origin.getText().toString(),
                            contact.getText().toString(),
                            description.getText().toString(),
                            menuImagePath,
                            facebook.getText().toString(),
                            instagram.getText().toString(),
                            categoryString
                    );

                } else {
                    editorMode = !editorMode;

                    profileImg.setClickable(true);
                    ftName.setEnabled(true);
                    description.setEnabled(true);
                    addPicture.setClickable(true);
                    menuImg.setClickable(true);
                    origin.setEnabled(true);
                    category.setEnabled(true);
                    contact.setEnabled(true);
                    facebook.setEnabled(true);
                    instagram.setEnabled(true);
                    register.setText("등록");
                }
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        GlobalBus.getBus().getDefault().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        GlobalBus.getBus().getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getImagePath(Events.ImageFileSelected imageFileSelected){
        switch(imageFileSelected.getRequestCode()){
            case 3:
                mainImagePath = imageFileSelected.getImg_path();
                Glide.with(getContext())
                        .load(new File(mainImagePath))
                        .into(profileImg);
                break;

            case 4:
                menuImagePath = imageFileSelected.getImg_path();
                Glide.with(getContext())
                        .load(new File(mainImagePath))
                        .into(profileImg);
                break;
        }
    }


    public class UpdateUserInfo extends AsyncTask<String, String, String> {

        Context context;
        AsyncDone asyncDone;
        ProgressDialog progressDialog;

        public UpdateUserInfo(Context context, AsyncDone asyncDone) {
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

                File menuFile = new File(strings[5]);
                String menu_filename = strings[5].substring(strings[5].lastIndexOf("/") + 1);

                RequestBody req = new MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart("ft_main_img", main_filename, RequestBody.create(MEDIA_TYPE_PNG, mainFile))
                        .addFormDataPart("ft_name", strings[1])
                        .addFormDataPart("origin", strings[2])
                        .addFormDataPart("ft_num", strings[3])
                        .addFormDataPart("ft_intro", strings[4])
                        .addFormDataPart("ft_menu_img", menu_filename, RequestBody.create(MEDIA_TYPE_PNG, menuFile))
                        .addFormDataPart("ft_sns_f", strings[6])
                        .addFormDataPart("ft_sns_i", strings[7])
                        .addFormDataPart("category", strings[8])
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
