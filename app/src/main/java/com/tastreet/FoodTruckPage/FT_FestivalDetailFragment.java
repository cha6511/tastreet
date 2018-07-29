package com.tastreet.FoodTruckPage;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.tastreet.EventBus.Events;
import com.tastreet.EventBus.GlobalBus;
import com.tastreet.Festival.FestivalListData;
import com.tastreet.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.tastreet.EventBus.Events.CURRENT_PAGE;
import static com.tastreet.EventBus.Events.FT_FESTIVAL_DETAIL_FRAGMENT;

/**
 * A simple {@link Fragment} subclass.
 */
public class FT_FestivalDetailFragment extends Fragment {


    @BindView(R.id.status)
    TextView status;
    @BindView(R.id.recruit_amt)
    TextView recruitAmt;
    @BindView(R.id.festival_name)
    TextView festivalName;
    @BindView(R.id.festival_date)
    TextView festivalDate;
    @BindView(R.id.festival_loc)
    TextView festivalLoc;
    @BindView(R.id.expect_amt)
    TextView expectAmt;
    @BindView(R.id.deadline)
    TextView deadline;
    @BindView(R.id.etc)
    TextView etc;
    Unbinder unbinder;
    @BindView(R.id.back)
    Button back;

    public FT_FestivalDetailFragment() {
        // Required empty public constructor
    }

    View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_ft__festival_detail, container, false);
        // Inflate the layout for this fragment
        unbinder = ButterKnife.bind(this, v);

        if (!getArguments().isEmpty()) {
            FestivalListData data = (FestivalListData) getArguments().getSerializable("festivalDetailData");

            status.setText(data.getRecruit_status());
            recruitAmt.setText(data.getRecruit_amt());
            festivalName.setText(data.getFestival_name());
            festivalDate.setText("일시 : " + data.getFestival_date());
            festivalLoc.setText("위치 : " + data.getFestival_loc());

        }
        CURRENT_PAGE = FT_FESTIVAL_DETAIL_FRAGMENT;
        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.back)
    public void onViewClicked() {
        Events.Msg msg = new Events.Msg(Events.BACK_BUTTON_PRESS);
        GlobalBus.getBus().post(msg);
    }
}
