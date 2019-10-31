package com.example.myapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MemoAdapter extends BaseAdapter {
    Context mContext = null;
    LayoutInflater mLayoutInflater = null;
    ArrayList<Memo> sample;

    public MemoAdapter(ArrayList<Memo> data) {
        sample = data;
    }
    public MemoAdapter(Context context, ArrayList<Memo> data) {
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
        View lect_view = mLayoutInflater.inflate(R.layout.memo_thum, null);

        TextView title_view = (TextView)lect_view.findViewById(R.id.th_text);
        title_view.setText(sample.get(i).getTitle());

        return lect_view;
    }

}
