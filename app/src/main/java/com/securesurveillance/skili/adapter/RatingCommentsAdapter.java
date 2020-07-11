package com.securesurveillance.skili.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.securesurveillance.skili.R;
import com.securesurveillance.skili.fragments.FavouriteFragment;
import com.securesurveillance.skili.model.responsemodel.GetAllProfileDetailsModel;
import com.securesurveillance.skili.model.responsemodel.GetFavApplicantListModel;
import com.securesurveillance.skili.profile.MyProfileActivity;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by adarsh on 7/8/2018.
 */

public class RatingCommentsAdapter extends RecyclerView.Adapter<RatingCommentsAdapter.MyViewHolder> {
    private ArrayList<GetAllProfileDetailsModel.ProfileRatingFeedbackDto> data;
    private LayoutInflater inflater;
    private Context context;


    public RatingCommentsAdapter(Context context, ArrayList<GetAllProfileDetailsModel.ProfileRatingFeedbackDto> data
    ) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        view = inflater.inflate(R.layout.listitem_ratingocmments, parent, false);


        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final GetAllProfileDetailsModel.ProfileRatingFeedbackDto current = data.get(position);
        holder.tvDesc.setText(current.getDescription());
        holder.tvName.setText(current.getRatingBy().getName());
        holder.tvRating.setText(current.getRatingBy().getRating());


        Picasso.get().load(current.getRatingBy().getProfilePic()).into(holder.ivCandidate, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(Exception e) {
                holder.ivCandidate.setBackground(context.getDrawable(R.drawable.user_profile));

            }
        });


    }


    @Override
    public int getItemCount() {
        return data.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName, tvRating, tvDesc;
        private ImageView ivCandidate;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvRating = (TextView) itemView.findViewById(R.id.tvRating);
            ivCandidate = (ImageView) itemView.findViewById(R.id.ivCandidate);
            tvDesc = (TextView) itemView.findViewById(R.id.tvDesc);
        }
    }
}
