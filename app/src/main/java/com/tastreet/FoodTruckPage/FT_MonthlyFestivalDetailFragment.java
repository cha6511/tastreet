package com.tastreet.FoodTruckPage;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tastreet.EventBus.Events;
import com.tastreet.EventBus.GlobalBus;
import com.tastreet.MonthlyFestival.MonthlyFestivalListData;
import com.tastreet.R;

import static com.tastreet.EventBus.Events.CURRENT_PAGE;
import static com.tastreet.EventBus.Events.FT_MONTHLY_FESTIVAL_DETAIL_FRAGMENT;

/**
 * A simple {@link Fragment} subclass.
 */
public class FT_MonthlyFestivalDetailFragment extends Fragment {

    public static FT_MonthlyFestivalDetailFragment ft_monthlyFestivalDetailFragment;
    public static FT_MonthlyFestivalDetailFragment getInstance(){
        if(ft_monthlyFestivalDetailFragment == null){
            ft_monthlyFestivalDetailFragment = new FT_MonthlyFestivalDetailFragment();
            return ft_monthlyFestivalDetailFragment;
        } return ft_monthlyFestivalDetailFragment;
    }
    public FT_MonthlyFestivalDetailFragment() {
        // Required empty public constructor
    }
    ImageView img;
    TextView festival_detail;
    TextView festival_desc;

    Button back;

    View v;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_ft__monthly_festival_detail, container, false);

        img = v.findViewById(R.id.img);
        festival_detail = v.findViewById(R.id.festival_detail);
        festival_desc = v.findViewById(R.id.festival_desc);

        if(! getArguments().isEmpty()){
            MonthlyFestivalListData data = (MonthlyFestivalListData) getArguments().getSerializable("monthlyDetailData");
            Glide.with(getContext()).load(data.getMfs_img()).into(img);
            festival_detail.setText(
                    "주소 : " + data.getMfs_addr() + "\n" +
                    "홈페이지 : " + data.getMfs_homeaddr() + "\n" +
                    "전화번호 : " + data.getMfs_num()
            );

            festival_desc.setText(data.getMfs_etc());
        }

        back = v.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Events.Msg msg = new Events.Msg(Events.BACK_BUTTON_PRESS);
                GlobalBus.getBus().post(msg);
            }
        });
        // Inflate the layout for this fragment
        CURRENT_PAGE = FT_MONTHLY_FESTIVAL_DETAIL_FRAGMENT;
        return v;
    }
}
