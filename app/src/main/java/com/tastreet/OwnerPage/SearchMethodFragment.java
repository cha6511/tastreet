package com.tastreet.OwnerPage;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.tastreet.EventBus.Events;
import com.tastreet.EventBus.GlobalBus;
import com.tastreet.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchMethodFragment extends Fragment {

    public static SearchMethodFragment searchMethodFragment;

    public static SearchMethodFragment getInstance() {
        if (searchMethodFragment == null) {
            searchMethodFragment = new SearchMethodFragment();
            return searchMethodFragment;
        }
        return searchMethodFragment;
    }

    public SearchMethodFragment() {
        // Required empty public constructor
    }

    View v;

    RadioButton direct;
    RadioButton inquire;

    Button next;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_search_method, container, false);
        next = v.findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(direct.isChecked()) {
                    Events.Msg msg = new Events.Msg(Events.DIRECT_MATCHING_FRAGMENT);
                    GlobalBus.getBus().post(msg);
                } else if(inquire.isChecked()){
                    Events.Msg msg = new Events.Msg(Events.INQUIRY_MATCHING_FRAGMENT);
                    GlobalBus.getBus().post(msg);
                } else{
                    Toast.makeText(getContext(), "매칭 방법을 선택해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        direct = v.findViewById(R.id.direct_matching);
        inquire = v.findViewById(R.id.inquire);

        direct.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                inquire.setChecked(!b);
            }
        });

        inquire.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                direct.setChecked(!b);
            }
        });


        Events.CURRENT_PAGE = Events.SEARCH_METHOD_FRAGMENT;
        return v;
    }

}
