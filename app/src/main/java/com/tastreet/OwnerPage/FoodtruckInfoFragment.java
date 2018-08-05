package com.tastreet.OwnerPage;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.tastreet.EventBus.Events;
import com.tastreet.EventBus.GlobalBus;
import com.tastreet.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class FoodtruckInfoFragment extends Fragment {


    @BindView(R.id.main_img)
    ImageView mainImg;
    @BindView(R.id.ft_name)
    TextView ftName;
    @BindView(R.id.description)
    TextView description;
    @BindView(R.id.menu_img)
    ImageView menuImg;
    @BindView(R.id.origin)
    TextView origin;
    @BindView(R.id.contact)
    TextView contact;
    @BindView(R.id.ft_sns_f)
    TextView ftSnsF;
    @BindView(R.id.ft_sns_i)
    TextView ftSnsI;
    @BindView(R.id.back)
    Button back;
    Unbinder unbinder;


    public FoodtruckInfoFragment() {
        // Required empty public constructor
    }

    View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_foodtruck_info, container, false);
        unbinder = ButterKnife.bind(this, v);
        if( getArguments().isEmpty()){
            Toast.makeText(getContext(), "데이터가 없습니다.", Toast.LENGTH_SHORT).show();
            Events.Msg msg = new Events.Msg(Events.DIRECT_MATCHING_FRAGMENT);
            GlobalBus.getBus().post(msg);
        }

        FoodListData data = (FoodListData) getArguments().getSerializable("data");

        ftName.setText("푸드트럭명 : " + data.getFt_name());
        description.setText("한줄소개 : " + data.getFt_intro());

        origin.setText("원산지 : " + data.getOrigin());
        contact.setText("연락처 : " + data.getFt_num());
        ftSnsF.setText("페이스북 : " + data.getFt_sns_f());
        ftSnsI.setText("인스타그램 : " + data.getFt_sns_i());



        Glide.with(getContext()).load(data.getFt_main_img())
                .apply(new RequestOptions().error(getContext().getResources().getDrawable(R.drawable.ic_launcher_foreground)))
                .into(mainImg);
        Glide.with(getContext()).load(data.getFt_menu_img())
                .apply(new RequestOptions().error(getContext().getResources().getDrawable(R.drawable.ic_launcher_foreground)))
                .into(menuImg);


        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.contact, R.id.ft_sns_f, R.id.ft_sns_i, R.id.back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.contact:
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel",
                        contact.getText().toString().split(" : ")[1], null));
                startActivity(intent);
                break;
            case R.id.ft_sns_f:
                try {
                    Intent f = new Intent(Intent.ACTION_VIEW);
                    f.setData(Uri.parse(ftSnsF.getText().toString().split(" : ")[1]));
                    startActivity(f);
                } catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(getContext(), "유효하지 않은 url입니다.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.ft_sns_i:
                try {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(ftSnsI.getText().toString().split(" : ")[1]));
                    startActivity(i);
                } catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(getContext(), "유효하지 않은 url입니다.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.back:
                Events.Msg msg = new Events.Msg(Events.DIRECT_MATCHING_FRAGMENT);
                GlobalBus.getBus().post(msg);
                break;
        }
    }
}
