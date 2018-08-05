package com.tastreet.FoodTruckPage;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.tastreet.EventBus.Events;
import com.tastreet.EventBus.GlobalBus;
import com.tastreet.OwnerPage.FoodListData;
import com.tastreet.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.tastreet.EventBus.Events.CURRENT_PAGE;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyFT_Fragment extends Fragment {


    @BindView(R.id._id)
    TextView Id;
    @BindView(R.id.profile_img)
    ImageView profileImg;
    @BindView(R.id.ft_name)
    EditText ftName;
    @BindView(R.id.description)
    EditText description;
    @BindView(R.id.add_picture)
    Button addPicture;
    @BindView(R.id.menu_img)
    ImageView menuImg;
    @BindView(R.id.origin)
    EditText origin;
    @BindView(R.id.category)
    Spinner category;
    @BindView(R.id.menu_img_view)
    LinearLayout menuImgView;
    @BindView(R.id.contact)
    EditText contact;
    @BindView(R.id.facebook)
    EditText facebook;
    @BindView(R.id.instagram)
    EditText instagram;
    @BindView(R.id.register)
    Button register;

    CategorySpinnerAdapter adapter;

    boolean editorMode = false;

    Unbinder unbinder;

    public MyFT_Fragment() {
        // Required empty public constructor
    }

    View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_my_ft_, container, false);
        // Inflate the layout for this fragment


        //      _id.setText();    서버에서 받아온 아이디 입력

        unbinder = ButterKnife.bind(this, v);
        if (getArguments().isEmpty()) {
            Toast.makeText(getContext(), "데이터가 없습니다.", Toast.LENGTH_SHORT).show();
            Events.Msg msg = new Events.Msg(Events.BACK_BUTTON_PRESS);
            GlobalBus.getBus().post(msg);
        }

        FoodListData data = (FoodListData) getArguments().getSerializable("data");

        Id.setText(data.getFt_name() + " 님");
        Glide.with(getContext()).load(data.getFt_main_img()).apply(new RequestOptions().error(R.drawable.ic_launcher_foreground)).into(profileImg);
        profileImg.setClickable(false);

        ftName.setText(data.getFt_name());
        ftName.setEnabled(false);

        description.setText(data.getFt_intro());
        description.setEnabled(false);

        addPicture.setClickable(false);
        Glide.with(getContext()).load(data.getFt_menu_img()).apply(new RequestOptions().error(R.drawable.ic_launcher_foreground)).into(menuImg);
        menuImg.setClickable(false);

        origin.setText(data.getOrigin());
        origin.setEnabled(false);

        List<String> adapterData = new ArrayList<>();
        adapterData.add("식사류");
        adapterData.add("간식류");
        adapterData.add("디저트");
        adapterData.add("음료");
        adapter = new CategorySpinnerAdapter(getContext(), adapterData);
        category.setAdapter(adapter);
        int adapterPos = 0;
        for (int i = 0; i < adapterData.size(); i++) {
            if (data.getCategory().equals(adapterData.get(i))) {
                adapterPos = i;
            }
        }
        category.setSelection(adapterPos);
        category.setEnabled(false);

        contact.setText(data.getFt_num());
        contact.setEnabled(false);

        facebook.setText(data.getFt_sns_f());
        facebook.setEnabled(false);

        instagram.setText(data.getFt_sns_i());
        instagram.setEnabled(false);


        CURRENT_PAGE = Events.MYFT_FRAGMENT;
        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.profile_img, R.id.add_picture, R.id.register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.profile_img:
                break;
            case R.id.add_picture:
                break;
            case R.id.register:
                if (editorMode) {
                    //변경내용으로 update
                    UpdateUserInfo updateUserInfo = new UpdateUserInfo();
                    updateUserInfo.execute();

                } else {
                    editorMode = !editorMode;

                    profileImg.setClickable(true);
                    ftName.setEnabled(true);
                    description.setEnabled(true);
                    addPicture.setClickable(true);
                    menuImg.setClickable(true);
                    origin.setEnabled(true);
                    category.setEnabled(true);
                    contact.setEnabled(true);
                    facebook.setEnabled(true);
                    instagram.setEnabled(true);
                    register.setText("등록");

                }
                break;
        }
    }


    private class UpdateUserInfo extends AsyncTask<String, String ,String>{

        @Override
        protected String doInBackground(String... strings) {
            return null;
        }
    }
}
