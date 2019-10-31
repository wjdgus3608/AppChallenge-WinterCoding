package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Adapter;

import com.example.myapp.ui.main.MainFragment;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<LectInfo> mLectList=new ArrayList<>();
    ArrayList<LectInfo> mAddedList=new ArrayList<>();
    ArrayList<Memo> mMemoList=new ArrayList<>();

    public void addMemoList(Memo memo){
        mMemoList.add(memo);
    }

    public ArrayList<Memo> getmMemoList() {
        return mMemoList;
    }

    public void setmMemoList(ArrayList<Memo> mMemoList) {
        this.mMemoList = mMemoList;
    }

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
