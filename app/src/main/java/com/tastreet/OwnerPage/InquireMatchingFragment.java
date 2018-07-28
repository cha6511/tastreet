package com.tastreet.OwnerPage;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.tastreet.EventBus.Events;
import com.tastreet.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class InquireMatchingFragment extends Fragment {

    TextView next;

    public static InquireMatchingFragment inquireMatchingFragment;
    public static InquireMatchingFragment getInstance(){
        if(inquireMatchingFragment == null){
            inquireMatchingFragment = new InquireMatchingFragment();
        }
        return inquireMatchingFragment;
    }

    public InquireMatchingFragment() {
        // Required empty public constructor
    }

    View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_inquire_matching, container, false);
        // Inflate the layout for this fragment
        next = v.findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InquireDoneDialog dialog = new InquireDoneDialog(getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        Events.CURRENT_PAGE = Events.INQUIRY_MATCHING_FRAGMENT;
        return v;
    }

}
