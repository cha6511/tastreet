package com.tastreet.FoodTruckPage;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.tastreet.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyFT_Fragment extends Fragment {

    TextView _id;
    ImageView profile_img;
    EditText ft_name;
    EditText description;
    Button add_picture;
    EditText contact;
    EditText facebook;
    EditText instagram;

    TextView register;

    public MyFT_Fragment() {
        // Required empty public constructor
    }

    View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_my_ft_, container, false);
        // Inflate the layout for this fragment
        _id = v.findViewById(R.id._id);
        profile_img = v.findViewById(R.id.profile_img);
        ft_name = v.findViewById(R.id.ft_name);
        description = v.findViewById(R.id.description);
        add_picture = v.findViewById(R.id.add_picture);
        contact = v.findViewById(R.id.contact);
        facebook = v.findViewById(R.id.facebook);
        instagram = v.findViewById(R.id.instagram);

        register = v.findViewById(R.id.register);


        //      _id.setText();    서버에서 받아온 아이디 입력

        profile_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //사진첩 열어서 profile_img에 넣기
            }
        });

        add_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //사진첩 열어서 배열에 넣기
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //위 내용 다 서버로 보내기
            }
        });

        return v;
    }

}
