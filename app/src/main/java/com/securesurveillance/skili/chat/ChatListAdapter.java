package com.securesurveillance.skili.chat;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.securesurveillance.skili.R;
import com.securesurveillance.skili.apiHandler.CommonMethod;
import com.securesurveillance.skili.model.ChatModel;
import com.securesurveillance.skili.model.responsemodel.ConversationModel;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by adarsh on 7/8/2018.
 */

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.MyViewHolder> {
    private List<ConversationModel.ConversationResponse> data;
    private LayoutInflater inflater;
    private Context context;
    String name;
    String pic;
    String FAV;

    public ChatListAdapter(Context context, List<ConversationModel.ConversationResponse> data, String name,
                           String pic, String FAV) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;

        this.name=name;
        this.pic=pic;
        this.FAV=FAV;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.listitem_chat, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        ConversationModel.ConversationResponse current = data.get(position);
        holder.tvDate.setText(current.getMsgDate());
        holder.tvMessage.setText(current.getMessage());
        holder.tvName.setText(CommonMethod.getFirstCapName(current.getToProfileName()));
        holder.rlCardView.setTag(position);

        Picasso.get().load(current.getToProfilePic()).into(holder.ivProfile, new Callback() {
            @Override
            public void onSuccess() {

            }


            @Override
            public void onError(Exception e) {
                holder.ivProfile.setBackground(context.getDrawable(R.drawable.user_profile));
            }
        });
        holder.rlCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout rl = (RelativeLayout) v;
                int pos = Integer.parseInt(rl.getTag().toString());
                Intent i = new Intent(context, ChatOneToOneActivity.class);
                i.putExtra("TOPROFILEID", data.get(pos).getToProfileId());
                i.putExtra("NAME", data.get(pos).getToProfileName());
                i.putExtra("PIC", data.get(pos).getToProfilePic());
                i.putExtra("FAV", data.get(pos).isFavouriteProfile());

                context.startActivity(i);
            }
        });
    }


    @Override
    public int getItemCount() {
        return data.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvDate, tvMessage, tvName;
private ImageView ivProfile;
        private RelativeLayout rlCardView;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvDate = (TextView) itemView.findViewById(R.id.tvDate);
            tvMessage = (TextView) itemView.findViewById(R.id.tvMessage);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            rlCardView = (RelativeLayout) itemView.findViewById(R.id.rlCardView);
            ivProfile=(ImageView)itemView.findViewById(R.id.ivProfile);
        }
    }
}
