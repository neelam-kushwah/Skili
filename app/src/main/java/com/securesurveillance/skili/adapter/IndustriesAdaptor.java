package com.securesurveillance.skili.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.securesurveillance.skili.R;
import com.securesurveillance.skili.model.SkillModel;

import java.util.ArrayList;

public class IndustriesAdaptor extends BaseAdapter {
    private LayoutInflater lInflater;
    private ArrayList<SkillModel> arrModel;
    private HolderListItem holderListItem;
    private View view;
    private Context context;
    boolean[] itemChecked;
    ArrayList<Integer> checkedPosition;

    private CbListener listener;

    public IndustriesAdaptor(Context context, ArrayList<SkillModel> arrModel, ArrayList<Integer> checkedPosition) {
        this.arrModel = arrModel;
        this.context = context;
        this.checkedPosition = checkedPosition;
        lInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        itemChecked = new boolean[arrModel.size()];
        for (int i = 0; i < checkedPosition.size(); i++) {
            itemChecked[checkedPosition.get(i)]=true;
        }
        listener = (CbListener) context;
    }


    public interface CbListener {
        void onItemClicked(View v, int pos);
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.adapter_data_generic_industries, parent, false);
            holderListItem = new HolderListItem();
            holderListItem.tv_Text = (TextView) view.findViewById(R.id.tv_Text);
            holderListItem.cb = (AppCompatCheckBox) view.findViewById(R.id.cb);
            holderListItem.cb.setTag(position);
            view.setTag(holderListItem);
        } else {
            holderListItem = (HolderListItem) view.getTag();
        }

        holderListItem.tv_Text.setText(arrModel.get(position).getName());

        holderListItem.cb.setChecked(false);

        if (itemChecked[position])
            holderListItem.cb.setChecked(true);
        else
            holderListItem.cb.setChecked(false);

        holderListItem.cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                CheckBox cb = (CheckBox) v;

                int pos = Integer.parseInt(cb.getTag().toString());

                if (cb.isChecked())
                    itemChecked[position] = true;
                else
                    itemChecked[position] = false;

                listener.onItemClicked(v, position);

            }

        });

        return view;
    }


    private class HolderListItem {
        private TextView tv_Text;
        private AppCompatCheckBox cb;
    }
}