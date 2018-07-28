package com.tastreet.OwnerPage;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tastreet.R;

import java.util.ArrayList;

public class FoodListAdapter extends RecyclerView.Adapter<FoodListAdapter.Holder> {

    Context context;
    ArrayList<FoodListData> datas = new ArrayList<>();
    View.OnClickListener onClickListener;

    public FoodListAdapter(Context context, ArrayList<FoodListData> datas, View.OnClickListener onClickListener) {
        this.datas = datas;
        this.onClickListener = onClickListener;
        this.context = context;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.food_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        FoodListData data = datas.get(position);
        Glide.with(context).load(data.getImg_url()).into(holder.img);
        holder.description.setText(data.getDescription());
        holder.img.setOnClickListener(onClickListener);
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView description;
        public Holder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
            description = itemView.findViewById(R.id.description);
        }
    }
}
