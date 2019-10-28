package com.example.myapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class LectInfoAdapter extends BaseAdapter {
    Context mContext = null;
    LayoutInflater mLayoutInflater = null;
    ArrayList<LectInfo> sample;

    public LectInfoAdapter(Context context, ArrayList<LectInfo> data) {
        mContext = context;
        sample = data;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return sample.size();
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public Object getItem(int i) {
        return sample.get(i);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View lect_view = mLayoutInflater.inflate(R.layout.lectinfo_layout, null);

        TextView title_view = (TextView)lect_view.findViewById(R.id.title_view);
        TextView time_view = (TextView)lect_view.findViewById(R.id.time_view);
        TextView cord_view = (TextView)lect_view.findViewById(R.id.cord_view);
        TextView teacher_view = (TextView)lect_view.findViewById(R.id.teacher_view);
        TextView place_view = (TextView)lect_view.findViewById(R.id.place_view);

        title_view.setText(sample.get(i).getTitle());
        time_view.setText(sample.get(i).getTime());
        cord_view.setText(sample.get(i).getCord());
        teacher_view.setText(sample.get(i).getTeacher());
        place_view.setText(sample.get(i).getPlace());

        return lect_view;
    }
}
