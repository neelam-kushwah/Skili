package com.securesurveillance.skili.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.securesurveillance.skili.JobDetailRecruiterActivity;
import com.securesurveillance.skili.R;
import com.securesurveillance.skili.RatingReviewActivity;
import com.securesurveillance.skili.SliderActivity;
import com.securesurveillance.skili.apiHandler.CommonMethod;
import com.securesurveillance.skili.apiHandler.ConnectivityUtils;
import com.securesurveillance.skili.model.responsemodel.NotificationModel;
import com.securesurveillance.skili.profile.MyProfileActivity;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by adarsh on 7/8/2018.
 */

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {
    private List<NotificationModel.NotificationResponse> data;
    private LayoutInflater inflater;
    private Context context;
    OnViewJobClicked listener;


    public interface OnViewJobClicked {
        void onJobclicked(String jobId);
        void onSeeRatingClicked();
    }

    public NotificationAdapter(Context context, List<NotificationModel.NotificationResponse> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
        listener = (OnViewJobClicked) context;

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.listitem_notifications, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final NotificationModel.NotificationResponse current = data.get(position);
         holder.tvDate.setText(current.getDate());
        holder.tvDescription.setText(current.getDescriptions());
        if (current.getAction().equals("VIEW_PROFILE")) {
            holder.tvAction.setVisibility(View.VISIBLE);
            holder.tvAction.setText("VIEW PROFILE");
        } else if (current.getAction().equals("VIEW_CANDIDATES")) {// not in use
            holder.tvAction.setVisibility(View.VISIBLE);
            holder.tvAction.setText("VIEW CANDIDATES");
        } else if (current.getAction().equals("VIEW_ALL_JOB")) {// not in use
            holder.tvAction.setVisibility(View.VISIBLE);
            holder.tvAction.setText("VIEW JOBS");

        } else if (current.getAction().equals("GIVE_RATING")) {
            holder.tvAction.setVisibility(View.VISIBLE);
            holder.tvAction.setText("GIVE RATING");

        } else if (current.getAction().equals("VIEW_JOB")) {// not in use
            holder.tvAction.setVisibility(View.VISIBLE);
            holder.tvAction.setText("VIEW JOB");

        } else if (current.getAction().equals("RATING")) {
            holder.tvAction.setVisibility(View.VISIBLE);
            holder.tvAction.setText("SEE RATING");

        } else {
            holder.tvAction.setVisibility(View.GONE);

        }
        if (!TextUtils.isEmpty(current.getDescriptions())) {
            holder.tvLetter.setText("" + current.getDescriptions().toUpperCase().charAt(0));
        }
        holder.tvAction.setTag(position);


        holder.tvAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TextView tv = (TextView) v;
                int pos = Integer.parseInt(tv.getTag().toString());
                if (data.get(pos).getAction().equals("VIEW_PROFILE")) {
                    if (ConnectivityUtils.isNetworkEnabled(context)) {

                        Intent i = new Intent(context, MyProfileActivity.class);
                        i.putExtra("PID", "" + data.get(pos).getTag1());// number
                        context.startActivity(i);
                    } else {
                        CommonMethod.showToastMessage(context, "Please check your internet connection");
                    }
                } else if (data.get(pos).getAction().equals("VIEW_CANDIDATES") ||
                        data.get(pos).getAction().equals("VIEW_ALL_JOB")) {//not in use
                    if (ConnectivityUtils.isNetworkEnabled(context)) {

                        Intent i = new Intent(context, SliderActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(i);
                    } else {
                        CommonMethod.showToastMessage(context, "Please check your internet connection");
                    }
                } else if (data.get(pos).getAction().equals("GIVE_RATING")) {
                    Intent i = new Intent(context, RatingReviewActivity.class);
                    i.putExtra("NAME", data.get(pos).getTag3());
                    i.putExtra("ID", data.get(pos).getTag1());
                    i.putExtra("JOBID", data.get(pos).getTag2());
                    i.putExtra("PIC", data.get(pos).getTag4());
                    context.startActivity(i);

                } else if (data.get(pos).getAction().equals("VIEW_JOB")) {
                    String jobId = data.get(pos).getTag1();
                    listener.onJobclicked(jobId);


                }else if(data.get(pos).getAction().equals("RATING")){
                    listener.onSeeRatingClicked();
                }

            }

        });
    }


    @Override
    public int getItemCount() {
        return data.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvAction, tvDate, tvDescription, tvLetter;

        private RelativeLayout rlCardView;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvAction = (TextView) itemView.findViewById(R.id.tvAction);
            tvDate = (TextView) itemView.findViewById(R.id.tvDate);
            tvDescription = (TextView) itemView.findViewById(R.id.tvDescription);
            rlCardView = (RelativeLayout) itemView.findViewById(R.id.rlCardView);
            tvLetter = (TextView) itemView.findViewById(R.id.tvLetter);
        }
    }
}
