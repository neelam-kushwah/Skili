package com.securesurveillance.skili.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.securesurveillance.skili.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by adarsh on 7/8/2018.
 */

public class JobDetailCloudAdapter extends RecyclerView.Adapter<JobDetailCloudAdapter.MyViewHolder> {
    private ArrayList<String> arrayListHashMap;
    private LayoutInflater inflater;
    private Context context;
    ItemDelete listener;

    public JobDetailCloudAdapter(Context context, ArrayList<String> arrayListHashMap) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.arrayListHashMap = arrayListHashMap;

        //  listener = (ItemDelete) context;

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.jobskill_listitem, parent, false);
        MyViewHolder holder = new MyViewHolder(view);


        return holder;
    }

    public interface ItemDelete {
        void onItemDeleted(int pos, int childPos, boolean isParent);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
      // holder.rlClose.setTag(position);
        holder.tvSubCategory.setText(arrayListHashMap.get(position).toUpperCase());
//        holder. rlClose.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                int pos = (int) view.getTag();
//                //  listener.onItemDeleted(pos, 0, true);
//            }
//        });


    }


    @Override
    public int getItemCount() {
        return arrayListHashMap.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
       // RelativeLayout rlClose;
        TextView tvSubCategory;

        public MyViewHolder(View itemView) {
            super(itemView);
           // rlClose = (RelativeLayout) itemView.findViewById(R.id.rlClose);
            tvSubCategory = (TextView) itemView.findViewById(R.id.tvSubCategory);
        }
    }
}
