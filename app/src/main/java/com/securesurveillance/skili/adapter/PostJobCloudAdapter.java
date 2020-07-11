package com.securesurveillance.skili.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.securesurveillance.skili.R;

import java.util.ArrayList;

/**
 * Created by adarsh on 7/8/2018.
 */

public class PostJobCloudAdapter extends RecyclerView.Adapter<PostJobCloudAdapter.MyViewHolder> {
    private ArrayList<String> arrayListHashMap;
    private LayoutInflater inflater;
    private Context context;
    ItemDeletePostJob listener;

    public PostJobCloudAdapter(Context context, ArrayList<String> arrayListHashMap) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.arrayListHashMap = arrayListHashMap;

         listener = (ItemDeletePostJob) context;

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.postjobskill_listitem, parent, false);
        MyViewHolder holder = new MyViewHolder(view);


        return holder;
    }

    public interface ItemDeletePostJob {
        void onItemDeleted(String pos);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
      holder.rlClose.setTag(position);
        holder.tvSubCategory.setText(arrayListHashMap.get(position).toUpperCase());
        holder. rlClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = (int) view.getTag();
                  listener.onItemDeleted(arrayListHashMap.get(pos));
            }
        });


    }


    @Override
    public int getItemCount() {
        return arrayListHashMap.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout rlClose;
        TextView tvSubCategory;

        public MyViewHolder(View itemView) {
            super(itemView);
            rlClose = (RelativeLayout) itemView.findViewById(R.id.rlClose);
            tvSubCategory = (TextView) itemView.findViewById(R.id.tvSubCategory);
        }
    }
}
