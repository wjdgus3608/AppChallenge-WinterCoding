package com.example.myapp.ui.main;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapp.HttpConnect;
import com.example.myapp.LectInfo;
import com.example.myapp.MainActivity;
import com.example.myapp.R;

import java.util.ArrayList;

public class DetailFragment extends Fragment {
    int add_mode=0;
    MainActivity gv;
    public static DetailFragment newInstance() {
        return new DetailFragment();
    }
    LectInfo mData=null;
    DetailFragment(){
    }
    DetailFragment(LectInfo data){
        mData=data;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        gv=(MainActivity) getActivity();
        for(int i=0; i<gv.getmAddedList().size(); i++){
            if(mData.getCode().equals(gv.getmAddedList().get(i).getCode())){
                add_mode=1;
                break;
            }
        }
        LinearLayout main_container=(LinearLayout) inflater.inflate(R.layout.lectdetail_layout, container, false);
        if(mData!=null){
            TextView title=(TextView)main_container.findViewById(R.id.d_title);
            TextView time=(TextView)main_container.findViewById(R.id.d_time);
            TextView code=(TextView)main_container.findViewById(R.id.d_code);
            TextView teacher=(TextView)main_container.findViewById(R.id.d_teacher);
            TextView place=(TextView)main_container.findViewById(R.id.d_place);
            title.setText(mData.getTitle());
            String str=mData.getStart_time()+" - "+mData.getEnd_time()+" | "+mData.getDays();
            time.setText("강의 시간 : "+str);
            code.setText("교과목 코드 : "+mData.getCode());
            teacher.setText("담당 교수 : "+mData.getTeacher());
            place.setText("강의실 : "+mData.getPlace());
            Button btn=(Button) main_container.findViewById(R.id.dual_btn);
            btn.setText(add_mode==0?"강의 추가":"메모 추가");
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (add_mode){
                        case 0:
                            addLecture();
                            break;
                        case 1:
                            addMemo();
                            break;
                    }
                }
            });
        }
        return main_container;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // TODO: Use the ViewModel
    }

    public void addLecture(){
        Log.e("my","add clicked!");
        gv.addLecture(mData);
        final String base_url="https://k03c8j1o5a.execute-api.ap-northeast-2.amazonaws.com/v1/programmers/timetable";
        new Thread(){
            @Override
            public void run() {
                super.run();
                HttpConnect hp=new HttpConnect();
                hp.run(1,base_url,mData.getCode());
            }
        }.start();

        getFragmentManager().beginTransaction()
                .replace(R.id.main, MainFragment.newInstance())
                .commitNow();
    }

    public void addMemo(){

    }

}
