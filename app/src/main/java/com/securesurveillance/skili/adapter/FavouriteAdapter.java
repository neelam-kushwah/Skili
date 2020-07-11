package com.securesurveillance.skili.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
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
import com.securesurveillance.skili.R;
import com.securesurveillance.skili.RecommendedDetailActivity;
import com.securesurveillance.skili.fragments.FavouriteFragment;
import com.securesurveillance.skili.model.RecommendedModel;
import com.securesurveillance.skili.model.responsemodel.GetFavApplicantListModel;
import com.securesurveillance.skili.profile.MyProfileActivity;

import java.util.List;

/**
 * Created by adarsh on 7/8/2018.
 */

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.MyViewHolder> {
    private List<GetFavApplicantListModel.Applicant> data;
    private LayoutInflater inflater;
    private FavouriteFragment context;
    boolean isVerticalView;
    RemoveFav listener;

    public FavouriteAdapter(FavouriteFragment context, List<GetFavApplicantListModel.Applicant> data, boolean isVerticalView) {
        this.context = context;
        inflater = LayoutInflater.from(context.getActivity());
        this.data = data;
        this.isVerticalView = isVerticalView;
        listener = (RemoveFav) context;
    }

    public interface RemoveFav {
        void onRemovefav(int pos);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (isVerticalView) {
            view = inflater.inflate(R.layout.listitem_fav_vertical, parent, false);

        } else {
            view = inflater.inflate(R.layout.listitem_fav_horizontal, parent, false);

        }
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final GetFavApplicantListModel.Applicant current = data.get(position);
        holder.tvDistance.setText(current.getDistance());
        holder.tvName.setText(current.getName());
        holder.tvRating.setText(current.getRating());

        holder.rlMessage.setTag(position);
        holder.tvRating.setTag(position);
        holder.rlRemove.setTag(position);
        holder.ivCandidate.setTag(position);
        holder.tvName.setTag(position);
        if (current.getChargeType() != null) {
            String chargeType = current.getChargeType();
            String chargeTypeString = "";
            if (chargeType.contains("MONTH")) {
                chargeTypeString = "INR/month";
            } else if (chargeType.contains("HOUR")) {
                chargeTypeString = "INR/hour";

            } else if (chargeType.contains("YEAR")) {
                chargeTypeString = "INR/year";

            }
            holder.ivDistance.setVisibility(View.VISIBLE);
            holder.tvPrice.setVisibility(View.VISIBLE);

            holder.tvPrice.setText(current.getCharge() + " " + chargeTypeString);

        } else {
            holder.ivDistance.setVisibility(View.GONE);
            holder.tvPrice.setVisibility(View.GONE);

        }
        Picasso.get().load(current.getProfilePic()).into(holder.ivCandidate, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(Exception e) {
                holder.ivCandidate.setBackground(context.getActivity().getDrawable(R.drawable.user_profile));

            }
        });
        holder.rlMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RelativeLayout rl = (RelativeLayout) view;
                int pos = Integer.parseInt(rl.getTag().toString());
                Intent i = new Intent(context.getActivity(), ChatOneToOneActivity.class);
                i.putExtra("TOPROFILEID", data.get(pos).getProfileId());
                i.putExtra("NAME", data.get(pos).getName());
                i.putExtra("PIC", data.get(pos).getProfilePic());
                context.startActivity(i);

            }
        });
        holder.rlRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout rl = (RelativeLayout) v;
                int pos = Integer.parseInt(rl.getTag().toString());

                listener.onRemovefav(pos);
            }
        });
        holder.tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView iv = (TextView) v;
                int pos = Integer.parseInt(iv.getTag().toString());
                Intent i = new Intent(context.getActivity(), MyProfileActivity.class);
                i.putExtra("PID", data.get(pos).getMobileNumber());
                i.putExtra("PROFILEID", data.get(pos).getProfileId());
                i.putExtra("ISFAV", "true");

                context.startActivity(i);
            }
        });
        holder.ivCandidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView iv = (ImageView) v;
                int pos = Integer.parseInt(iv.getTag().toString());
                Intent i = new Intent(context.getActivity(), MyProfileActivity.class);
                i.putExtra("PID", data.get(pos).getMobileNumber());
                i.putExtra("PROFILEID", data.get(pos).getProfileId());
                i.putExtra("ISFAV", "true");

                context.startActivity(i);
            }
        });

    }


    @Override
    public int getItemCount() {
        return data.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName, tvRating, tvDistance, tvPrice;
        private RelativeLayout rlRemove, rlMessage, rlCardView;
        private ImageView ivCandidate, ivDistance;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvRating = (TextView) itemView.findViewById(R.id.tvRating);
            tvDistance = (TextView) itemView.findViewById(R.id.tvDistance);
            tvPrice = (TextView) itemView.findViewById(R.id.tvPrice);
            rlRemove = (RelativeLayout) itemView.findViewById(R.id.rlRemove);
            rlMessage = (RelativeLayout) itemView.findViewById(R.id.rlMessage);
            ivCandidate = (ImageView) itemView.findViewById(R.id.ivCandidate);
            rlCardView = (RelativeLayout) itemView.findViewById(R.id.rlCardView);
            ivDistance = (ImageView) itemView.findViewById(R.id.ivDistance);
        }
    }
}
