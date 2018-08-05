package com.tastreet.OwnerPage;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.tastreet.AsyncDone;
import com.tastreet.Common;
import com.tastreet.EventBus.Events;
import com.tastreet.EventBus.GlobalBus;
import com.tastreet.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class DirectMatchingFragment extends Fragment implements View.OnClickListener {

    public static DirectMatchingFragment directMatchingFragment;

    public static DirectMatchingFragment getInstance() {
        if (directMatchingFragment == null) {
            directMatchingFragment = new DirectMatchingFragment();
        }
        return directMatchingFragment;
    }

    public DirectMatchingFragment() {
        // Required empty public constructor
    }

    RecyclerView food_list;
    GridLayoutManager gridLayoutManager;
    FoodListAdapter adapter;

    ArrayList<FoodListData> datas = new ArrayList<>();
    ArrayList<FoodListData> selectedArray = new ArrayList<>();

    TabLayout tabs;

    View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_direct_matching, container, false);
        // Inflate the layout for this fragment
        tabs = v.findViewById(R.id.tabs);
        food_list = v.findViewById(R.id.food_list);


        tabs.addTab(tabs.newTab().setText("식사류"));
        tabs.addTab(tabs.newTab().setText("간식류"));
        tabs.addTab(tabs.newTab().setText("디저트"));
        tabs.addTab(tabs.newTab().setText("음료"));

        adapter = new FoodListAdapter(getContext(), selectedArray, DirectMatchingFragment.this);
        gridLayoutManager = new GridLayoutManager(getContext(), 2);
        food_list.setAdapter(adapter);
        food_list.setLayoutManager(gridLayoutManager);

        final GetFTs getFTs = new GetFTs(getContext(), new AsyncDone() {
            @Override
            public void getResult(String result) {
                if (Common.SUCCESS.equals(result)) {
                    selectedArray.clear();
                    for (int i = 0; i < datas.size(); i++) {
                        if (datas.get(i).getCategory().equals("식사류")) {
                            selectedArray.add(datas.get(i));
                        }
                    }
                    adapter = new FoodListAdapter(getContext(), selectedArray, DirectMatchingFragment.this);
                    gridLayoutManager = new GridLayoutManager(getContext(), 2);
                    food_list.setAdapter(adapter);
                    food_list.setLayoutManager(gridLayoutManager);
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), "푸드트럭 목록을 불러오는데 실패하였습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        getFTs.execute();


        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                selectedArray.clear();
                for (int i = 0; i < datas.size(); i++) {
                    if (datas.get(i).getCategory().equals(tab.getText().toString())) {
                        selectedArray.add(datas.get(i));
                    }
                }
                adapter = new FoodListAdapter(getContext(), selectedArray, DirectMatchingFragment.this);
                gridLayoutManager = new GridLayoutManager(getContext(), 2);
                food_list.setAdapter(adapter);
                food_list.setLayoutManager(gridLayoutManager);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        Events.CURRENT_PAGE = Events.DIRECT_MATCHING_FRAGMENT;
        return v;
    }

    @Override
    public void onClick(View view) {
        FoodListData data = ((FoodListData)view.getTag());
        Events.SendFoodtruckInfoData sendFoodtruckInfoData = new Events.SendFoodtruckInfoData(data);
        GlobalBus.getBus().post(sendFoodtruckInfoData);
    }


    public class GetFTs extends AsyncTask<String, String, String> {

        Context context;
        ProgressDialog progressDialog;
        AsyncDone asyncDone;

        public GetFTs(Context context, AsyncDone asyncDone) {
            this.context = context;
            this.asyncDone = asyncDone;
            this.progressDialog = new ProgressDialog(context);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("정보를 불러오는중...");
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            OkHttpClient client = new OkHttpClient();
            Request.Builder builder = new Request.Builder();
            builder.url(Events.baseUrl + "get_foodtrucks.php");
            Request request = builder.build();
            Response response = null;
            try {
                response = client.newCall(request).execute();
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
            if (TextUtils.isEmpty(s)) {
                asyncDone.getResult(Common.FAILED);
            } else {
                datas.clear();
                try {
                    JSONArray resultArray = new JSONArray(s);
                    ArrayList<FoodListData> tmp = new ArrayList<>();
                    for (int i = 0; i < resultArray.length(); i++) {
                        JSONObject obj = resultArray.getJSONObject(i);

                        tmp.add(new FoodListData(
                                obj.getString("ft_main_img").replace("/var/www/html", "http://52.78.25.74"),
                                obj.getString("ft_name"),
                                obj.getString("origin"),
                                obj.getString("ft_num"),
                                obj.getString("ft_intro"),
                                obj.getString("ft_menu_img").replace("/var/www/html", "http://52.78.25.74"),
                                obj.getString("ft_sns_f"),
                                obj.getString("ft_sns_i"),
                                obj.getString("category")
                        ));
                    }
                    datas.addAll(tmp);
                    asyncDone.getResult(Common.SUCCESS);
                } catch (Exception e) {
                    e.printStackTrace();
                    asyncDone.getResult(Common.FAILED);
                }
            }
            if (progressDialog.isShowing())
                progressDialog.dismiss();
        }
    }

}
