package com.tastreet.FoodTruckPage;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tastreet.EventBus.Events;
import com.tastreet.EventBus.GlobalBus;
import com.tastreet.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.tastreet.EventBus.Events.CURRENT_PAGE;

/**
 * A simple {@link Fragment} subclass.
 */
public class FT_AboutFragment extends Fragment {


    @BindView(R.id.clause)
    TextView clause;
    @BindView(R.id.charge_rate)
    TextView chargeRate;
    @BindView(R.id.cs)
    TextView cs;
    Unbinder unbinder;


    public FT_AboutFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ft__about, container, false);
        unbinder = ButterKnife.bind(this, view);
        CURRENT_PAGE = Events.FT_ABOUT_FRAGMENT;
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @OnClick({R.id.clause, R.id.charge_rate, R.id.cs})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.clause:
                Events.Msg clause = new Events.Msg(Events.FT_CLAUSE_FRAGMENT);
                GlobalBus.getBus().post(clause);
                break;
            case R.id.charge_rate:
                Events.Msg chargeRate = new Events.Msg(Events.FT_CHARGE_RATE_FRAGMENT);
                GlobalBus.getBus().post(chargeRate);
                break;
            case R.id.cs:
                Events.Msg cs = new Events.Msg(Events.FT_CS_FRAGMENT);
                GlobalBus.getBus().post(cs);
                break;
        }
    }
}
