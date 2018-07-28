package com.tastreet.OwnerPage;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.tastreet.EventBus.Events;
import com.tastreet.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class DirectMatchingFragment extends Fragment {

    public static DirectMatchingFragment directMatchingFragment;
    public static DirectMatchingFragment getInstance(){
        if(directMatchingFragment == null){
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

    TabLayout tabs;

    View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_direct_matching, container, false);
        // Inflate the layout for this fragment
        tabs = v.findViewById(R.id.tabs);
        food_list = v.findViewById(R.id.food_list);
        gridLayoutManager = new GridLayoutManager(getContext(), 2);
        adapter = new FoodListAdapter(getContext(), datas, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //데이터베이스에서 해당 상점 목록 가져오기
            }
        });
        food_list.setAdapter(adapter);
        food_list.setLayoutManager(gridLayoutManager);

        tabs.addTab(tabs.newTab().setText("식사류"));
        tabs.addTab(tabs.newTab().setText("간식류"));
        tabs.addTab(tabs.newTab().setText("디저트"));
        tabs.addTab(tabs.newTab().setText("음료"));

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch(tab.getPosition()){
                    case 0:
                        Toast.makeText(getContext(), tab.getText(), Toast.LENGTH_SHORT).show();
                        break;

                    case 1:
                        Toast.makeText(getContext(), tab.getText(), Toast.LENGTH_SHORT).show();
                        break;

                    case 2:
                        Toast.makeText(getContext(), tab.getText(), Toast.LENGTH_SHORT).show();
                        break;

                    case 3:
                        Toast.makeText(getContext(), tab.getText(), Toast.LENGTH_SHORT).show();
                        break;
                }
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

}
