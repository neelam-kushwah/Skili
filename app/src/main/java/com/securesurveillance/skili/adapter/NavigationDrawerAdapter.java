package com.securesurveillance.skili.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.securesurveillance.skili.R;
import com.securesurveillance.skili.model.NavDrawerItem;

import java.util.Collections;
import java.util.List;


public class NavigationDrawerAdapter extends RecyclerView.Adapter<NavigationDrawerAdapter.MyViewHolder> {
    private  List<NavDrawerItem> data = Collections.emptyList();
    private LayoutInflater inflater;
    private Context context;
    private int layoutResID;

    public NavigationDrawerAdapter(Context context, int layoutResID, List<NavDrawerItem> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.layoutResID = layoutResID;

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.nav_drawer_row, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        NavDrawerItem current = data.get(position);
        holder.tv_Title.setText(current.getTitle());
        holder.iconImageView.setImageResource(current.getImgResID());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
      private  TextView tv_Title;
        private ImageView iconImageView;


        public MyViewHolder(View itemView) {
            super(itemView);
            tv_Title = (TextView) itemView.findViewById(R.id.tv_Title);
               iconImageView = (ImageView) itemView.findViewById(R.id.drawerListImage);



        }
    }
}
