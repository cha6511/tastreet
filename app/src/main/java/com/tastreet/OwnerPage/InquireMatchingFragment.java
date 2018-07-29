package com.tastreet.OwnerPage;


import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tastreet.AsyncDone;
import com.tastreet.EventBus.Events;
import com.tastreet.R;

import org.json.JSONArray;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class InquireMatchingFragment extends Fragment {

    TextView next;

    public static InquireMatchingFragment inquireMatchingFragment;


    Unbinder unbinder;
    @BindView(R.id.festival_name)
    EditText festivalName;
    @BindView(R.id.festival_date)
    EditText festivalDate;
    @BindView(R.id.festival_loc)
    EditText festivalLoc;
    @BindView(R.id.expect_amt)
    EditText expectAmt;
    @BindView(R.id.deadline)
    EditText deadline;
    @BindView(R.id.e_support)
    EditText eSupport;
    @BindView(R.id.m_num)
    EditText mNum;
    @BindView(R.id.etc)
    EditText etc;
    @BindView(R.id.back)
    Button back;

    public static InquireMatchingFragment getInstance() {
        if (inquireMatchingFragment == null) {
            inquireMatchingFragment = new InquireMatchingFragment();
        }
        return inquireMatchingFragment;
    }

    public InquireMatchingFragment() {
        // Required empty public constructor
    }

    View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_inquire_matching, container, false);

        Events.CURRENT_PAGE = Events.INQUIRY_MATCHING_FRAGMENT;
        unbinder = ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.back)
    public void onViewClicked() {
        RegisterInquireMatching registerInquireMatching = new RegisterInquireMatching(getContext(), new AsyncDone() {
            @Override
            public void getResult(String result) {
                if ("SUCCESS".equals(result)) {
                    InquireDoneDialog dialog = new InquireDoneDialog(getContext());
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();
                } else{
                    Toast.makeText(getContext(), "문의 등록에 실패했습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        registerInquireMatching.execute(
                festivalName.getText().toString(),
                festivalDate.getText().toString(),
                festivalLoc.getText().toString(),
                expectAmt.getText().toString(),
                deadline.getText().toString(),
                eSupport.getText().toString(),
                etc.getText().toString(),
                mNum.getText().toString()
        );
    }


    public class RegisterInquireMatching extends AsyncTask<String, String, String> {

        Context context;
        AsyncDone asyncDone;
        ProgressDialog progressDialog;

        public RegisterInquireMatching(Context context, AsyncDone asyncDone) {
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
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            OkHttpClient client = new OkHttpClient();
            RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("fs_name", strings[0])
                    .addFormDataPart("fs_time", strings[1])
                    .addFormDataPart("fs_place", strings[2])
                    .addFormDataPart("capacity", strings[3])
                    .addFormDataPart("deadline", strings[4])
                    .addFormDataPart("e_ox", strings[5])
                    .addFormDataPart("etc", strings[6])
                    .addFormDataPart("m_num", strings[7])
                    .build();

            Request request = new Request.Builder().url(Events.baseUrl + "register_inquire.php").post(requestBody).build();
            Response response = null;
            String myResponse = "";
            try {
                response = client.newCall(request).execute();
                myResponse = response.body().string();
                return myResponse;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("register inquire result", s);
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
                    asyncDone.getResult("FAILED");
                }
            }
            if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }
        }
    }
}
