package com.tastreet.FoodTruckPage;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.tastreet.AsyncDone;
import com.tastreet.EventBus.Events;
import com.tastreet.EventBus.GlobalBus;
import com.tastreet.Festival.FestivalListAdapter;
import com.tastreet.Festival.FestivalListData;
import com.tastreet.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.tastreet.EventBus.Events.CURRENT_PAGE;
import static com.tastreet.EventBus.Events.FT_FESTIVAL_FRAGMENT;

/**
 * A simple {@link Fragment} subclass.
 */
public class FT_FestivalFragment extends Fragment  implements View.OnClickListener{

    public static FT_FestivalFragment FTFestivalFragment;
    public static FT_FestivalFragment getInstance(){
        if(FTFestivalFragment == null){
            FTFestivalFragment = new FT_FestivalFragment();
        }
        return FTFestivalFragment;
    }

    public FT_FestivalFragment() {
        // Required empty public constructor
    }

    RecyclerView festival_list;
    FestivalListAdapter adapter;
    ArrayList<FestivalListData> datas = new ArrayList<>();
    LinearLayoutManager linearLayoutManager;

    View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_festival, container, false);

        GetFestivalData getFestivalData = new GetFestivalData(getContext(), new AsyncDone() {
            @Override
            public void getResult(String result) {
                if("FAILED".equals(result)){
                    Toast.makeText(getContext(), "축제 목록을 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
                } else{
                    festival_list = v.findViewById(R.id.festival_list);
                    adapter = new FestivalListAdapter(datas, FT_FestivalFragment.this);
                    linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                    festival_list.setAdapter(adapter);
                    festival_list.setLayoutManager(linearLayoutManager);
                    adapter.notifyDataSetChanged();
                }
            }
        });
        getFestivalData.execute();
        CURRENT_PAGE = FT_FESTIVAL_FRAGMENT;
        return v;
    }

    @Override
    public void onClick(View view) {
        Events.SendFestivalData sendFestivalData = new Events.SendFestivalData( ((FestivalListData)view.getTag()) );
        GlobalBus.getBus().post(sendFestivalData);
    }



    public class GetFestivalData extends AsyncTask<String, String, String>{

        Context context;
        ProgressDialog progressDialog;
        AsyncDone asyncDone;

        public GetFestivalData(Context context, AsyncDone asyncDone){
            this.context = context;
            this.asyncDone = asyncDone;
            this.progressDialog = new ProgressDialog(context);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... strings) {
            OkHttpClient client = new OkHttpClient();
            Request.Builder builder = new Request.Builder();
            builder.url(Events.baseUrl + "get_festivals.php");
            Request request = builder.build();
            Response response = null;
            try{
                response = client.newCall(request).execute();
                String myResponse = response.body().string();
                return myResponse;
            } catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(TextUtils.isEmpty(s)){
                if(progressDialog.isShowing())
                    progressDialog.dismiss();
                asyncDone.getResult("FAILED");
            } else{
                if(progressDialog.isShowing())
                    progressDialog.dismiss();
                //값 받아와서 처리함
                try{
                    datas.clear();

                    JSONObject resultObj = new JSONObject(s);
                    JSONArray festivalsArray = resultObj.getJSONArray("festival");
                    ArrayList<FestivalListData> tmp = new ArrayList<>();
                    for(int i = 0 ; i < festivalsArray.length() ; i++){
                        JSONObject festivalObj = festivalsArray.getJSONObject(i);
                        FestivalListData data = new FestivalListData(
                                festivalObj.getString("fs_name"),
                                festivalObj.getString("fs_amt"),
                                festivalObj.getString("fs_status"),
                                festivalObj.getString("fs_img"),
                                festivalObj.getString("fs_add"),
                                festivalObj.getString("hp_add"),
                                festivalObj.getString("fs_num"),
                                festivalObj.getString("fs_desc"),
                                festivalObj.getString("fs_date")
                        );
                        tmp.add(data);
                    }
                    datas.addAll(tmp);
                } catch (Exception e){
                    e.printStackTrace();
                }
                asyncDone.getResult("SUCCESS");
            }
        }
    }
}
