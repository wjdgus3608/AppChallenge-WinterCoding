package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.myapp.ui.main.MainFragment;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<LectInfo> mLectList=new ArrayList<>();
    ArrayList<LectInfo> mAddedList=new ArrayList<>();
    public void addLecture(LectInfo data){
        mAddedList.add(data);
    }
    public ArrayList<LectInfo> getmAddedList() {
        return mAddedList;
    }

    public void setmAddedList(ArrayList<LectInfo> mAddedList) {
        this.mAddedList = mAddedList;
    }

    public ArrayList<LectInfo> getmLectList() {
        return mLectList;
    }

    public void setmLectList(ArrayList<LectInfo> mLectList) {
        this.mLectList = mLectList;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, MainFragment.newInstance())
                    .commitNow();
        }
    }
}
