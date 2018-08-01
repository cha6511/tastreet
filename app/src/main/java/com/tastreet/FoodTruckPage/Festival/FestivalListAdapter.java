package com.tastreet.FoodTruckPage.Festival;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tastreet.R;

import java.util.ArrayList;

public class FestivalListAdapter extends RecyclerView.Adapter<FestivalListAdapter.Holder>{

    ArrayList<FestivalListData> datas = new ArrayList<>();
    View.OnClickListener onClickListener;

    public FestivalListAdapter(ArrayList<FestivalListData> datas, View.OnClickListener onClickListener) {
        this.datas = datas;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.festival_list_item, parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        FestivalListData data = datas.get(position);
        holder.status.setText(data.getRecruit_status());
        holder.recruit_amt.setText(data.getRecruit_amt());
        holder.festival_name.setText(data.getFestival_name());
        holder.festival_date.setText("일시 : " + data.getFestival_date());
        holder.festival_loc.setText("위치 : " + data.getFestival_loc());

        holder.body.setOnClickListener(onClickListener);
        holder.body.setTag(data);
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView status;
        TextView recruit_amt;
        TextView festival_name;
        TextView festival_date;
        TextView festival_loc;

        LinearLayout body;
        public Holder(View itemView) {
            super(itemView);
            status = itemView.findViewById(R.id.status);
            recruit_amt = itemView.findViewById(R.id.recruit_amt);
            festival_name = itemView.findViewById(R.id.festival_name);
            festival_date = itemView.findViewById(R.id.festival_date);
            festival_loc = itemView.findViewById(R.id.festival_loc);

            body = itemView.findViewById(R.id.body);
        }
    }
}
