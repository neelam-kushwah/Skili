package com.securesurveillance.skili.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.securesurveillance.skili.R;
import com.securesurveillance.skili.RecommendedDetailActivity;
import com.securesurveillance.skili.model.RecommendedModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by adarsh on 7/8/2018.
 */

public class CloudAdapter extends RecyclerView.Adapter<CloudAdapter.MyViewHolder> {
    private HashMap<String, ArrayList<String>> arrayListHashMap;
    private LayoutInflater inflater;
    private Context context;
    List<String> data;
    ItemDelete listener;
    boolean isNotMe=false;
    public CloudAdapter(Context context, HashMap<String, ArrayList<String>> arrayListHashMap) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.arrayListHashMap = arrayListHashMap;
        data = new ArrayList<String>(arrayListHashMap.keySet()); // <== Set to List

        listener = (ItemDelete) context;

    }

    public CloudAdapter(Context context, HashMap<String, ArrayList<String>> arrayListHashMap, boolean isNotMe) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.arrayListHashMap = arrayListHashMap;
        data = new ArrayList<String>(arrayListHashMap.keySet()); // <== Set to List
this.isNotMe=isNotMe;
        listener = (ItemDelete) context;

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.listitem_cloud, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

public interface ItemDelete {

   void onItemDeleted(int pos, int childPos, boolean isParent);
}
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        View view = inflater.inflate(R.layout.selectcategory_listitem, null);
        TextView tvMainCategory = (TextView) view.findViewById(R.id.tvMainCategory);
        RelativeLayout rlCloseP=(RelativeLayout)view.findViewById(R.id.rlCloseP);
        rlCloseP.setTag(position);
        rlCloseP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos= (int) view.getTag();
                listener.onItemDeleted(pos,0,true);
            }
        });
        if(isNotMe){
            rlCloseP.setVisibility(View.INVISIBLE);
        }
        tvMainCategory.setText(data.get(position).toUpperCase());
        LinearLayout rl = new LinearLayout(context);
        rl.setOrientation(LinearLayout.HORIZONTAL);
        rl.addView(view);
        Log.i("Parent",data.get(position).toUpperCase());
        for (int i = 0; i < arrayListHashMap.get(data.get(position)).size(); i++) {
            View viewInner = inflater.inflate(R.layout.selectsubcategory_listitem, null);
            RelativeLayout rlClose=(RelativeLayout)viewInner.findViewById(R.id.rlClose);
            rlClose.setTag(i);
            rlClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos= (int) view.getTag();
                    listener.onItemDeleted(position,pos,false);
                }
            });
            if(isNotMe){
                rlClose.setVisibility(View.INVISIBLE);
            }
            TextView tvSubCategory = (TextView) viewInner.findViewById(R.id.tvSubCategory);
            tvSubCategory.setText(arrayListHashMap.get(data.get(position)).get(i).toUpperCase());
            rl.addView(viewInner);
            Log.i("Child",arrayListHashMap.get(data.get(position)).get(i).toUpperCase());


        }
        holder.llCloud.addView(rl);

//        holder.horizontalScrollView.addView(llCl);

//        holder.rlCardView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                RelativeLayout rl=(RelativeLayout) v;
//                int pos = Integer.parseInt(rl.getTag().toString());
//                Toast.makeText(context, ""+pos, Toast.LENGTH_LONG).show();
//                Intent i = new Intent(context, RecommendedDetailActivity.class);
//                context.startActivity(i);
//            }
//        });
    }


    @Override
    public int getItemCount() {
        return arrayListHashMap.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout llCloud;
        HorizontalScrollView horizontalScrollView;

        public MyViewHolder(View itemView) {
            super(itemView);
            horizontalScrollView = (HorizontalScrollView) itemView.findViewById(R.id.horizontalScrollView);
            llCloud = (LinearLayout) itemView.findViewById(R.id.llCloud);

        }
    }
}
