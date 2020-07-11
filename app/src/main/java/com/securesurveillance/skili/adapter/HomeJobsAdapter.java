package com.securesurveillance.skili.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.securesurveillance.skili.chat.ChatOneToOneActivity;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.securesurveillance.skili.JobDetailRecruiterActivity;
import com.securesurveillance.skili.R;
import com.securesurveillance.skili.model.HomeJobsModel;
import com.securesurveillance.skili.model.responsemodel.GetAllJobRecruiterDetailModel;
import com.securesurveillance.skili.model.responsemodel.SearchJobsResponse;
import com.securesurveillance.skili.profile.MyProfileActivity;
import com.securesurveillance.skili.utility.Constants;

import java.util.List;

/**
 * Created by adarsh on 7/8/2018.
 */

public class HomeJobsAdapter extends RecyclerView.Adapter<HomeJobsAdapter.MyViewHolder> {
    private List<GetAllJobRecruiterDetailModel> data;
    private LayoutInflater inflater;
    private Context context;
    ApplyListener listener;

    public HomeJobsAdapter(Context context, List<GetAllJobRecruiterDetailModel> data, ApplyListener listener) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.listener = listener;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.listitem_homejobs, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        GetAllJobRecruiterDetailModel current = data.get(position);
        holder.tvBudget.setText(current.getStatus());
        holder.tvDesc.setText(current.getJobDescription());
        holder.tvPlace.setText(current.getJobLocation());
        holder.tvJob.setText(current.getJobTitle());
        holder.tvTime.setText(current.getJobType());
        holder.tvRecruiterName.setText(current.getPostedBy().getName());
        holder.rlArtist.setTag(position);//open job detail
        holder.btnApply.setTag(position);
holder.tvRecruiterName.setTag(position);
holder.artistPic.setTag(position);
        holder.rlRecruiterLayout.setTag(position);
        if (current.getJobType().contains("Part")) {
            holder.ivTime.setBackgroundResource(R.drawable.parttime);
            holder.tvTime.setTextColor(ContextCompat.getColor(context, R.color.cancelColor));
            holder.viewColor.setBackgroundColor(ContextCompat.getColor(context, R.color.cancelColor));
        } else {
            holder.tvTime.setTextColor(ContextCompat.getColor(context, R.color.completeColor));
            holder.viewColor.setBackgroundColor(ContextCompat.getColor(context, R.color.completeColor));
            holder.ivTime.setBackgroundResource(R.drawable.fulltime);
        }
        if (current.getStatus().equalsIgnoreCase("Completed")) {
            holder.tvStatus.setVisibility(View.GONE);
            holder.btnApply.setVisibility(View.GONE);
        } else if (current.getApplicationStatus()!=null&&(current.getApplicationStatus()
                .equalsIgnoreCase("APPLIED")||
                current.getApplicationStatus().equalsIgnoreCase("REJECTED")||
                current.getApplicationStatus().equalsIgnoreCase("ACCEPTED")
                ||current.getApplicationStatus().equalsIgnoreCase("CANCELLED"))) {
            holder.tvStatus.setVisibility(View.VISIBLE);
            holder.tvStatus.setText(current.getApplicationStatus());
            holder.btnApply.setVisibility(View.GONE);
        } else {
            holder.tvStatus.setVisibility(View.GONE);
            holder.btnApply.setVisibility(View.VISIBLE);
        }
        if (!TextUtils.isEmpty(current.getPostedBy().getProfilePic())) {
            Picasso.get().load(current.getPostedBy().getProfilePic()).into(holder.artistPic, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError(Exception e) {
                    holder.artistPic.setBackground(context.getDrawable(R.drawable.user_profile));
                }
            });
        }
        holder.tvConnect.setTag(position);
        holder.tvConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(context,"Coming Soon...", Toast.LENGTH_LONG).show();
                TextView rl = (TextView) v;
                int pos = Integer.parseInt(rl.getTag().toString());
                Intent i = new Intent(context, ChatOneToOneActivity.class);
                i.putExtra("TOPROFILEID",data.get(pos).getPostedBy().getProfileId());
//
                i.putExtra("NAME",data.get(pos).getPostedBy().getName());

                i.putExtra("PIC",data.get(pos).getPostedBy().getProfilePic());
                context.startActivity(i);
            }
        });
        holder.rlArtist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RelativeLayout rl = (RelativeLayout) view;
                int pos = Integer.parseInt(rl.getTag().toString());
                Intent i = new Intent(context, JobDetailRecruiterActivity.class);
                i.putExtra("DATA", data.get(pos));
                i.putExtra("ISFAV", 0);

                context.startActivity(i);
            }
        });
        holder.tvRecruiterName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView rl = (TextView) v;
                int pos = Integer.parseInt(rl.getTag().toString());
                Intent i = new Intent(context, MyProfileActivity.class);
                i.putExtra("PID", data.get(pos).getPostedBy().getMobileNumber());
                i.putExtra("PROFILEID", data.get(pos).getPostedBy().getProfileId());
                i.putExtra("ISFAV", data.get(pos).getPostedBy().getFavRecruiter());
                context.startActivity(i);
            }
        });
        holder.artistPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView rl = (ImageView) v;
                int pos = Integer.parseInt(rl.getTag().toString());
                Intent i = new Intent(context, MyProfileActivity.class);
                i.putExtra("PID", data.get(pos).getPostedBy().getMobileNumber());
                i.putExtra("PROFILEID", data.get(pos).getPostedBy().getProfileId());
                i.putExtra("ISFAV", data.get(pos).getPostedBy().getFavRecruiter());
                context.startActivity(i);
            }
        });
        holder.btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button btn = (Button) view;
                int pos = Integer.parseInt(btn.getTag().toString());
                listener.onApplyClicked(pos);
            }
        });
    }

    public interface ApplyListener {
        void onApplyClicked(int pos);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvJob, tvDesc, tvBudget, tvTime, tvPlace, tvRecruiterName, tvStatus,tvConnect;
        private View viewColor;
        private RelativeLayout rlArtist, rlRecruiterLayout;
        ImageView ivTime, artistPic;
        Button btnApply;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvJob = (TextView) itemView.findViewById(R.id.tvJob);
            tvDesc = (TextView) itemView.findViewById(R.id.tvDesc);
            tvBudget = (TextView) itemView.findViewById(R.id.tvBudget);
            tvTime = (TextView) itemView.findViewById(R.id.tvTime);
            tvPlace = (TextView) itemView.findViewById(R.id.tvPlace);
            viewColor = (View) itemView.findViewById(R.id.viewColor);
            rlArtist = (RelativeLayout) itemView.findViewById(R.id.rlArtist);
            ivTime = (ImageView) itemView.findViewById(R.id.ivTime);
            rlRecruiterLayout = (RelativeLayout) itemView.findViewById(R.id.rlRecruiterLayout);
            btnApply = (Button) itemView.findViewById(R.id.btnApply);
            tvRecruiterName = (TextView) itemView.findViewById(R.id.tvRecruiterName);
            artistPic = (ImageView) itemView.findViewById(R.id.artistPic);
            tvStatus = (TextView) itemView.findViewById(R.id.tvStatus);
            tvConnect= (TextView) itemView.findViewById(R.id.tvConnect);
        }
    }
}
