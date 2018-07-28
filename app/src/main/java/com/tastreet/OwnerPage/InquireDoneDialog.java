package com.tastreet.OwnerPage;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.tastreet.EventBus.Events;
import com.tastreet.EventBus.GlobalBus;
import com.tastreet.R;

public class InquireDoneDialog extends Dialog {
    Context context;
    TextView confirm;
    public InquireDoneDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inquire_done_dialog);
        confirm = findViewById(R.id.confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InquireDoneDialog.this.dismiss();
                Events.Msg msg = new Events.Msg(Events.FINISH_MATCHING);
                GlobalBus.getBus().post(msg);
            }
        });
    }
}
