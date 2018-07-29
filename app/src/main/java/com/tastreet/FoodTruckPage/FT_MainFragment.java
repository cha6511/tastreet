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

import com.tastreet.AsyncDone;
import com.tastreet.EventBus.Events;
import com.tastreet.EventBus.GlobalBus;
import com.tastreet.MonthlyFestival.MonthlyFestivalListAdapter;
import com.tastreet.MonthlyFestival.MonthlyFestivalListData;
import com.tastreet.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.tastreet.EventBus.Events.CURRENT_PAGE;
import static com.tastreet.EventBus.Events.FT_MAIN_FRAGMENT;

/**
 * A simple {@link Fragment} subclass.
 */
public class FT_MainFragment extends Fragment implements View.OnClickListener{


    public static FT_MainFragment ft_mainFragment;
    public static FT_MainFragment getInstance(){
        if(ft_mainFragment == null){
            ft_mainFragment = new FT_MainFragment();
            return ft_mainFragment;
        } return ft_mainFragment;
    }

    public FT_MainFragment() {
        // Required empty public constructor
    }

    RecyclerView mfs_list;
    MonthlyFestivalListAdapter adapter;
    ArrayList<MonthlyFestivalListData> datas = new ArrayList<>();
    LinearLayoutManager linearLayoutManager;


    View v;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_ft__main, container, false);


        GetMonthlyFestival getMonthlyFestival = new GetMonthlyFestival(getContext(), new AsyncDone() {
            @Override
            public void getResult(String result) {
                mfs_list = v.findViewById(R.id.mfs_list);
                adapter = new MonthlyFestivalListAdapter(getContext(), datas, FT_MainFragment.this);
                linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                mfs_list.setAdapter(adapter);
                mfs_list.setLayoutManager(linearLayoutManager);
                adapter.notifyDataSetChanged();
            }
        });
        getMonthlyFestival.execute();



        CURRENT_PAGE = FT_MAIN_FRAGMENT;
        // Inflate the layout for this fragment
        return v;
    }

    @Override
    public void onClick(View view) {
        Events.SendMonthlyFestivalData sendMonthlyFestivalData = new Events.SendMonthlyFestivalData( ((MonthlyFestivalListData)view.getTag() ));
        GlobalBus.getBus().post(sendMonthlyFestivalData);
    }








    public class GetMonthlyFestival extends AsyncTask<String, String, String>{
        Context context;
        ProgressDialog progressDialog;
        AsyncDone asyncDone;

        public GetMonthlyFestival(Context context, AsyncDone asyncDone) {
            this.context = context;
            this.asyncDone = asyncDone;
            this.progressDialog = new ProgressDialog(context);
        }



        @Override
        protected String doInBackground(String... strings) {
            OkHttpClient client = new OkHttpClient();
            Request.Builder builder = new Request.Builder();
            builder.url(Events.baseUrl + "get_monthly_festival.php");
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
                    JSONArray festivalsArray = resultObj.getJSONArray("monthly_festival");
                    ArrayList<MonthlyFestivalListData> tmp = new ArrayList<>();
                    for(int i = 0 ; i < festivalsArray.length() ; i++){
                        JSONObject festivalObj = festivalsArray.getJSONObject(i);
                        MonthlyFestivalListData data = new MonthlyFestivalListData(
                                festivalObj.getString("mfs_name"),
                                festivalObj.getString("mfs_img"),
                                festivalObj.getString("mfs_addr"),
                                festivalObj.getString("mfs_term"),
                                festivalObj.getString("mfs_homeaddr"),
                                festivalObj.getString("mfs_num"),
                                festivalObj.getString("mfs_etc")
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
