package com.example.myapp.ui.main;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.myapp.MemoConnect;
import com.example.myapp.TimeTableConnect;
import com.example.myapp.LectInfo;
import com.example.myapp.MainActivity;
import com.example.myapp.Memo;
import com.example.myapp.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class MemoFragment extends Fragment {
    MainActivity gv;
    LectInfo mTarget;
    LinearLayout main_container;
    MemoFragment(LectInfo target){
        mTarget=target;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        gv=(MainActivity) getActivity();
        main_container=(LinearLayout) inflater.inflate(R.layout.memo_layout, container, false);
        Button btn=(Button)main_container.findViewById(R.id.dual_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText m_title=main_container.findViewById(R.id.m_title);
                EditText m_detail=main_container.findViewById(R.id.m_detail);
                String title_str=m_title.getText().toString();
                String detail_str=m_detail.getText().toString();
                Memo memo=new Memo();
                memo.setTitle(title_str);
                memo.setDetail(detail_str);
                memo.setType("EXAM");
                memo.setCode(mTarget.getCode());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Calendar c1 = Calendar.getInstance();
                String strToday = sdf.format(c1.getTime());
                memo.setDate(strToday);
                postMemo(memo);
                getFragmentManager().beginTransaction()
                        .replace(R.id.main, MainFragment.newInstance())
                        .commitNow();
            }
        });
        return main_container;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void postMemo(final Memo memo){
        Thread t=new Thread(){
            @Override
            public void run() {
                super.run();
                synchronized (this){
                    String base_url="https://k03c8j1o5a.execute-api.ap-northeast-2.amazonaws.com/v1/programmers/memo";
                    MemoConnect hp = new MemoConnect();
                    hp.run(1, base_url, memo);
                    notify();
                }
            }
        };
        t.start();
        synchronized (t) {
            try {
                t.wait();
            } catch (InterruptedException e) {
                Log.e("my", e.toString());
            }

        }
    }
}
