package com.tastreet.FoodTruckPage;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tastreet.EventBus.Events;
import com.tastreet.R;

import static com.tastreet.EventBus.Events.CURRENT_PAGE;

/**
 * A simple {@link Fragment} subclass.
 */
public class FT_CsFragment extends Fragment {


    public FT_CsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        CURRENT_PAGE = Events.FT_CS_FRAGMENT;
        return inflater.inflate(R.layout.fragment_ft__cs, container, false);
    }

}
