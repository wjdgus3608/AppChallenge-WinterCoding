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

import com.example.myapp.MemoConnect;
import com.example.myapp.TimeTableConnect;
import com.example.myapp.LectInfo;
import com.example.myapp.MainActivity;
import com.example.myapp.Memo;
import com.example.myapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DetailFragment extends Fragment {
    int add_mode=0;
    String mMemo="";
    MainActivity gv;
    public static DetailFragment newInstance() {
        return new DetailFragment();
    }
    LectInfo mData=null;
    LinearLayout main_container;
    DetailFragment(){
    }
    DetailFragment(LectInfo data){
        mData=data;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("my","onCreate!");
        Thread t=new Thread(){
            @Override
            public void run() {
                super.run();
                        MemoConnect hp=new MemoConnect();
                        String url= "https://k03c8j1o5a.execute-api.ap-northeast-2.amazonaws.com/v1/programmers/memo?user_key=8882ca818a4ee457998b5c010ae9f1a1&code="+mData.getCode();
                        mMemo=hp.run(0,url,null);
                for (Fragment fragment: getFragmentManager().getFragments()) {
                    if (fragment.isVisible()) {
                        getFragmentManager().beginTransaction().detach(fragment).attach(fragment).commit();
                    }
                }
            }
        };
        t.start();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.e("my","view created!");
        gv=(MainActivity) getActivity();
        for(int i=0; i<gv.getmAddedList().size(); i++){
            if(mData.getCode().equals(gv.getmAddedList().get(i).getCode())){
                add_mode=1;
                break;
            }
        }
        main_container=(LinearLayout) inflater.inflate(R.layout.lectdetail_layout, container, false);
        if(mData!=null){
            TextView title=(TextView)main_container.findViewById(R.id.d_title);
            TextView time=(TextView)main_container.findViewById(R.id.d_time);
            TextView code=(TextView)main_container.findViewById(R.id.d_code);
            TextView teacher=(TextView)main_container.findViewById(R.id.d_teacher);
            TextView place=(TextView)main_container.findViewById(R.id.d_place);
            LinearLayout memo_box=(LinearLayout)main_container.findViewById(R.id.memo_box);
            title.setText(mData.getTitle());
            String str=mData.getStart_time()+" - "+mData.getEnd_time()+" | "+mData.getDays();
            time.setText("강의 시간 : "+str);
            code.setText("교과목 코드 : "+mData.getCode());
            teacher.setText("담당 교수 : "+mData.getTeacher());
            place.setText("강의실 : "+mData.getPlace());
            ArrayList<Memo> list=new ArrayList<>();
            try {
                JSONArray jsonArray=new JSONObject(mMemo).getJSONArray("Items");
                for(int i=0; i<jsonArray.length(); i++){
                    JSONObject jsonObject=jsonArray.getJSONObject(i);
                    Memo data=new Memo();
                    data.setTitle(jsonObject.optString("title"));
                    data.setType(jsonObject.optString("type"));
                    data.setCode(jsonObject.optString("lecture_code"));
                    list.add(data);
                }
            }
            catch (JSONException e){
                Log.e("my",e.toString());
            }
            if(list.size()!=0){
                TextView title_box=(TextView) inflater.inflate(R.layout.memo_title,memo_box,false);
                title_box.setText("메모");
                memo_box.addView(title_box);
                for(int i=0; i<list.size(); i++){
                    final Memo cur=list.get(i);
                    String memo_str=cur.getTitle();
                    LinearLayout memo_tmp=(LinearLayout) inflater.inflate(R.layout.memo_text,memo_box,false);
                    TextView txtview=memo_tmp.findViewById(R.id.text);
                    Button btn=memo_tmp.findViewById(R.id.remove);
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            deleteMemo(cur);
                        }
                    });
                    txtview.setText(memo_str);
                    memo_box.addView(memo_tmp);
                }
            }



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
        Thread t=new Thread(){
            @Override
            public void run() {
                super.run();
                synchronized (this) {
                    TimeTableConnect hp = new TimeTableConnect();
                    Log.e("my", "before code value : " + mData.getCode());
                    hp.run(1, base_url, mData.getCode());
                    notify();
                }
            }
        };
        t.start();
        synchronized (t) {
            try{
                System.out.println("Waiting for b to complete...");
                t.wait();
            }catch(InterruptedException e){
                e.printStackTrace();
            }
            getFragmentManager().beginTransaction()
                    .replace(R.id.main, MainFragment.newInstance())
                    .commitNow();
        }
    }

    public void addMemo(){
        getFragmentManager().beginTransaction()
                .replace(R.id.main, new MemoFragment(mData))
                .commitNow();
    }

    public void deleteMemo(final Memo memo){
        Thread t=new Thread(){
            @Override
            public void run() {
                super.run();
                    String base_url="https://k03c8j1o5a.execute-api.ap-northeast-2.amazonaws.com/v1/programmers/memo";
                    MemoConnect hp = new MemoConnect();
                    hp.run(2, base_url, memo);
                Thread t=new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        MemoConnect hp=new MemoConnect();
                        String url= "https://k03c8j1o5a.execute-api.ap-northeast-2.amazonaws.com/v1/programmers/memo?user_key=8882ca818a4ee457998b5c010ae9f1a1&code="+mData.getCode();
                        mMemo=hp.run(0,url,null);
                        for (Fragment fragment: getFragmentManager().getFragments()) {
                            if (fragment.isVisible()) {
                                getFragmentManager().beginTransaction().detach(fragment).attach(fragment).commit();
                            }
                        }
                    }
                };
                t.start();
            }
        };
        t.start();
    }

}
