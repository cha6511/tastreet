package com.tastreet.FoodTruckPage;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.tastreet.R;

public class FT_RegisterActivity extends AppCompatActivity {

    TextView no_profile;
    ImageView profile_img;

    EditText _id;
    EditText pw;
    EditText ft_name;
    EditText description;

    Button add_picture;

    EditText contact;
    EditText facebook;
    EditText instagram;

    Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ft__register);

        no_profile = findViewById(R.id.no_profile);
        profile_img = findViewById(R.id.profile_img);

        _id= findViewById(R.id.ft_id);
        pw = findViewById(R.id.ft_pw);
        ft_name = findViewById(R.id.ft_name);
        description = findViewById(R.id.description);

        add_picture = findViewById(R.id.add_picture);

        contact = findViewById(R.id.contact);
        facebook = findViewById(R.id.facebook);
        instagram = findViewById(R.id.instagram);

        register = findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
