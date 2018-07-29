package com.tastreet.MonthlyFestival;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tastreet.R;

import java.util.ArrayList;

public class MonthlyFestivalListAdapter extends RecyclerView.Adapter<MonthlyFestivalListAdapter.Holder> {
    ArrayList<MonthlyFestivalListData> datas = new ArrayList<>();
    View.OnClickListener onClickListener;
    Context context;

    public MonthlyFestivalListAdapter(Context context, ArrayList<MonthlyFestivalListData> datas, View.OnClickListener onClickListener) {
        this.context = context;
        this.datas = datas;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.monthly_festival_list_item, parent, false);

        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        MonthlyFestivalListData data = datas.get(position);
        Glide.with(context).load(data.getMfs_img()).into(holder.img);
        holder.mfs_name.setText(data.getMfs_addr());
        holder.mfs_date.setText(data.getMfs_num());
        holder.body.setOnClickListener(onClickListener);
        holder.body.setTag(data);
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView mfs_name;
        TextView mfs_date;
        RelativeLayout body;
        public Holder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
            mfs_name = itemView.findViewById(R.id.mfs_name);
            mfs_date = itemView.findViewById(R.id.mfs_date);
            body = itemView.findViewById(R.id.body);
        }
    }
}
