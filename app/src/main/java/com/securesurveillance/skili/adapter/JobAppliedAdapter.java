package com.securesurveillance.skili.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.securesurveillance.skili.apiHandler.CommonMethod;
import com.securesurveillance.skili.apiHandler.ConnectivityUtils;
import com.securesurveillance.skili.profile.MyProfileActivity;
import com.squareup.picasso.Picasso;
import com.securesurveillance.skili.R;
import com.securesurveillance.skili.model.GlobalFeedModel;
import com.securesurveillance.skili.model.responsemodel.GetApplicantListModel;
import com.securesurveillance.skili.utility.RoundedImageView;

import java.util.List;

/**
 * Created by adarsh on 7/8/2018.
 */

public class JobAppliedAdapter extends RecyclerView.Adapter<JobAppliedAdapter.MyViewHolder> {
    private List<GetApplicantListModel.Result.Applicant> data;
    private LayoutInflater inflater;
    private Context context;
    AddApplicant listener;
    boolean isAccepted;
    int acceptedPosition;
    public JobAppliedAdapter(Context context, List<GetApplicantListModel.Result.Applicant> data, boolean isAccepted, int acceptedPosition) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.isAccepted=isAccepted;
        this.acceptedPosition=acceptedPosition;
        listener = (AddApplicant) context;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.listitem_jobapplied, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    public interface AddApplicant {
        void onFavAdd(int pos);

        void onAccept(int pos);
        void onReject(int pos);

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        GetApplicantListModel.Result.Applicant current = data.get(position);
        holder.tvName.setText(current.getName());
        holder.tvRating.setText(current.getRating());
        Picasso.get().load(current.getProfilePic()).into(holder.artistPic);
        holder.rlCardView.setTag(position);
        holder.rlFav.setTag(position);
        holder.rlAccept.setTag(position);
        holder.artistPic.setTag(position);
        holder.rlReject.setTag(position);
        holder.rlName.setTag(position);

        if(isAccepted){
            holder.rlAccept.setVisibility(View.GONE);
            holder.tvStatus.setVisibility(View.GONE);
            holder.rlReject.setVisibility(View.GONE);
            if(acceptedPosition==position){
                holder.rlReject.setVisibility(View.VISIBLE);
                holder.tvStatus.setVisibility(View.VISIBLE);

            }

        }else{
            holder.rlAccept.setVisibility(View.VISIBLE);
            holder.rlReject.setVisibility(View.VISIBLE);
            holder.tvStatus.setVisibility(View.GONE);

        }

        holder.rlFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout rl = (RelativeLayout) v;
                int pos = Integer.parseInt(rl.getTag().toString());
                listener.onFavAdd(pos);
            }
        });
        holder.rlAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout rl = (RelativeLayout) v;
                int pos = Integer.parseInt(rl.getTag().toString());
                listener.onAccept(pos);
            }
        });
        holder.rlReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout rl = (RelativeLayout) v;
                int pos = Integer.parseInt(rl.getTag().toString());
                listener.onReject(pos);
            }
        });
        holder.rlName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout rl = (RelativeLayout) v;
                int pos = Integer.parseInt(rl.getTag().toString());
                if (ConnectivityUtils.isNetworkEnabled(context)) {
                    Intent i = new Intent(context, MyProfileActivity.class);
                    i.putExtra("PID", data.get(pos).getMobileNumber());
                    i.putExtra("PROFILEID", data.get(pos).getProfileId());
                    i.putExtra("ISFAV", data.get(pos).getFavouriteApplicant());

                    context.startActivity(i);
                } else {
                    CommonMethod.showToastMessage(context, "Please check your internet connection");
                }
            }
        });
        holder.artistPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout rl = (RelativeLayout) v;
                int pos = Integer.parseInt(rl.getTag().toString());
                if (ConnectivityUtils.isNetworkEnabled(context)) {
                    Intent i = new Intent(context, MyProfileActivity.class);
                    i.putExtra("PID", data.get(pos).getMobileNumber());
                    i.putExtra("PROFILEID", data.get(pos).getProfileId());
                    i.putExtra("ISFAV", data.get(pos).getFavouriteApplicant());

                    context.startActivity(i);
                } else {
                    CommonMethod.showToastMessage(context, "Please check your internet connection");
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return data.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName, tvRating,tvStatus;
        RoundedImageView artistPic;
        private RelativeLayout rlFav, rlReject, rlAccept,rlName;
        private RelativeLayout rlCardView;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvRating = (TextView) itemView.findViewById(R.id.tvRating);
            tvStatus = (TextView) itemView.findViewById(R.id.tvStatus);
            rlName = (RelativeLayout) itemView.findViewById(R.id.rlName);
            rlCardView = (RelativeLayout) itemView.findViewById(R.id.rlCardView);
            artistPic = (RoundedImageView) itemView.findViewById(R.id.artistPic);
            rlAccept = (RelativeLayout) itemView.findViewById(R.id.rlAccept);
            rlReject = (RelativeLayout) itemView.findViewById(R.id.rlReject);
            rlFav = (RelativeLayout) itemView.findViewById(R.id.rlFav);
        }
    }
}
