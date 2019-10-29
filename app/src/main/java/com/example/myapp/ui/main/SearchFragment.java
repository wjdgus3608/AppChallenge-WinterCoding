package com.example.myapp.ui.main;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.myapp.LectInfo;
import com.example.myapp.LectInfoAdapter;
import com.example.myapp.MainActivity;
import com.example.myapp.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SearchFragment extends Fragment {
    MainActivity gv;
    ArrayList<LectInfo> lectList=new ArrayList<>();
    ListView listView;
    LectInfoAdapter lectInfoAdapter;
    public static SearchFragment newInstance() {
        return new SearchFragment();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        gv=((MainActivity)getActivity());
        LinearLayout view=(LinearLayout) inflater.inflate(R.layout.search_frgment, container, false);
        EditText editText=(EditText) view.findViewById(R.id.keyword);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String keyword=editable.toString();
                ArrayList<LectInfo> tmplist=new ArrayList<>();
                for(int i=0; i<gv.getmLectList().size(); i++){
                    LectInfo cur=gv.getmLectList().get(i);
                    if(cur.getTitle().contains(keyword)){
                        tmplist.add(cur);
                    }
                }
                lectInfoAdapter=new LectInfoAdapter(getActivity(),tmplist);
                listView.setAdapter(lectInfoAdapter);
            }
        });
        String keyword=editText.getText().toString();

        listView=(ListView)view.findViewById(R.id.list_container);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e("my",i+" clicked!!");
                DetailFragment fragment=new DetailFragment((LectInfo)adapterView.getItemAtPosition(i));
                getFragmentManager().beginTransaction()
                        .replace(R.id.main, fragment)
                        .commitNow();
            }
        });
        new LectApi().execute();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    public class LectApi extends AsyncTask<Integer , Integer , ArrayList<LectInfo>> {

        private String api_key="QJuHAX8evMY24jvpHfHQ4pHGetlk5vn8FJbk70O6";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected ArrayList<LectInfo> doInBackground(Integer... integers) {
            String base_url="https://k03c8j1o5a.execute-api.ap-northeast-2.amazonaws.com/v1/programmers/lectures";

            try {
                URL url = new URL(base_url);
                try {
                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("x-api-key", api_key);
                    conn.setRequestProperty("Content-Type","application/json");
                    if (conn.getResponseCode() == conn.HTTP_OK) {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

                        String line;
                        String page = "";

                        while ((line = reader.readLine()) != null){
                            page += line;
                        }
                        Log.e("my",page);
                        return jsonParse(page);
                    }
                    else {
                        Log.e("my", conn.getResponseCode() + " error");
                    }
                }
                catch (IOException e){
                    Log.e("my",e.toString());
                }
            }
            catch (MalformedURLException e){
                Log.e("my",e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<LectInfo> list) {
            super.onPostExecute(list);
            gv.setmLectList(list);
            lectInfoAdapter=new LectInfoAdapter(getActivity(),gv.getmLectList());
            listView.setAdapter(lectInfoAdapter);
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
