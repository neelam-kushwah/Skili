package com.securesurveillance.skili.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.securesurveillance.skili.JobDetailRecruiterActivity;
import com.securesurveillance.skili.R;
import com.securesurveillance.skili.model.responsemodel.GetAllJobRecruiterDetailModel;
import com.securesurveillance.skili.utility.Constants;
import com.securesurveillance.skili.utility.SharePreferanceWrapperSingleton;

import java.util.List;

/**
 * Created by adarsh on 7/8/2018.
 */

public class SliderJobsAdapter extends RecyclerView.Adapter<SliderJobsAdapter.MyViewHolder> {
    private List<GetAllJobRecruiterDetailModel> data;
    private LayoutInflater inflater;
    private Context context;
    private SharePreferanceWrapperSingleton objSPS;
    int isFavourite = 0;

    public SliderJobsAdapter(Context context, List<GetAllJobRecruiterDetailModel> data, SharePreferanceWrapperSingleton objSPS, int isFavourite) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.objSPS = objSPS;
        this.isFavourite = isFavourite;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.listitem_sliderjobs, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        GetAllJobRecruiterDetailModel current = data.get(position);
        String loweCaseStatus = "";
        if (current.getApplicationStatus() == null) {
            String statusFirstChar = String.valueOf(current.getStatus().charAt(0)).toUpperCase();
            loweCaseStatus = current.getStatus().toLowerCase();
            loweCaseStatus = statusFirstChar.toCharArray()[0] + loweCaseStatus.substring(1, loweCaseStatus.length());

        } else {
            String statusFirstChar = String.valueOf(current.getApplicationStatus().charAt(0)).toUpperCase();
            loweCaseStatus = current.getApplicationStatus().toLowerCase();
            loweCaseStatus = statusFirstChar.toCharArray()[0] + loweCaseStatus.substring(1, loweCaseStatus.length());

        }


        holder.tvCancelled.setText(loweCaseStatus);
        holder.tvDesc.setText(current.getJobDescription());
        holder.tvPlace.setText(current.getJobLocation());
        holder.tvJob.setText(current.getJobTitle());
        if (current.getJobType().contains("PART")) {
            holder.tvTime.setText("Part Time");
        } else {
            holder.tvTime.setText("Full Time");

        }
        String budgetsFirstChar = String.valueOf(current.getBudgetType().charAt(0)).toUpperCase();
        String loweCaseBudget = current.getBudgetType().toLowerCase();

        loweCaseBudget = budgetsFirstChar.toCharArray()[0] + loweCaseBudget.substring(1, loweCaseBudget.length());
        try {
            if (current.getBudget().toString().trim().length() > 4) {
                holder.tvRs.setText((Float.parseFloat(current.getBudget()) / 1000) + "K");
            } else {
                holder.tvRs.setText(current.getBudget());
            }

        } catch (Exception e) {
            holder.tvRs.setText(current.getBudget());

        }
        holder.tvBudget.setText(loweCaseBudget);
        holder.rlCardView.setTag(position);
        if (current.getStatus().equalsIgnoreCase("Completed") || (current.getApplicationStatus() != null && current.getApplicationStatus().equalsIgnoreCase("ACCEPTED"))) {
            holder.tvCancelled.setTextColor(ContextCompat.getColor(context, R.color.completeColor));
            holder.viewColor.setBackgroundColor(ContextCompat.getColor(context, R.color.completeColor));
        } else {
            holder.tvCancelled.setTextColor(ContextCompat.getColor(context, R.color.cancelColor));
            holder.viewColor.setBackgroundColor(ContextCompat.getColor(context, R.color.cancelColor));

        }
        if (current.getJobType().contains("Part")) {
            holder.ivTime.setBackgroundResource(R.drawable.parttime);

        } else {
            holder.ivTime.setBackgroundResource(R.drawable.fulltime);

        }

        holder.rlCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout rl = (RelativeLayout) v;
                int pos = Integer.parseInt(rl.getTag().toString());

                Intent i = new Intent(context, JobDetailRecruiterActivity.class);
                i.putExtra("DATA", data.get(pos));
                i.putExtra("ISFAV", isFavourite);

                context.startActivity(i);
            }
        });
    }


    @Override
    public int getItemCount() {
        return data.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvJob, tvDesc, tvCancelled, tvTime, tvPlace, tvRs, tvBudget;
        private View viewColor;
        private RelativeLayout rlCardView;
        ImageView ivTime;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvJob = (TextView) itemView.findViewById(R.id.tvJob);
            tvDesc = (TextView) itemView.findViewById(R.id.tvDesc);
            tvCancelled = (TextView) itemView.findViewById(R.id.tvCancelled);
            tvTime = (TextView) itemView.findViewById(R.id.tvTime);
            tvPlace = (TextView) itemView.findViewById(R.id.tvPlace);
            tvRs = (TextView) itemView.findViewById(R.id.tvRs);
            tvBudget = (TextView) itemView.findViewById(R.id.tvBudget);
            viewColor = (View) itemView.findViewById(R.id.viewColor);
            rlCardView = (RelativeLayout) itemView.findViewById(R.id.rlCardView);
            ivTime = (ImageView) itemView.findViewById(R.id.ivTime);
        }
    }
}
