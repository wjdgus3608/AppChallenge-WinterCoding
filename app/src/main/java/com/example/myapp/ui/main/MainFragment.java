package com.example.myapp.ui.main;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.example.myapp.HttpConnect;
import com.example.myapp.LectInfo;
import com.example.myapp.MainActivity;
import com.example.myapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainFragment extends Fragment {
    MainActivity gv;
    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        gv=(MainActivity) getActivity();
        new TimetableApi().execute();
        LinearLayout main_container=(LinearLayout) inflater.inflate(R.layout.main_container, container, false);
        inflater.inflate(R.layout.main_fragment, main_container, true);
        Button search_btn= (Button) main_container.findViewById(R.id.search_btn);
        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("my","click!!");
                getFragmentManager().beginTransaction()
                        .replace(R.id.main, SearchFragment.newInstance())
                        .commitNow();
            }
        });

        FrameLayout time_container=(FrameLayout) main_container.findViewById(R.id.time_container);
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
            params.topMargin=(int)start*2;
            params.height = (int) end*2;
            params.width = 177;
            params.leftMargin=(int) 177*days.get(i);
            Log.e("my","days = "+days.get(i));
            time_view.setLayoutParams(params);
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

    class TimetableApi extends AsyncTask<Integer,Integer,Integer>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(Integer... integers) {
            String base_url="https://k03c8j1o5a.execute-api.ap-northeast-2.amazonaws.com/v1/programmers/timetable?user_key=8882ca818a4ee457998b5c010ae9f1a1";
            HttpConnect hp=new HttpConnect();
            hp.run(0,base_url,"");
            return null;
        }
        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
        }
        public ArrayList<LectInfo> jsonParse(String jsonStr){

            ArrayList<LectInfo> list=new ArrayList<>();
            Log.e("my","str size"+jsonStr.length());
            try {
                JSONArray jsonArray=new JSONObject(jsonStr).getJSONArray("Items");
                for(int i=0; i<jsonArray.length(); i++){
                    JSONObject jsonObject=jsonArray.getJSONObject(i);
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
                Log.e("my","list size"+list.size());
            }
            catch (JSONException e){
                Log.e("my",e.toString());
            }

            return list;
        }
    }
}
