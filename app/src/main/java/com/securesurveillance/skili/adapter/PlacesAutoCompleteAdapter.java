package com.securesurveillance.skili.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.securesurveillance.skili.R;
import com.securesurveillance.skili.utility.PlaceAPI;

import java.util.ArrayList;

/**
 * Created by adarsh on 11/5/2018.
 */

public class PlacesAutoCompleteAdapter extends BaseAdapter implements Filterable {

    public ArrayList<String> resultList;

    Context mContext;


    public PlacesAutoCompleteAdapter(Context context, ArrayList<String> resultList) {
        mContext = context;
        this.resultList = resultList;
    }


    @Override
    public int getCount() {
        // Last item will be the footer
        return resultList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;

        //if (convertView == null) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //  if (position != (resultList.size() - 1))
        view = inflater.inflate(R.layout.autocomplete_list_item, null);
//        else
//            view = inflater.inflate(R.layout.autocomplete_google_logo, null);
        //}
        //else {
        //    view = convertView;
        //}

        //  if (position != (resultList.size() - 1)) {
        TextView autocompleteTextView = (TextView) view.findViewById(R.id.autocompleteText);
        autocompleteTextView.setText(resultList.get(position));
//        }
//        else {
//            ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
//            // not sure what to do <img draggable="false" class="emoji" alt="😀" src="https://s.w.org/images/core/emoji/72x72/1f600.png">
//        }

        return view;
    }

    @Override
    public String getItem(int position) {
        return resultList.get(position);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
//                if (constraint != null) {
//                    resultList = mPlaceAPI.autocomplete(constraint.toString());
//
//                    // Footer
//                    // resultList.add("footer");
//
//                    filterResults.values = resultList;
//                    filterResults.count = resultList.size();
//                }


                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
//                if (results != null && results.count > 0) {
//                    notifyDataSetChanged();
//                } else {
//                    notifyDataSetInvalidated();
//                }
            }
        };

        return filter;
    }

}