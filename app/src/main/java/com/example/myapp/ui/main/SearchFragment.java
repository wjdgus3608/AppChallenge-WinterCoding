package com.example.myapp.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.myapp.LectInfo;
import com.example.myapp.LectInfoAdapter;
import com.example.myapp.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SearchFragment extends Fragment {
    ArrayList<LectInfo> lectList=new ArrayList<>();
    public static SearchFragment newInstance() {
        return new SearchFragment();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LinearLayout view=(LinearLayout) inflater.inflate(R.layout.search_frgment, container, false);
        ListView listView=(ListView)view.findViewById(R.id.list_container);
        LectInfo tmp=new LectInfo();
        tmp.setTitle("abc");
        tmp.setTime("123");
        tmp.setCord("777");
        tmp.setTeacher("tea");
        tmp.setPlace("here");
        LectInfo tmp2=new LectInfo();
        tmp2.setTitle("abc");
        tmp2.setTime("123");
        tmp2.setCord("777");
        tmp2.setTeacher("tea");
        tmp2.setPlace("here");
        lectList.add(tmp);
        lectList.add(tmp2);
        LectInfoAdapter lectInfoAdapter=new LectInfoAdapter(getActivity(),lectList);
        listView.setAdapter(lectInfoAdapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
}
