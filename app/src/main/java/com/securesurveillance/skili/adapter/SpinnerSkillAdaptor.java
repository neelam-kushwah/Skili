package com.securesurveillance.skili.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.securesurveillance.skili.R;
import com.securesurveillance.skili.model.SkillModel;

import java.util.ArrayList;

public class SpinnerSkillAdaptor extends BaseAdapter {
    private LayoutInflater lInflater;
    private ArrayList<SkillModel.InnerSkillModel> arrModel;
    private HolderListItem holderListItem;
    private View view;
    private Context context;

    public SpinnerSkillAdaptor(Context context, ArrayList<SkillModel.InnerSkillModel> arrModel) {
        this.arrModel = arrModel;
        this.context = context;
        lInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return arrModel == null ? 0 : arrModel.size();
    }

    @Override
    public Object getItem(int position) {
        return arrModel.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.adapter_data_generic_skills, parent, false);
            holderListItem = new HolderListItem();
            holderListItem.tv_Text = (TextView) view.findViewById(R.id.tv_Text);
            holderListItem.ivCamera = (ImageView) view.findViewById(R.id.ivCamera);
            holderListItem.ivVideo = (ImageView) view.findViewById(R.id.ivVideo);
            holderListItem.ivLink = (ImageView) view.findViewById(R.id.ivLink);


            view.setTag(holderListItem);
        } else {
            holderListItem = (HolderListItem) view.getTag();
        }

        holderListItem.tv_Text.setText(arrModel.get(position).getName());
        if (arrModel.get(position).isEnableImage()) {
            holderListItem.ivCamera.setVisibility(View.VISIBLE);
        } else {
            holderListItem.ivCamera.setVisibility(View.GONE);
        }
        if (arrModel.get(position).isEnableVideo()) {
            holderListItem.ivVideo.setVisibility(View.VISIBLE);
        } else {
            holderListItem.ivVideo.setVisibility(View.GONE);
        }
        if (arrModel.get(position).isEnableLink()) {
            holderListItem.ivLink.setVisibility(View.VISIBLE);
        } else {
            holderListItem.ivLink.setVisibility(View.GONE);
        }
        return view;
    }

    private class HolderListItem {
        private TextView tv_Text;
        private ImageView ivCamera, ivVideo, ivLink;

    }
}