package com.windsoft.oneday.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.windsoft.oneday.R;

/**
 * Created by ironFactory on 2015-08-17.
 */
public class SpinnerAdapter extends ArrayAdapter<String> {

    private String [] list;
    private Context context;

    public SpinnerAdapter(Context context, int resource, int textViewResourceId, String[] objects) {
        super(context, resource, textViewResourceId, objects);
        list = objects;
        this.context = context;
    }


    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.spinner_text, parent, false);

        TextView textView = (TextView) convertView.findViewById(R.id.spinner_text);
        textView.setBackgroundColor(Color.WHITE);
        textView.setText(list[position]);
        textView.setTextColor(context.getResources().getColor(R.color.text_black));

        return super.getDropDownView(position, convertView, parent);
    }
}
