package com.securesurveillance.skili.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.securesurveillance.skili.R;
import com.securesurveillance.skili.RecommendedDetailActivity;
import com.securesurveillance.skili.model.RecommendedModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by adarsh on 7/8/2018.
 */

public class AppliedAdapter extends RecyclerView.Adapter<AppliedAdapter.MyViewHolder> {
    private List<RecommendedModel> data;
    private LayoutInflater inflater;
    private Context context;
    private final String[] labels = {"Applied", "Application Sent", "Viewed"};

    public AppliedAdapter(Context context, List<RecommendedModel> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.listitem_applied, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        RecommendedModel current = data.get(position);
        holder.tvCompanyName.setText(current.getCompanyName());
        holder.tvExperience.setText(current.getExperience());
        holder.tvHeaderDesignation.setText(current.getDesignationHeader());
        holder.tvPostedOn.setText(current.getPostedOn());
        holder.btnApply.setTag(position);
        holder.rlCardView.setTag(position);

        holder.tvPlace.setText(current.getPlace());
        holder.btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Button btn=(Button)v;
//                int pos = Integer.parseInt(btn.getTag().toString());
//                Toast.makeText(context, ""+pos, Toast.LENGTH_LONG).show();
//                Intent i = new Intent(context, RecommendedDetailActivity.class);
//                context.startActivity(i);
            }
        });
        holder.rlCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout rl=(RelativeLayout) v;
                int pos = Integer.parseInt(rl.getTag().toString());
                Toast.makeText(context, ""+pos, Toast.LENGTH_LONG).show();
                Intent i = new Intent(context, RecommendedDetailActivity.class);
                context.startActivity(i);
            }
        });
       }


    @Override
    public int getItemCount() {
        return data.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvHeaderDesignation, tvCompanyName, tvPostedOn, tvExperience, tvPlace;
        private Button btnApply;
        private RelativeLayout rlCardView;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvHeaderDesignation = (TextView) itemView.findViewById(R.id.tvHeaderDesignation);
            tvCompanyName = (TextView) itemView.findViewById(R.id.tvCompanyName);
            tvPostedOn = (TextView) itemView.findViewById(R.id.tvPostedOn);
            tvExperience = (TextView) itemView.findViewById(R.id.tvExperience);
            tvPlace = (TextView) itemView.findViewById(R.id.tvPlace);
            btnApply = (Button) itemView.findViewById(R.id.btnApply);
            rlCardView=(RelativeLayout)itemView.findViewById(R.id.rlCardView);
        }
    }
}
