package com.securesurveillance.skili.adapter;

import android.content.Context;
import android.content.Intent;
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
import com.securesurveillance.skili.R;
import com.securesurveillance.skili.RecommendedDetailActivity;
import com.securesurveillance.skili.apiHandler.CommonMethod;
import com.securesurveillance.skili.apiHandler.ConnectivityUtils;
import com.securesurveillance.skili.chat.ChatActivity;
import com.securesurveillance.skili.model.GlobalFeedModel;
import com.securesurveillance.skili.model.RecommendedModel;
import com.securesurveillance.skili.model.responsemodel.SearchCandidateResponse;
import com.securesurveillance.skili.profile.MyProfileActivity;
import com.securesurveillance.skili.utility.Constants;
import com.securesurveillance.skili.utility.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by adarsh on 7/8/2018.
 */

public class GlobalFeedAdapter extends RecyclerView.Adapter<GlobalFeedAdapter.MyViewHolder> {
    private List<SearchCandidateResponse.Result.Candidate> data;
    private LayoutInflater inflater;
    private Context context;

    public GlobalFeedAdapter(Context context, ArrayList<SearchCandidateResponse.Result.Candidate> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.listitem_globalfeed, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final SearchCandidateResponse.Result.Candidate current = data.get(position);
        if (current.getLastName() != null) {
            holder.tvName.setText(CommonMethod.getFirstCapName(current.getFirstName()) + " " +
                    CommonMethod.getFirstCapName(current.getLastName()));
        } else {
            holder.tvName.setText(CommonMethod.getFirstCapName(current.getFirstName()));


        }
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
            holder.ivRs.setVisibility(View.VISIBLE);
            holder.tvCharge.setVisibility(View.VISIBLE);

            holder.tvCharge.setText(current.getCharge() + " " + chargeTypeString);

        } else {
            holder.ivRs.setVisibility(View.GONE);
            holder.tvCharge.setVisibility(View.GONE);

        }
        holder.tvRating.setText(current.getRating());
        holder.tvView.setText(current.getViews() + " views");
        holder.tvName.setTag(position);
        holder.rlName.setTag(position);
        holder.ivCandidate.setTag(position);
        holder.tvConnect.setTag(position);
        holder.tvConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(context,"Coming Soon...", Toast.LENGTH_LONG).show();
                TextView rl = (TextView) v;
                int pos = Integer.parseInt(rl.getTag().toString());
                Intent i = new Intent(context, ChatOneToOneActivity.class);
                i.putExtra("TOPROFILEID", data.get(pos).get_id());
                i.putExtra("NAME", data.get(pos).getFirstName());
                i.putExtra("PIC", data.get(pos).getProfilePicThumbnail());
                context.startActivity(i);
            }
        });
        if (!TextUtils.isEmpty(current.getProfilePicThumbnail())) {
            Picasso.get().load(current.getProfilePicThumbnail()).into(holder.ivCandidate, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError(Exception e) {
                    holder.ivCandidate.setBackground(context.getDrawable(R.drawable.user_profile));
                }
            });
        }
        holder.tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView rl = (TextView) v;
                int pos = Integer.parseInt(rl.getTag().toString());
                if (ConnectivityUtils.isNetworkEnabled(context)) {
                    Intent i = new Intent(context, MyProfileActivity.class);
                    i.putExtra("PID", data.get(pos).getMobileNumber());
                    i.putExtra("PROFILEID", data.get(pos).get_id());
                    i.putExtra("ISFAV", data.get(pos).getFavouriteApplicant());

                    context.startActivity(i);
                } else {
                    CommonMethod.showToastMessage(context, "Please check your internet connection");
                }
            }
        });
        holder.rlName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView rl = (TextView) v;
                int pos = Integer.parseInt(rl.getTag().toString());
                if (ConnectivityUtils.isNetworkEnabled(context)) {
                    Intent i = new Intent(context, MyProfileActivity.class);
                    i.putExtra("PID", data.get(pos).getMobileNumber());
                    i.putExtra("PROFILEID", data.get(pos).get_id());
                    i.putExtra("ISFAV", data.get(pos).getFavouriteApplicant());

                    context.startActivity(i);
                } else {
                    CommonMethod.showToastMessage(context, "Please check your internet connection");
                }
            }
        });
        holder.ivCandidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RoundedImageView rl = (RoundedImageView) v;
                int pos = Integer.parseInt(rl.getTag().toString());
                if (ConnectivityUtils.isNetworkEnabled(context)) {
                    Intent i = new Intent(context, MyProfileActivity.class);
                    i.putExtra("PID", data.get(pos).getMobileNumber());
                    i.putExtra("PROFILEID", data.get(pos).get_id());
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
        private TextView tvName, tvRating, tvDistance, tvCharge, tvConnect, tvView;
        RoundedImageView ivCandidate;
        ImageView ivRs;
        private RelativeLayout rlName;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvRating = (TextView) itemView.findViewById(R.id.tvRating);
            tvDistance = (TextView) itemView.findViewById(R.id.tvDistance);
            tvCharge = (TextView) itemView.findViewById(R.id.tvCharge);
            tvConnect = (TextView) itemView.findViewById(R.id.tvConnect);
            tvView = (TextView) itemView.findViewById(R.id.tvView);
            rlName = (RelativeLayout) itemView.findViewById(R.id.rlName);
            ivCandidate = (RoundedImageView) itemView.findViewById(R.id.ivCandidate);
            ivRs = (ImageView) itemView.findViewById(R.id.ivRs);
        }
    }
}
