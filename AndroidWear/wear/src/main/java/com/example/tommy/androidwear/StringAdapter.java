package com.example.tommy.androidwear;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Tommy on 2018/3/24.
 */

public class StringAdapter extends RecyclerView.Adapter<StringAdapter.ViewHolder> {
    private static final String TAG= "StringAdapter";
    private String[] mData;

    public StringAdapter(String[] data) {
        this.mData = data;
    }

    public void updateData(String[] data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 实例化展示的view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        // 实例化viewholder
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // 绑定数据
        holder.mTv.setText(mData[position]);
        holder.mTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "onClick: "+mData[position]);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView mTv;

        public ViewHolder(View itemView) {
            super(itemView);
            mTv = (TextView) itemView.findViewById(R.id.serial_option);
        }
    }
}


