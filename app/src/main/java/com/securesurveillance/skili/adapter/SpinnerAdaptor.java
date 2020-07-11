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

public class SpinnerAdaptor extends BaseAdapter {
    private LayoutInflater lInflater;
    private ArrayList<String> arrModel;
    private HolderListItem holderListItem;
    private View view;
    ArrayList<SkillModel> arrSkillModel;
    private Context context;

    public SpinnerAdaptor(Context context, ArrayList<String> arrModel) {
        this.arrModel = arrModel;
        this.context = context;
        lInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public SpinnerAdaptor(Context context, ArrayList<String> arrModel, ArrayList<SkillModel> arrSkillModel) {
        this.arrModel = arrModel;
        this.context = context;
        this.arrSkillModel = arrSkillModel;
        lInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        if (arrSkillModel == null) {
            return arrModel == null ? 0 : arrModel.size();

        } else {
            return arrSkillModel == null ? 0 : arrSkillModel.size();

        }
    }

    @Override
    public Object getItem(int position) {
        if (arrSkillModel == null) {
            return arrModel.get(position);
        } else {
            return arrSkillModel.get(position);

        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.adapter_data_generic, parent, false);
            holderListItem = new HolderListItem();
            holderListItem.tv_Text = (TextView) view.findViewById(R.id.tv_Text);
            view.setTag(holderListItem);
        } else {
            holderListItem = (HolderListItem) view.getTag();
        }
        if (arrSkillModel != null) {
            holderListItem.tv_Text.setText(arrSkillModel.get(position).getName());

        } else {
            holderListItem.tv_Text.setText(arrModel.get(position));
        }
        return view;
    }

    private class HolderListItem {
        private TextView tv_Text;
    }
}