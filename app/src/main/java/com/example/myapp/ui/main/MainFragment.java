package com.example.myapp.ui.main;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.myapp.LectInfoAdapter;
import com.example.myapp.Memo;
import com.example.myapp.MemoAdapter;
import com.example.myapp.MemoConnect;
import com.example.myapp.TimeTableConnect;
import com.example.myapp.LectInfo;
import com.example.myapp.MainActivity;
import com.example.myapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MainFragment extends Fragment {
    MainActivity gv;
    LinearLayout main_container;
    LayoutInflater minflater;
    Calendar mNow;
    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("my","frg create!!");
        new TimetableApi().execute();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        gv=(MainActivity) getActivity();

        minflater=inflater;
        main_container=(LinearLayout) inflater.inflate(R.layout.main_container, container, false);
        inflater.inflate(R.layout.main_fragment, main_container, true);
        final TextView todate=(TextView) main_container.findViewById(R.id.now_date);
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 MM월 dd");
        String[] Today=calDay(0);
        todate.setText(Today[0]+" "+Today[1]);
        setDayView();
        Button pre_btn=(Button) main_container.findViewById(R.id.previous_btn);
        pre_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mNow.add(Calendar.DATE,-7);
                mNow.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
                String[] str=sdf.format(mNow.getTime()).split(" ");
                todate.setText(str[0]+" "+str[1]);
                setDayView();
            }
        });
        Button today_btn=(Button) main_container.findViewById(R.id.today_btn);
        today_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mNow = Calendar.getInstance();
                String[] str=sdf.format(mNow.getTime()).split(" ");
                todate.setText(str[0]+" "+str[1]);
                setDayView();
            }
        });
        Button next_btn=(Button) main_container.findViewById(R.id.next_btn);
        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mNow.add(Calendar.DATE,7);
                mNow.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
                String[] str=sdf.format(mNow.getTime()).split(" ");
                todate.setText(str[0]+" "+str[1]);
                setDayView();
            }
        });
        Button search_btn= (Button) main_container.findViewById(R.id.search_btn);
        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.main, SearchFragment.newInstance())
                        .commitNow();
            }
        });

        FrameLayout time_container=(FrameLayout) main_container.findViewById(R.id.time_container);
            Log.e("my","add size "+gv.getmAddedList().size());
            for(int i=0; i<gv.getmAddedList().size(); i++){
                LectInfo cur=gv.getmAddedList().get(i);
                makeDays(cur,inflater,time_container);
            }
        return main_container;
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // TODO: Use the ViewModel
    }

    public float convertTime(String time){
        String[] tmp=time.split(":");
        float sum=0;
        sum+=Float.parseFloat(tmp[0])*60;
        sum+=Float.parseFloat(tmp[1]);
        return sum;
    }
    public void makeDays(LectInfo cur,LayoutInflater inflater,FrameLayout time_container){
        ArrayList<Integer> days = new ArrayList<>();
        if(cur.getDays().contains(",")) {
            String[] str = cur.getDays().split(",");
            for (int i = 0; i < str.length; i++) {
                days.add(check(str[i]));
            }
        }
        else{
            days.add(check(cur.getDays()));
        }
        for(int i=0; i<days.size(); i++){
            LinearLayout time_view=(LinearLayout) inflater.inflate(R.layout.time_layout,time_container,false);
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)time_view.getLayoutParams();
// Changes the height and width to the specified *pixels*
            float start=convertTime(cur.getStart_time())-540;
            float end=convertTime(cur.getEnd_time())-540;
            DisplayMetrics outMetrics=new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
            //int dp= px/outMetrics.densityDpi;
            params.topMargin=(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,start*51/60+31,getResources().getDisplayMetrics());
            params.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,(end-start)*51/60,getResources().getDisplayMetrics());
            params.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,70,getResources().getDisplayMetrics());
            params.leftMargin=(int) (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,70*days.get(i)+39,getResources().getDisplayMetrics());
            time_view.setLayoutParams(params);
            final LectInfo data=cur;
            time_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DetailFragment fragment=new DetailFragment((LectInfo)data);
                    getFragmentManager().beginTransaction()
                            .replace(R.id.main, fragment)
                            .commitNow();
                }
            });
            TextView tv1=(TextView)time_view.findViewById(R.id.t_title);
            TextView tv2=(TextView)time_view.findViewById(R.id.t_place);
            ListView listView=(ListView)time_view.findViewById(R.id.t_memo_container);
            tv1.setText(cur.getTitle());
            tv2.setText(cur.getPlace());
            ArrayList<Memo> tmplist=new ArrayList<>();
            for(int j=0; j<gv.getmMemoList().size(); j++){
                if(cur.getCode().equals(gv.getmMemoList().get(j).getCode())){
                    tmplist.add(gv.getmMemoList().get(j));
                }
            }
            MemoAdapter mAdapter=new MemoAdapter(getActivity(),tmplist);
            listView.setAdapter(mAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    DetailFragment fragment=new DetailFragment((LectInfo)data);
                    getFragmentManager().beginTransaction()
                            .replace(R.id.main, fragment)
                            .commitNow();
                }
            });
            time_container.addView(time_view);
        }
    }

    public int check(String str){
        if(str.contains("월"))
            return 0;
        else if(str.contains("화"))
            return 1;
        else if(str.contains("수"))
            return 2;
        else if(str.contains("목"))
            return 3;
        else if(str.contains("금"))
            return 4;
        return -1;
    }

    public String[] calDay(int index){
        String[] ret=new String[2];
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 MM월 dd");
        switch (index){
            case 0:
                mNow = Calendar.getInstance();
                mNow.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
                ret=sdf.format(mNow.getTime()).split(" ");
                break;
            case 1:
                mNow.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
                ret=sdf.format(mNow.getTime()).split(" ");
                break;
            case 2:
                mNow.set(Calendar.DAY_OF_WEEK,Calendar.TUESDAY);
                ret=sdf.format(mNow.getTime()).split(" ");
                break;
            case 3:
                mNow.set(Calendar.DAY_OF_WEEK,Calendar.WEDNESDAY);
                ret=sdf.format(mNow.getTime()).split(" ");
                break;
            case 4:
                mNow.set(Calendar.DAY_OF_WEEK,Calendar.THURSDAY);
                ret=sdf.format(mNow.getTime()).split(" ");
                break;
            case 5:
                mNow.set(Calendar.DAY_OF_WEEK,Calendar.FRIDAY);
                ret=sdf.format(mNow.getTime()).split(" ");
                break;
        }
        return ret;
    }

    public void setDayView(){
        TextView mon=(TextView) main_container.findViewById(R.id.mon);
        TextView tue=(TextView) main_container.findViewById(R.id.tue);
        TextView wed=(TextView) main_container.findViewById(R.id.wed);
        TextView thu=(TextView) main_container.findViewById(R.id.thu);
        TextView fri=(TextView) main_container.findViewById(R.id.fri);


        mon.setText("Mon\n"+calDay(1)[2]);
        tue.setText("Tue\n"+calDay(2)[2]);
        wed.setText("Wed\n"+calDay(3)[2]);
        thu.setText("Thu\n"+calDay(4)[2]);
        fri.setText("Fri\n"+calDay(5)[2]);
    }

    class TimetableApi extends AsyncTask<Integer,Integer,String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Integer... integers) {
            String base_url="https://k03c8j1o5a.execute-api.ap-northeast-2.amazonaws.com/v1/programmers/timetable?user_key=8882ca818a4ee457998b5c010ae9f1a1";
            TimeTableConnect hp=new TimeTableConnect();

            return hp.run(0,base_url,"");
        }
        @Override
        protected void onPostExecute(String jsonStr) {
            super.onPostExecute(jsonStr);
            ArrayList<LectInfo> list=new ArrayList<>();
            Log.e("my","str size"+jsonStr.length());
            try {
                JSONArray jsonArray=new JSONObject(jsonStr).getJSONArray("Items");
                for(int i=0; i<jsonArray.length(); i++){
                    JSONObject jsonObject=jsonArray.getJSONObject(i);
                    LectInfo data=new LectInfo();
                    data.setCode(jsonObject.optString("lecture_code"));
                    list.add(data);
                }
            }
            catch (JSONException e){
                Log.e("my",e.toString());
            }
            gv.setmAddedList(list);

                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        String url="https://k03c8j1o5a.execute-api.ap-northeast-2.amazonaws.com/v1/programmers/lectures?code=";
                        TimeTableConnect hp=new TimeTableConnect();
                        ArrayList<LectInfo> list=new ArrayList<>();
                        for(int i=0; i<gv.getmAddedList().size(); i++) {
                            LectInfo cur = gv.getmAddedList().get(i);
                            String resString=hp.run(0, url + cur.getCode(), "");
                            try {
                                JSONArray jsonArray=new JSONObject(resString).getJSONArray("Items");
                                for(int j=0; j<jsonArray.length(); j++){
                                    JSONObject jsonObject=jsonArray.getJSONObject(j);
                                    LectInfo data=new LectInfo();
                                    data.setTitle(jsonObject.optString("lecture"));
                                    data.setStart_time(jsonObject.optString("start_time"));
                                    data.setEnd_time(jsonObject.optString("end_time"));
                                    data.setDays(jsonObject.optString("dayofweek"));
                                    data.setTeacher(jsonObject.optString("professor"));
                                    data.setPlace(jsonObject.optString("location"));
                                    data.setCode(jsonObject.optString("code"));
                                    list.add(data);
                                  }
                            }
                            catch (JSONException e){
                                Log.e("my",e.toString());
                            }
                        }
                        gv.setmAddedList(list);
                        Log.e("my","gv add size : "+gv.getmAddedList().size());
                        MemoConnect mconn=new MemoConnect();
                        String myurl="https://k03c8j1o5a.execute-api.ap-northeast-2.amazonaws.com/v1/programmers/memo?user_key=8882ca818a4ee457998b5c010ae9f1a1";
                        String ret=mconn.run(0,myurl,null);
                        ArrayList<Memo> list2=new ArrayList<>();
                        try {
                            JSONArray jsonArray=new JSONObject(ret).getJSONArray("Items");
                            for(int j=0; j<jsonArray.length(); j++){
                                JSONObject jsonObject=jsonArray.getJSONObject(j);
                                Memo data=new Memo();
                                data.setCode(jsonObject.optString("lecture_code"));
                                data.setType(jsonObject.optString("type"));
                                data.setTitle(jsonObject.optString("title"));
                                data.setDetail(jsonObject.optString("description"));
                                data.setDate(jsonObject.optString("date"));
                                list2.add(data);
                            }
                        }
                        catch (JSONException e){
                            Log.e("my",e.toString());
                        }
                        gv.setmMemoList(list2);
                        for (Fragment fragment: getFragmentManager().getFragments()) {
                            if (fragment.isVisible()) {
                                getFragmentManager().beginTransaction().detach(fragment).attach(fragment).commit();
                            }
                        }
                    }
                }.start();
        }
    }
}
