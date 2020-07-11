package com.securesurveillance.skili.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.securesurveillance.skili.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Hp-R on 15/09/2016.
 */
public class HomeGridViewAdapter extends BaseAdapter {
        private Context mContext;
        private  ArrayList<String> arrText;
        private ArrayList<Drawable> arrImage;
        private ArrayList<String>arrPackageName;

        public HomeGridViewAdapter(Context c, ArrayList<String> arrText, ArrayList<Drawable> arrImage, ArrayList<String>arrPackageName) {
            mContext = c;
            this.arrText = arrText;
            this.arrImage = arrImage;
            this.arrPackageName=arrPackageName;
        }

        @Override
        public int getCount() {
            return arrText.size();
        }

        @Override
        public Object getItem(int position) {
           return arrText.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View grid;
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (convertView == null) {

                grid = new View(mContext);
                grid = inflater.inflate(R.layout.listitem_promoteapp, null);
                TextView tvOptions = (TextView) grid.findViewById(R.id.tvOptions);
                ImageView ivOptions = (ImageView)grid.findViewById(R.id.ivOptions);
                tvOptions.setText(arrText.get(position));
                ivOptions.setImageDrawable(arrImage.get(position));

            } else {
                grid = (View) convertView;
            }

            return grid;
        }

}
